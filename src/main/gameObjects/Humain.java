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
 * classe repr�sentant un joueur humain
 * @author Stoufa
 *
 */
public class Humain extends Joueur {

    public Humain( String pseudo, Pioche pioche, Talon talon ) {
        super( pseudo, pioche, talon );
    }


    /**
     * @return le pseudo du joueur courant
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return cha�ne d�crivant le joueur en cours
     * le joueur est identifi� par son pseudo
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
     * permet au joueur de jouer une carte
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

        // Ici, on doit attendre le joueur jusqu'a qu'il clique sur une carte jouable !
        Debug.log( "Esperando un clic..." );

        Jeu.countDownLatch = new CountDownLatch( 1 ); // r�initialisation
        Jeu.countDownLatch.await(); // DOWN : arr�ter le jeu en attendant le clique !
        // TODO : add blocking loop
 

        Debug.log( "Click received ! ..." );
       
        if ( main.nbCartes() > 1 && playedCard.couleur == Couleur.NOIR ) { // TODO : ce test doit �tre d�l�gu�e � la classe Jeu
            // On doit demander une couleur au joueur
            System.out.println( "Debes elegir un color." );
            Couleur couleur = donnerCouleur();
            // Si elle est de couleur noir, on est s�r qu'elle est sp�ciale !
            ( (CarteSpecial) playedCard ).setCouleur( couleur );
            // Changer la couleur de l'arri�re-plan
            // la couleur va �tre mis � jour automatiquement dans la m�thode render()
            //Jeu.changeBackgroundColorTo(couleur);
            if ( ( (CarteSpecial) playedCard ).symbole == Symbole.JOKER ) {
                Audio.playSound( "wildSound" );
            }
        }
        main.retirer( playedCard ); // remove the card from the player's hand !
        talon.empiler( playedCard ); // add it to the discard pile
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

                        // Puis on doit attendre si la carte est une carte noire !

                        // si la carte choisie est une carte Noir ( On ne doit pas lib�rer le 
                        // jeu jusqu'a ce que le joueur choisisse une couleur )
                        //						if (playedCard.couleur != Couleur.NOIR) {
                        //						}
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
