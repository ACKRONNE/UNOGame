package main.gameObjects;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Debug;
import main.gfx.Sprite;

/**
 * cette classe regroupe les aspets communs de toutes les cartes
 * @author Stoufa
 *
 */
public abstract class Carte extends GameObject {

    /**
     * Dimension de la carta
     */
    public static final int WIDTH = 86, HEIGHT = 129;

    /**
     * Color de la carta
     * @see Couleur
     */
    public Couleur          couleur;

    /**
     * Imagen de la carta
     */
    public Image            image = null;

    /**
     * mapa jugable o no, es decir, compatible con la tarjeta de la parte superior del talón o no
     */
    public boolean          jouable;

    /**
     *ángulo de rotación de imagen/sprite
     */
    public float            angle;
    /**
     *Área en la que se puede hacer clic en el mapa
     */
    public Shape            bounds;

    /**
     * constructeur
     * @param couleur : la couleur de la carte
     */
    public Carte( Couleur couleur ) {
        this.couleur = couleur;
        //this.bounds = new Rectangle();	// init bounds
    }

    /**
     * @param carte : la tarjeta que vemos en la parte superior del talón
     * @return true : Si la carta actual se puede dejar caer sobre el objeto (carta)
     * @return false : sinon
     * tarjeta abstracta el mecanismo de comparación difiere entre la tarjeta figura y la tarjeta especiale
     */
    public abstract boolean compatible( Carte carte );

    /**
     * el método para mostrar el mapa en la pantalla
     * @throws SlickException 
     */
    @Override
    public void render( Graphics g ) throws SlickException {
    	
        if ( image == null )
            Debug.err( this );


        this.rotate( angle ); 
        image.draw( x, y );


        if ( !jouable ) { // si el mapa no se puede reproducir, agregue un filtro gris en el mapa
            Image inactiveCardImg = Sprite.getInactiveCard();
            inactiveCardImg.setRotation( angle );
            inactiveCardImg.draw( x, y );
        }

        //updateBounds();
    }

    public void rotate( float degree ) {
        this.angle = degree;
        image.setRotation( degree ); // rotate sprite
        updateBounds();
    }

    public boolean isClicked( Point2D point ) {
        return bounds.contains( point );
    }

    public void updateBounds() {
        int rectX = (int) x, rectY = (int) y, rectWidth = WIDTH, rectHeight = HEIGHT;
        Shape rect = new Rectangle( rectX, rectY, rectWidth, rectHeight ); //creating the rectangle you want to rotate

        if ( angle == 0 ) {
            this.bounds = rect;
            return;
        }
        // rotate the rectangle
        AffineTransform transform = new AffineTransform();
        //rect.transform(transform);
        //AffineTransform transform = new AffineTransform();
        transform.rotate( Math.toRadians( angle ), rectX + rectWidth / 2, rectY + rectHeight / 2 ); //rotating in central axis
        rect = transform.createTransformedShape( rect );
        this.bounds = rect;
    }

}
