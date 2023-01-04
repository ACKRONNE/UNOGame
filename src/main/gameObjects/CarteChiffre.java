package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.gfx.Sprite;

/**
 * esta clase representa las cartas de números del juego: [0..9] x [AZUL, AMARILLO, VERDE, ROJO]
 *
 */
public class CarteChiffre extends Carte {

    /**
     * El valor de la carta entre 0 y 9
     */
    private int valeur;

    /**
     * constructeur
     * @param couleur :el color de la carta
     * @param valeur : el valor de la carta
     * @throws SlickException 
     */
    public CarteChiffre( Couleur couleur, int valeur ) throws SlickException {
        super( couleur );
        this.valeur = valeur;
        image = Sprite.get( valeur, couleur );
    }

    /**
     * devuelve una cadena que contiene la representación del objeto actual
     */
    @Override
    public String toString() {
        return "(" + couleur.getValeur() + "," + valeur + ")";
    }

    /**
     * @return true :si la tarjeta actual y la pasada en parámetro son compatibles ( ��d jugable )
     * @return false : si no
     */
    @Override
    public boolean compatible( Carte carte ) {
        if ( carte instanceof CarteChiffre ) { //	Número de mapa
            // m�me couleur ou m�me valeur ?
            return ( carte.couleur == couleur ) || ( ( (CarteChiffre) carte ).valeur == valeur );
        } else { 
            return carte.couleur == couleur;
        }
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub

    }

}
