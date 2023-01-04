package main.gameObjects;

import java.util.ArrayList;

/**
 * esta clase representa la mano de cada jugador
 *
 */
public class Main {

    /**
     * todas las cartas en la mano
     */
    protected ArrayList<Carte> cartes;

    /**
     * constructeur
     */
    public Main() {
        cartes = new ArrayList<Carte>();
    }

    /**
     * te permite añadir una carta a la mano
     * @param carte :la tarjeta para agregar
     */
    public void ajouter( Carte carte ) {
        cartes.add( carte );
    }

    /**
     *quita una carta de la mano
     * @param num : el índice de la tarjeta que se eliminará de la lista
     * @return tarjeta eliminada
     */
    public Carte retirer( int num ) {
        return cartes.remove( num );
    }

    /*
    public void retirer(Carte carte) {
    	cartes.remove(carte);
    }
    */

    /**
     * te permite voltear todas las cartas en tu mano
     * @return todas las cartas en la mano
     */
    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    /**
     * @return el número de cartas en la mano
     */
    public int nbCartes() {
        return cartes.size();
    }

    @Override
    public String toString() {
        String str = "";
        for ( Carte carte : cartes ) {
            str = str + carte.toString();
        }
        return str;
    }

    /**
     * quita la carta de la mano
     * @param playedCard
     */
    public void retirer( Carte playedCard ) {
        cartes.remove( playedCard );
    }

}
