package main.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * el estado del menú que se muestra al comienzo del juego
 *
 */
public class MenuState extends BasicGameState {
    /**
     * lista de opciones
     */
    private String[]  menuOptions = new String[] {
            "Jouer",
            "Quitter"
    };
    /**
     * le permite saber qué opción ha seleccionado (inicialmente la primera)
     */
    private int       index       = 0;
    /**
     * identificador de estado ( Estado )
     */
    public static int stateID;

    /**
     * constructeur
     * @param stateID identificateur de l'�tat ( state )
     */
    public MenuState( int stateID ) {
        MenuState.stateID = stateID;
    }

    /**
     * inicializar el estado del menú
     */
    @Override
    public void init( GameContainer arg0, StateBasedGame arg1 ) throws SlickException {
        // rien � initialiser !
    }

    @Override
    /**
     *actualizar lógica de estado
     */
    public void update( GameContainer container, StateBasedGame sbg, int delta ) throws SlickException {
        Input input = container.getInput(); // utilizado� para probar la entrada del usuario (teclado y mouse)
        if ( input.isKeyPressed( Input.KEY_UP ) ) { // si se presiona la tecla de flecha hacia arriba�
            index--;
            if ( index == -1 ) { //si hemos desbordado la mesa superior...
                index = menuOptions.length - 1; // ... on recomendar desde abajo
            }
        } else if ( input.isKeyPressed( Input.KEY_DOWN ) ) { // si se presiona la tecla de flecha hacia abajo�
            index++;
            if ( index == menuOptions.length ) { // si hemos desbordado la mesa de abajo...
                index = 0; // ... comenzamos de nuevo desde arriba
            }
        } else if ( input.isKeyPressed( Input.KEY_ENTER ) ) { // si se presiona la tecla Enter
            if ( index == 0 ) { // primera opción: Jugar
                //Entramos en el Estado del juego con transiciones
                sbg.enterState( GameState.stateID, new FadeOutTransition(), new FadeInTransition() );
            } else if ( index == 1 ) { // 2eme option : Quitter
                // Salir de la aplicación
                System.exit( 0 );
            }
        }
    }

    /**
     * dibujar la lógica del menú
     */
    @Override
    public void render( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
        for ( int i = 0; i < menuOptions.length; ++i ) { // Repasamos todas las opciones
            if ( i == index ) { // la opción seleccionada...
                g.setColor( Color.red ); // ... es de color rojo
            } else { // los otros
                g.setColor( Color.white ); // son de color blanco
            }
            int step = gc.getHeight() / ( menuOptions.length + 1 ); // el espacio entre las diferentes opciones
            /*
             * ----------
             * <step>
             * option1
             * <step>
             * option2
             * <step>
             * ----------
             * Por lo tanto, debemos dividir la altura de la ventana por (el número de opciones + 1)
             */
         // mostrar la opción centrada
            g.drawString(
                    menuOptions[i],
                    gc.getWidth() / 2 - g.getFont().getWidth( menuOptions[i] ) / 2,
                    step * ( i + 1 ) );
        }
    }

    /**
     * devuelve el identificador de estado
     */
    @Override
    public int getID() {
        return stateID;
    }
}
