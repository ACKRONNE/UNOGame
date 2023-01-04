package main.gameObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.gfx.Sprite;

/**
 * cette classe repr�sente les cartes sp�ciales du jeu : +2, +4, joker, inverser, passer
 * @author Stoufa
 *
 */
public class CarteSpecial extends Carte {

    /**
     * symbole de la carte
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
     * retourne une cha�ne contenant la repr�sentation de la carte 
     */
    @Override
    public String toString() {
        return "(" + couleur.getValeur() + "," + symbole.getValeur() + ")";
    }

    /**
     * @return true : si la carte courante est compatible avec celle pass�e en param�tre (��d : jouable)
     * @return false : sinon
     * @param carte : la carte � comparer avec l'objet courant
     */
    @Override
    public boolean compatible( Carte carte ) {
        if ( couleur == Couleur.NOIR ) {
            return true; // Les cartes noirs ( nottament le Joker et la carte +4 )
            // peuvent �tre d�pos�e sur n'importe qu'elle autre carte
        }
        if ( carte instanceof CarteSpecial ) { //	CarteSpecial
            // m�me couleur ou m�me symbole ?
            return ( carte.couleur == couleur ) || ( ( (CarteSpecial) carte ).symbole == symbole ); // m�me couleur ou m�me symbole
        } else { // CarteChiffre
            // m�me couleur ?
            return carte.couleur == couleur; // m�me couleur
        }
    }

    /**
     * permet de changer la couleur de la carte sp�ciale et ceci n'est possible
     * que si la couleur initiale de la carte est NOIR
     * @param couleur : peut �tre ROUGE, JAUNE, VERT ou BLEU
     */
    public void setCouleur( Couleur couleur ) {
        // TODO : changer cette m�thode pour qu'elle change la couleur de l'arri�re-plan du jeu
        if ( this.couleur == Couleur.NOIR ) {
            // On ne peut changer la couleur que quand la couleur initiale de
            // la carte est noire : Joker ou +4
            this.couleur = couleur;
        }
    }

    /**
     * permet de retourner le symbole de la carte sp�ciale
     * @return le symbole de la carte sp�ciale
     */
    public Symbole getSymbole() {
        return symbole;
    }

    @Override
    public void update( GameContainer container ) throws SlickException {
        // TODO Auto-generated method stub

    }

}
