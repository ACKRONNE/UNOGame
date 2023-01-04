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
 * el estado del final del juego
 */
public class GameOverState extends BasicGameState {
    /**
     * identificador de estado ( Estado )
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
     * inicializar el estado ( estado )
     */
    @Override
    public void init( GameContainer arg0, StateBasedGame arg1 ) throws SlickException {
        // rien � initialiser !
    }

    /**
     * actualizar lógica de estado
     */
    @Override
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = container.getInput(); //utilizado� para probar la entrada del usuario (teclado y mouse)
        if ( input.isKeyPressed( Input.KEY_ESCAPE ) | input.isKeyPressed( Input.KEY_N ) ) { //tecla de escape o n
            System.exit( 0 ); // Salir de la aplicación
        }
        if ( input.isKeyPressed( Input.KEY_ENTER ) | input.isKeyPressed( Input.KEY_O ) ) { // ttecla enter o o
            // restablecer el estado del juego
            sbg.getState( GameState.stateID ).init( container, sbg );
            // cambiar al estado del juego
            sbg.enterState( GameState.stateID );
        }
    }

    /**
     * dibujar la lógica del menú
     */
    @Override
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        g.setColor( Color.white );
        String message = String.format( "Fin del juego, gano!", Jeu.joueurCourant.pseudo );
        //texto centrado y en el medio de la pantalla
        int x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2, y = gc.getHeight() / 2;
        g.drawString( message, x, y );
        message = "¿Juega de nuevo? [O/N]";
        x = gc.getWidth() / 2 - g.getFont().getWidth( message ) / 2;
        g.drawString( message, x, y + 20 );
    }

    /**
     * devuelve el identificador de estado
     */
    @Override
    public int getID() {
        return stateID;
    }
}