package main.states;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.GameRunnable;
import main.gameObjects.Jeu;

/**
 * el estado del juego
 */
public class GameState extends BasicGameState {
    /**
     *identificador de estado ( Estado )
     */
    public static int stateID;
    /**
     *objeto que representa el juego
     */
    Jeu               jeu = null;

    /**
     * constructeur
     * @param stateID identificador de estado ( estado )
     */
    public GameState( int stateID ) {
        GameState.stateID = stateID;
    }

    /**
     * inicializar el estado ( estado )
     */
    @Override
    public void init( GameContainer gc, StateBasedGame sbg ) throws SlickException {
        try {
            jeu = new Jeu(); // initialisation du jeu
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e ) {
            e.printStackTrace();
        }
        // comenzando el juego en un proceso separado ( Thread ) 
        // necesitaremos bloquear la lógica del juego hasta que obtengamos 
        // un clic en una tarjeta
        Thread thread = new Thread( new GameRunnable( jeu, sbg ) );
        thread.start();
    }

    /**
     * delta: tiempo en milisegundos transcurrido desde la llamada anterior al método update()
      * por ejemplo: 2 FPS -> 2 actualizaciones por segundo => delta = 500
      * cada 500 milisegundos se invoca update()
      * 60 FPS => 1 segundo / 60 => 1000 ms / 60 = 16.666..
     */
    @Override
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        jeu.update( container ); // mettre � jour le jeu
    }

    /**
     * dessiner la logique de l'�tat ( state ) du jeudibujar la lógica del estado del juego
     */
    @Override
    public void render( GameContainer container, StateBasedGame arg1, Graphics g ) throws SlickException {
        jeu.render( g );
    }

    /**
     * devuelve el identificador de estado
      * cada estado (estado) tiene una identificación única
      * esta identificación es útil para cambiar entre diferentes estados del juego
     */
    @Override
    public int getID() {
        return stateID;
    }
}
