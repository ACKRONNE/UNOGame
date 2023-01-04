package main.gameObjects;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import main.Game;
import main.common.Config;
import main.common.Debug;
import main.gfx.Sprite;
import main.io.Audio;
import main.io.GetColorDialog;
import main.states.GameOverState;

/**
 * clase que representa el juego, es responsable del progreso del juego seguido
  * el significado del juego, la activación de efectos especiales, ...
 *
 */
public class Jeu {
    /**
     * La punta
     */
    private Pioche               pioche;
    /**
     * talon 
     */
    private Talon                talon;
    /**
     * el número de jugadores (debe estar entre 2 y 4)
     */
    private int                  nbJoueurs;
    /**
     * Lista de jugadores
     */
    private Joueur[]             joueurs;
    /**
     * la dirección de juego, puede tener dos valores posibles: -1 por la derecha �
      * izquierda, 1 de izquierda a derecha [0] + (1) -> [1] ... [1] + (-1) -> [0] ...
     */
    private int                  sens  = -1;                 //predeterminado a la izquierda
    /**
     * el índice del reproductor actual, inicialmente el primero
     */
    static int                   tour  = 0;
    /**
     * el objeto Player en el índice PlayerCurrent índice de la tabla de jugadores
     */
    public static Joueur         joueurCourant;
    /**
     * entero que contiene el id del jugador actual
     */
    //public static int tour = 1;
    static Input                 input = null;
    /**
     * utilizado� para detener el juego hasta que el jugador haga clic en una carta!
     */
    static CountDownLatch        countDownLatch;
    /**
     * solía esperar el color elegido por el jugador
     */
    public static CountDownLatch waitForDialogCountDownLatch;
    //	public static boolean clickReceived;
    //	public static boolean colorReceived;
    /**
     * dialogue du choix de la couleur
     */
    public static GetColorDialog dialog;

    /**
     * constructeur
     * 
     * @throws SlickException
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    public Jeu() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        init();
    }

    /**
     * permet de d�marrer le jeu
     * @param sbg 
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     * @throws LWJGLException 
     */
    public void lancer( StateBasedGame sbg ) throws InterruptedException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SlickException {
        Audio.playMusic();
        System.out.println( "=== El juego comienza ===" );
        boolean effetSpecial = false; // esta variable permite recorrer hasta el infinito en el caso de tarjetas especiales
        while ( true ) { // bucle de juego
            joueurCourant = joueurs[tour];
            System.out.println( "Torre de " + joueurCourant.pseudo );
            // efecto especial: ¡para que no activemos el efecto más de una vez!
            // por ejemplo: 2 jugadores el primero juega una carta especial, el otro no tiene
            // cartas jugables, asi pasa su turno, ahi, no debemos reactivar el efecto especial
            // desde la tarjeta hasta la parte superior del talón
            if ( effetSpecial && talon.sommet() instanceof CarteSpecial ) { // el jugador anterior actualizo un mapa especial
                effetSpecial = false;
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PASSER ) { // el jugador actual debe pasar
                    System.out.println( joueurCourant.pseudo + " debe pasar su turno -> efecto de la carta: "
                            + talon.sommet().toString() );
                    Audio.playSound( "skipSound" );
                    joueurSuivant();
                    continue;
                }
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PLUS2 ) { //el jugador anterior jugó +2
                    // el jugador actual debe sacar 2 cartas
                    System.out.println( joueurCourant.pseudo
                            + " debe robar 2 cartas y saltarse su turno -> efecto de carta " + talon.sommet() );
                    Audio.playSound( "plus2Sound" );
                    for ( int i = 0; i < 2; i++ ) {
                        joueurCourant.prendreCarte();
                    }
                    // y pasar
                    joueurSuivant();
                    continue;
                }
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.PLUS4 ) { //el jugador anterior jugó +4
                    // el jugador actual debe sacar 4 cartas
                    System.out.println( joueurCourant.pseudo
                            + " debe robar 4 cartas y pasar turno -> efecto de carta " + talon.sommet() );
                    Audio.playSound( "plus4Sound" );
                    for ( int i = 0; i < 4; i++ ) {
                        joueurCourant.prendreCarte();
                    }
                    // y pasar
                    joueurSuivant();
                    continue;
                }
            }
            joueurCourant.jouerTour();
            Debug.log( "======== Fin de Ronda ========" );
            // TODO : ¡Las cartas de mano de los jugadores deben actualizarse después de cada ronda! (jugabilidad!)
            updatePlayersHands();

            if ( joueurCourant.nbCartes() == 0 ) { // probamos si el jugador actual ha vaciado su mano
                System.out.println( joueurCourant.pseudo + " ¡ganado!" );
                Audio.playSound( "winSound" );
                break;
            }
            if ( joueurCourant.nbCartes() == 1 ) { // probamos si al jugador actual solo le queda una carta (¡UNO!) en su mano
                System.out.println( joueurCourant.pseudo + " <UNO!>" );
                Audio.playSound( "unoSound" );
            }
            // Debemos probar aquí si el jugador tiene duplicados de la carta jugada TODO
            // Debemos probar el mapa inverso en este nivel
            if ( joueurCourant.playedCard != null && talon.sommet() instanceof CarteSpecial ) { // el jugador actual ha jugado una carta especial
                // la prueba en la carta jugada para no volver a invertir la dirección en el caso de que
                // ¡un jugador ha invertido y el siguiente jugador no tiene cartas jugables! sin esto
                // probar la dirección se invertirá de nuevo!
                effetSpecial = true; // activar el efecto especial
                if ( ( (CarteSpecial) talon.sommet() ).getSymbole() == Symbole.INVERSER ) {
                	// el jugador actual ha invertido la dirección
                    System.out.println( joueurCourant.pseudo + " invirtió la dirección del juego" );
                    Audio.playSound( "reverseSound" );
                    sens *= -1; // el valor del significado es 1 o -1, multiplicamos por -1 para cambiar
                    effetSpecial = false; // el efecto especial se activa en este caso
                }
            }
            joueurSuivant();
            System.out.println();
        }
        System.out.println( "=== Fin del juego ===" );
        Audio.stopMusic();
        // go to the game over state
        sbg.enterState( GameOverState.stateID );
    }

    /**
     * jugabilidad actualizada del mapa del jugador
    * @param joueurCourant 
     */
    private void updatePlayersHands() {
        for ( Joueur joueur : joueurs ) { // Para cada jugador...
            for ( Carte carte : joueur.main.cartes ) { // Repasamos sus cartas...
            	// Y los actualizamos (jugabilidad con el mapa de la parte superior del talón)
                carte.jouable = carte.compatible( talon.sommet() );
            }
        }
    }

    /**
     *te permite pasar al siguiente jugador según la dirección del juego
     */
    private void joueurSuivant() {
        // avanzar al siguiente jugador
        tour += sens;
        //Debemos verificar si el índice d excede los límites de la tabla
        if ( tour < 0 ) {
            tour += nbJoueurs;
            //ejemplo: 3 jugadores -> [ 0 , 1 , 2 ]
            // 0 + (-1) = -1 < 0 -> -1 + 3 -> 2 (último jugador)
        }
        if ( tour > nbJoueurs - 1 ) { // indice >= nbJoueurs
            tour -= nbJoueurs;
            // ejemplo : 3 jugadores -> [ 0 , 1 , 2 ]
            // 2 + (1) = 3 > 2 -> 3 - 3 -> 0 ( primer jugador )
        }
    }

    public void update( GameContainer container ) throws SlickException {
        input = container.getInput();
        for ( int i = 0; i < joueurs.length; i++ ) { // actualizar el estado del jugador
            joueurs[i].update( container );
        }
    }

    /**
     * te permite ver actualizaciones en el juego
     * 
     * @param g
     * @throws SlickException
     */
    public void render( Graphics g ) throws SlickException {
        changeBackgroundColorTo( talon.sommet().couleur );

        for ( int i = 0; i < joueurs.length; i++ ) { //exhibición de jugadores
            joueurs[i].render( g );
        }

        pioche.render( g );
        talon.render( g );
    }

    public static void changeBackgroundColorTo( Couleur couleur ) throws SlickException {
        Image bgImage = Sprite.getBackground( couleur );
        if ( bgImage == null ) {
            Debug.err( "¿Última carta negra jugada?" );
            return;
        }
        bgImage.draw( 0, 0, Game.WIDTH, Game.HEIGHT );
    }

    public void init() throws SlickException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        //countDownLatch = new CountDownLatch(1);
        //waitForDialogCountDownLatch = new CountDownLatch(1);	// used to wait the dialog

        dialog = new GetColorDialog();
        //		dialog.setVisible(false);	// hidden by default

        Sprite.load();
        Config.load();
        pioche = new Pioche();
        pioche.melanger();
        talon = new Talon( pioche );

        // TODO : get nbJoueurs from server

        nbJoueurs = Integer.parseInt( Config.get( "nbJoueurs" ) );
        joueurs = new Joueur[nbJoueurs];
        Position positionsJoueurs[] = new Position[nbJoueurs];

        switch ( nbJoueurs ) {
        case 2:
            // we place the players face to face :)
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.HAUT;
            break;
        case 3:
            // we place the players like so : right, left, down
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.DROITE;
            positionsJoueurs[2] = Position.GAUCHE;
            break;
        case 4:
            // we place the players in the four edges of the screen, making a
            // cercle
            positionsJoueurs[0] = Position.BAS;
            positionsJoueurs[1] = Position.DROITE;
            positionsJoueurs[2] = Position.HAUT;
            positionsJoueurs[3] = Position.GAUCHE;
            break;
        default:
            break;
        }

        // 1er jugador Humano, los otros son Bots
        for ( int i = 0; i < nbJoueurs; i++ ) {
            //String pseudoJoueur = Config.get("j" + i);	// getting player names
            //joueurs[i] = new Humain(pseudoJoueur, pioche, talon);
            //joueurs[i] = new Bot(pseudoJoueur, pioche, talon);

            if ( i == 0 ) {
                // first player is humain
                String pseudoJoueur = JOptionPane.showInputDialog( "Nombre -> " );
                joueurs[i] = new Humain( pseudoJoueur, pioche, talon );
            } else {
                // the rest are bots
                joueurs[i] = new Bot( "Bot" + i, pioche, talon );
            }

            joueurs[i].position = positionsJoueurs[i];
            joueurs[i].id = i;
        }

        /*
        // Todos los jugadores son humanos.
        for (int i = 0; i < nbJoueurs; i++) {
        	String pseudoJoueur = JOptionPane.showInputDialog( String.format("pseudo (%d) ? >> ", i + 1) );
        	joueurs[i] = new Humain(pseudoJoueur, pioche, talon);
        	joueurs[i].position = positionsJoueurs[i];
        	joueurs[i].id = i;
        }
        */

        for ( int i = 0; i < joueurs.length; i++ ) { // distribución de cartas
            for ( int j = 0; j < 7; j++ ) { //Cada jugador tomará 7 cartas.
                joueurs[i].prendreCarte();
            }
        }

    }

}
