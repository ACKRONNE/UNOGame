package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.gfx.Sprite;

/**
 * esta clase representa las cartas especiales del juego: +2, +4, joker, reverse, pass
 *
 */
public class CarteSpecial extends Carte {

    /**
     * simbolo de la carta
     * @see Symbole
     */
    public Symbole symbole;

    /**
     * constructeur
     * @param couleur : El color de la carta
     * @param symbole : El simbolo de la carta
     * @throws SlickException 
     */
    public CarteSpecial( Couleur couleur, Symbole symbole ) throws SlickException {
        super( couleur );
        this.symbole = symbole;
        image = Sprite.get( symbole, couleur );
    }

    /**
     * devuelve una cadena que contiene la representación de la carta 
     */
    @Override
    public String toString() {
        return "(" + couleur.getValeur() + "," + symbole.getValeur() + ")";
    }

    /**
     * @return true : si la tarjeta actual es compatible con la pasada en parámetro (��d: jugable)
     * @return false : si no
     * @param carte : Compara la carta con el objeto actual
     */
    @Override
    public boolean compatible( Carte carte ) {
        if ( couleur == Couleur.NOIR ) {
            return true; // Cartas negras (en particular, el comodín y la carta +4)
            // se puede depositar en cualquier otra tarjeta
        }
        if ( carte instanceof CarteSpecial ) { //	CarteSpecial
            //mismo color o mismo símbolo?
            return ( carte.couleur == couleur ) || ( ( (CarteSpecial) carte ).symbole == symbole ); // m�me couleur ou m�me symbole
        } else { // CarteChiffre
            // ¿el mismo color?
            return carte.couleur == couleur; // el mismo color
        }
    }

    /**
     * permite cambiar el color de la carta especial y esto solo es posible * si el color inicial de la carta es NEGRO
     * @param couleur : Colores ROUGE, JAUNE, VERT ou BLEU
     */
    public void setCouleur( Couleur couleur ) {
        // TODO : cambia este método para cambiar el color del fondo del juego
        if ( this.couleur == Couleur.NOIR ) {
            //Solo puede cambiar el color cuando el color inicial de
            // la carta es negra: comodín o +4
            this.couleur = couleur;
        }
    }

    /**
     *voltea el símbolo de la carta especial
     * @return El simbolo de la carta especial
     */
    public Symbole getSymbole() {
        return symbole;
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub

    }

}
