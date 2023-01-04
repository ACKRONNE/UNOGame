package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.gameObjects.Jeu;

/**
 * @author Stoufa
 * l'�tat ( State ) de fin du  jeu
 */
public class GameOverState extends BasicGameState {
    /**
     * identificateur de l'�tat ( State )
     */
    public static int stateID;

    /**
     * constructeur
     * @param stateID identificateur de l'�tat ( state )
     */
    public GameOverState( int stateID ) {
        GameOverState.stateID = stateID;
    }

    /**
     * initialiser l'�tat ( state )
     */
    @Override
    public void init( GameContainer arg0, StateBasedGame arg1 ) throws SlickException {
        // rien � initialiser !
    }

    /**
     * mettre � jour la logique de l'�tat
     */
    @Override
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = container.getInput(); // utilis� pour tester l'entr�e de l'utilisateur ( clavier et souris )
        if ( input.isKeyPressed( Input.KEY_ESCAPE ) | input.isKeyPressed( Input.KEY_N ) ) { // touche �chap ou n
            System.exit( 0 ); // Quitter l'application
        }
        if ( input.isKeyPressed( Input.KEY_ENTER ) | input.isKeyPressed( Input.KEY_O ) ) { // touche entr�e ou o
            // r�initialisation de l'�tat du jeu
            sbg.getState( GameState.stateID ).init( container, sbg );
            // commuter vers l'�tat du jeu
            sbg.enterState( GameState.stateID );
        }
    }

    /**
     * dessiner la logique du menu
     */
    @Override
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        g.setColor( Color.white );
        String message = String.format( "Fin del juego, gano!", Jeu.joueurCourant.pseudo );
        // texte centr� et au milieu de l'�cran
        int x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2, y = gc.getHeight() / 2;
        g.drawString( message, x, y );
        message = "¿Juega de nuevo? [O/N]";
        x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2;
        g.drawString( message, x, y + 20 );
    }

    /**
     * retourne l'identificateur de l'�tat
     */
    @Override
    public int getID() {
        return stateID;
    }
}