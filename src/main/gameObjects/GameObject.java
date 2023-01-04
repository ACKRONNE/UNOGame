package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Clase abstracta que representa un objeto de juego.
 * @author Stoufa
 *
 */
public abstract class GameObject {

    /**
     * posiciones de los objetos en la pantalla
     */
    public float x    = 0, y = 0;
    /**
     * velocidades de movimiento de objetos
     */
    public float velX = 0, velY = 0; // velocity : speed

    /**
     * permite actualizar el estado del objeto
     * @param container
     * @throws SlickException 
     */
    public abstract void update( GameContainer container ) throws SlickException;

    /**
     * muestra el estado actual del objeto
     * @param g
     * @throws SlickException
     */
    public abstract void render( Graphics g ) throws SlickException;

}
