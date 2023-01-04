package main.gameObjects;

import java.awt.Point;
import java.util.concurrent.CountDownLatch;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;

/**
 * clase que representa a un jugador humano
 *
 */
public class Humain extends Joueur {

    public Humain( String pseudo, Pioche pioche, Talon talon ) {
        super( pseudo, pioche, talon );
    }


    /**
     * @return el apodo del jugador actual
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return cadena que describe al jugador actual * el jugador se identifica por su apodo
     */
    @Override
    public String toString() {
        return "[Humano] : " + getPseudo();
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        //this.afficherMain(g);
        super.render( g );
    }

    /**
     * permet au joueur de donner une couleur
     * @return la couleur choisie
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws InterruptedException 
     * @throws SlickException 
     */
    private Couleur donnerCouleur() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, InterruptedException, SlickException {
        // Here we should show the dialog and wait for a response from the user
        Jeu.waitForDialogCountDownLatch = new CountDownLatch( 1 ); // used to wait the dialog


        Jeu.dialog.selectedColor = null; // reset chosen color value
        Jeu.dialog.setVisible( true ); // showing dialog

        //Jeu.waitForDialogCountDownLatch = new CountDownLatch(1);	// r�initialisation du bloqueur
        Debug.log( "Esperando diálogo..." );
        Jeu.waitForDialogCountDownLatch.await(); // hold


        Debug.log( "El diálogo está hecho..." );

        //Jeu.dialog.setVisible(false);	// hiding dialog
        // TODO : add blocking loop
        //		Jeu.colorReceived = false;
        //		while (!Jeu.colorReceived) {	// waiting for a color
        //			Thread.sleep(1000);
        //		}

        Debug.log( "color elegido: " + Jeu.dialog.selectedColor );
        Couleur couleur = Couleur.getCouleur( Jeu.dialog.selectedColor );
        return couleur;
    }

    /**
     *permite al jugador jugar una carta
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     */
    @Override
    public void jouerCarte() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException {


        System.out.println( "Tienes " + nbCartesJouables() + " cartas jugables" );

        // ¡Aquí, tenemos que esperar al jugador hasta que haga clic en una carta jugable!
        Debug.log( "Esperando un clic..." );

        Jeu.countDownLatch = new CountDownLatch( 1 ); // r�initialisation
        Jeu.countDownLatch.await(); // DOWN :¡detén el juego mientras esperas el clic!
        // TODO : añadir bucle de bloqueo
 

        Debug.log( "Click received ! ..." );
       
        if ( main.nbCartes() > 1 && playedCard.couleur == Couleur.NOIR ) { // TODO :esta prueba debe ser delegada a la clase Game
            // Debemos pedirle al jugador un color
            System.out.println( "Debes elegir un color." );
            Couleur couleur = donnerCouleur();
            // Si es negro, ¡seguro que es especial!
            ( (CarteSpecial) playedCard ).setCouleur( couleur );
            // Cambiar el color de fondo // el color se actualizará automáticamente en el método render()
            //Jeu.changeBackgroundColorTo(couleur);
            if ( ( (CarteSpecial) playedCard ).symbole == Symbole.JOKER ) {
                Audio.playSound( "wildSound" );
            }
        }
        main.retirer( playedCard ); // ¡quita la carta de la mano del jugador!
        talon.empiler( playedCard ); //añádelo a la pila de descarte
        System.out.println( pseudo + " jugó " + playedCard );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        //		// test
        //		Debug.log("id = " + this.id);
        //		Debug.log(main.toString());
        if ( id != Jeu.tour ) {
            return; // ce n'est pas le tour de ce joueur !
        }
        //Input input = container.getInput();
        if ( Jeu.input.isMousePressed( Input.MOUSE_LEFT_BUTTON ) ) { // click detected
            Debug.log( "update() / id = " + this.id );
            int mx = Jeu.input.getMouseX(), my = Jeu.input.getMouseY();
            for ( int i = main.cartes.size() - 1; i >= 0; --i ) {
                Carte carte = main.cartes.get( i );
                if ( carte.isClicked( new Point( mx, my ) ) ) { // the click was on one of the cards
                    if ( carte.jouable ) { // if the card is playable
                        //Debug.log(carte.toString());
                        System.out.println( "carta hecha clic!" );
                        if ( carte instanceof CarteChiffre ) {
                            // play the sound only for numerical cards !
                            // special cards have special sound effects and we
                            // don't want to mix them up !
                            Audio.playSound( "clickSound" );
                        }
                        this.playedCard = carte; // saving the played card

                        Debug.log( "Esperando un clic de carta..." );
                        Jeu.countDownLatch.countDown(); // UP : release the block, MUST BE AFTER SETTING THE CARD OR ELSE NullException !
                        Debug.log( "Card click detected!" );
                        //Jeu.clickReceived = true;	// release the block, MUST BE AFTER SETTING THE CARD OR ELSE NullException !

                        // ¡Entonces tenemos que esperar si la carta es una carta negra!

                        // si la carta elegida es una carta Negra (No debemos soltar la
                        // juego hasta que el jugador elija un color)
                        // if (cartajugada.color != Color.NEGRO) {
                        // }
                        // TODO : add a different sound if the card isn't playable :)
                        //carte.jouable = !carte.jouable;	// test
                        System.out.println( carte );
                        break; // stop propagating the click event !
                    } else {
                        Debug.log( "¡Esta carta no se puede jugar!" );
                        Audio.playSound( "invalidClickSound" );
                        break; // break the loop ! no need to check for the other cards !
                    }
                }
            }
            Debug.log( "==================================================" );
        }
    }

}
