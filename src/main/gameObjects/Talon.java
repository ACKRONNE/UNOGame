package main.gameObjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.common.Config;

/**
 * clase que representa el stub del juego
 */
public class Talon extends Pile {

    /**
     * constructeur
     * @param pioche : la baraja del juego de la que se debe tomar una carta para la cual es la primera carta de la reserva
     */
    public Talon( Pioche pioche ) {
        ajouterPremiereCarte( pioche );
    }

    /**
     * permite agregar la primera carta (al comienzo del juego)
     * @param pioche : la pila de sorteo de la que desea robar la carta
     */
    private void ajouterPremiereCarte( Pioche pioche ) {
        empiler( pioche.premiereCarteTalon() );
        /*
        while (true) {
        	// Retirer une carte
        	Carte carte = pioche.depiler();
        	if (carte instanceof CarteSpecial) {	// C'est une carte sp�ciale
        		// Il faut dans ce cas la rajouter al�atoirement dans la pioche
        		pioche.retournerCarte(carte);
        		//System.out.println("Oops carte sp�cial , ...");
        		//System.out.println(carte);
        	} else {
        		this.empiler(carte);
        		return;
        	}
        }
        */
    }

    /**
     *devuelve una cadena que describe el código auxiliar
     */
    @Override
    public String toString() {
        String str = "";
        if ( cartes.isEmpty() ) {
            str = "[VIDE]";
        }
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            str = str + i + ") " + carte.toString();
            if ( i != cartes.size() - 1 ) { // si esta no es la última iteración
                str = str + "\n"; //añadir un retorno de carro
            }
        }
        return str;
    }

    /**
     * muestra el talón
     */
    public void afficher() {
        System.out.println( this );
    }

    /**
     * permite que el mapa se muestre en la parte superior del stub
     */
    public void afficherSommet() {
        System.out.println( "Pico del talón: " + sommet().toString() );
    }

    /**
     * el método responsable de mostrar el stub en la pantalla
     * @param g
     * @throws SlickException 
     */
    public void render( Graphics g ) throws SlickException {
        Carte carteSommet = this.sommet();

        Image image = this.sommet().image;
        image.setRotation( 0 );

        carteSommet.x = Game.WIDTH / 2 - Carte.WIDTH / 2 - Integer.parseInt( Config.get( "offsetPiocheTalon" ) );
        carteSommet.y = Game.HEIGHT / 2 - Carte.HEIGHT / 2;
        carteSommet.angle = 0;
        carteSommet.updateBounds();

        // Para ver aproximadamente cuántas tarjetas hay
        float yVal = carteSommet.y;
        for ( int i = 0; i < cartes.size(); ++i ) {
            g.drawImage( image, carteSommet.x, yVal );
            if ( i % 5 == 0 ) {
                yVal -= 2;
            }
        }

        // mostrando el número de tarjetas
        int offset = 20;
        String str = String.valueOf( this.cartes.size() );
        g.drawString( str,
                // centered over the last card
                Game.WIDTH / 2 - Integer.parseInt( Config.get( "offsetPiocheTalon" ) )
                        - g.getFont().getWidth( str ) / 2,
                // a little bit above the last card
                yVal - offset // para que esté cerca de la carta superior de la pila
        //Game.HEIGHT / 2 - Carte.HEIGHT / 2 - offset
        );
    }

 // No necesitamos implementar el método update() para el stub, el clic
    // ¡en el talón no tiene un efecto especial!
    //	public void update(GameContainer container) throws SlickException {
    //		Input input = container.getInput();
    //		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {	// click detected			
    //			int mx = input.getMouseX(), my = input.getMouseY();
    //			Carte carte = this.sommet();
    //			if (carte.isClicked(new Point(mx, my))) {
    //				Debug.log("talon, update()");
    //				Audio.playSound("clickSound");
    //			}
    //		}
    //	}

}
