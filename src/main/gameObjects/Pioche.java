package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;
import main.gfx.Sprite;

/**
 * clase que representa la baraja del juego
 *
 */
public class Pioche extends Pile {
    // TODO : debemos ocuparnos del caso en que la pila de sorteo se vacíe y debemos tomar una carta
    // en este caso, necesitamos barajar la cola (excepto la parte superior) y colocarla en la pila de sorteo

    /**
     * constructeur : le permite construir la pila de sorteo e insertar todas las cartas necesarias en ella
     * @throws SlickException 
     */
    public Pioche() throws SlickException {
        for ( Couleur couleur : Couleur.values() ) {
            if ( couleur == Couleur.NOIR ) {
                for ( int i = 0; i < 4; i++ ) { // roba 4 cartas comodín y 4 cartas +4
                    ajouter( new CarteSpecial( Couleur.NOIR, Symbole.JOKER ) );
                    ajouter( new CarteSpecial( Couleur.NOIR, Symbole.PLUS4 ) );
                }
                continue; //se agregan todas las cartas negras, pasamos al siguiente color
            }
            // 1 tarjeta 0 para cada color
            ajouter( new CarteChiffre( couleur, 0 ) );
            // 2 Tarjetas 1..9 para cada color
            for ( int i = 1; i <= 9; i++ ) {
                ajouter( new CarteChiffre( couleur, i ) );
                ajouter( new CarteChiffre( couleur, i ) );
            }
            // 2 Pasar cartas, al revés, +2 por cada color
            for ( int i = 0; i < 2; i++ ) {
                ajouter( new CarteSpecial( couleur, Symbole.PASSER ) );
                ajouter( new CarteSpecial( couleur, Symbole.INVERSER ) );
                ajouter( new CarteSpecial( couleur, Symbole.PLUS2 ) );
            }
        }

        //afficher();	// test
    }

    /**
     * Este método se usa para voltear aleatoriamente una carta en la pila de sorteo 
     * * en caso de que la primera carta sea una carta especial (al comienzo del juego)
     * @param carte
     */
    private void retournerCarte( Carte carte ) {
        int i = rand.nextInt( cartes.size() ); // Entier al�atoire entre 0 et cartes.size() - 1
        cartes.add( i, carte );
    }

    /**
     * este metodo es llamado por el heel para que devuelva su primera carta
      * la tarjeta no debe ser especial
     * @return
     */
    public Carte premiereCarteTalon() {
        Carte carte;
        while ( true ) {
            carte = depiler(); // Quitar una tarjeta
            //Debug.log(carte.toString());
            if ( carte instanceof CarteSpecial ) { // es una carta especial
                // En este caso, debe agregarse aleatoriamente a la pila de sorteo.
                retournerCarte( carte );
                //System.out.println("Oops carte sp�cial , ...");
                //System.out.println(carte);
            } else {
                return carte;
            }
        }
    }

    /**
     * el método responsable de mostrar el pico en la pantalla
     * @param g
     * @throws SlickException 
     */
    public void render( Graphics g ) throws SlickException {
        //g.drawString("testPioche", Game.WIDTH / 2, Game.HEIGHT / 2);
        Carte carteSommet = this.sommet();
        if ( carteSommet == null ) {
            return; // wait untill the draw card is populated again
        }

        Image image = Sprite.getHiddenCard();
        image.setRotation( 0 );

        carteSommet.x = Game.WIDTH / 2 - Carte.WIDTH / 2 + Integer.parseInt( Config.get( "offsetPiocheTalon" ) );
        carteSommet.y = Game.HEIGHT / 2 - Carte.HEIGHT / 2;
        carteSommet.angle = 0;
        carteSommet.updateBounds();

        // Pour voir � peu pr�s combien y en a de cartes
        float yVal = carteSommet.y;
        for ( int i = 0; i < cartes.size(); ++i ) {
            g.drawImage( image, carteSommet.x, yVal );
            if ( i % 5 == 0 ) {
                yVal -= 2;
            }
        }

        // showing number of cards
        int offset = 20;
        String str = String.valueOf( this.cartes.size() );
        g.drawString( str,
                // centered over the last card
                Game.WIDTH / 2 + Integer.parseInt( Config.get( "offsetPiocheTalon" ) )
                        - g.getFont().getWidth( str ) / 2,
                // a little bit above the last card
                yVal - offset // pour que �a soit pr�s de la carte du sommet de la pile
        //Game.HEIGHT / 2 - Carte.HEIGHT / 2 - offset
        );
    }

    public void update( GameContainer container ) throws SlickException {
        //Debug.log("pioche bounds : " + this.sommet().bounds);

        //Input input = container.getInput();
        //		if (Jeu.input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {	// click detected			
        //			int mx = Jeu.input.getMouseX(), my = Jeu.input.getMouseY();
        //			Carte carte = this.sommet();
        //			//carte.updateBounds();
        //			Debug.log("pioche bounds : " + carte.bounds);
        //			if (carte.isClicked(new Point(mx, my))) {
        //				Debug.log("pioche, update()");
        //				Audio.playSound("clickSound");
        //			}
        //		}
    }

    // test
    //	public void afficher() {
    //		for(Carte carte : cartes) {
    //			System.out.println(carte);
    //		}
    //	}

}
