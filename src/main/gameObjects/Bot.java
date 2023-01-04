package main.gameObjects;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.io.Audio;

/**
 * clase que representa a un jugador no humano
 * Esta clase se puede enriquecer implementando diferentes estrategias de juego:
 * estrategia de aufencife (jugar +2 y +4 cartas primero),
 * defensivo (jugar cartas +2 y +4 último),
 * mantener el color actual,
 * mantener las cartas negras al final, ....
 *
 */
public class Bot extends Joueur {

    Random random = new Random(); // usado para simular el comportamiento aleatorio de un jugador

    public Bot( String pseudo, Pioche pioche, Talon talon ) {
        super( pseudo, pioche, talon );
        // TODO Auto-generated constructor stub
    }

    @Override
    public void render( Graphics g ) throws SlickException {
        super.render( g );
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub
    }

    @Override
    public void jouerCarte() throws SlickException {
        // TODO choose randomly !
        System.out.println( "El bot tiene " + nbCartesJouables() + " cartas jugables" );
        Debug.log( "Esperando el movimiento del bot..." );
        try {
            Thread.sleep( 2000 ); // Thinks for 2 seconds
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Carte carte = null;
        int nbEssais = 100;
        int indiceCarte = 0;
        do {
            if ( nbEssais > 0 ) {
                nbEssais--;
                indiceCarte = random.nextInt( main.cartes.size() );
            } else {
                indiceCarte = ( indiceCarte + 1 ) % main.cartes.size();
            }
            carte = main.cartes.get( indiceCarte );
            if ( !carte.jouable ) {
                Debug.log( "la tarjeta " + carte + " no es jugable" );
            }
        } while ( !carte.jouable );
        if ( carte instanceof CarteChiffre ) {
        	// reproduce el sonido solo para cartas numéricas!
            // las cartas especiales tienen efectos de sonido especiales y nosotros
            // ¡No quiero mezclarlos!
            Audio.playSound( "clickSound" );
        }
        this.playedCard = carte; // guardando la carta jugada

        if ( playedCard.couleur == Couleur.NOIR ) {
            // Debemos pedirle al jugador un color
            Debug.log( "El bot debe elegir un color..." );
            Couleur couleur = donnerCouleur();
         // ¡Si es negro, estamos seguros de que es especial!
            ( (CarteSpecial) playedCard ).setCouleur( couleur );
            // Cambiar el color de fondo 
            // el color se actualizará automáticamente en el método render() 
            // Game.changeBackgroundColorTo(color);
            if ( ( (CarteSpecial) playedCard ).symbole == Symbole.JOKER ) {
                Audio.playSound( "wildSound" );
            }
        }

        main.retirer( playedCard ); // remove the card from the player's hand !
        talon.empiler( playedCard ); // add it to the discard pile
        Debug.log( pseudo + " jugó " + playedCard );
    }

    @Override
    public String toString() {
        return "[Bot] : " + pseudo;
    }

    private Couleur donnerCouleur() {
        Debug.log( "Esperando la elección de color del bot" );
        String[] colorNames = { "Jaune", "Vert", "Bleu", "Rouge" };
        String selectedColor = null;
        try {
            Thread.sleep( 2000 ); // Thinks for 2 seconds
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        selectedColor = colorNames[random.nextInt( colorNames.length )];
        Debug.log( "El bot seleccionó el color: " + selectedColor );
        return Couleur.getCouleur( selectedColor );
    }

}
