package main.gameObjects;

/**
 * �numeración que representa los diferentes símbolos que pueden tener las cartas del juego
 *
 */
public enum Symbole {

    PASSER( "Passer" ), INVERSER( "Inverser" ), PLUS2( "+2" ), PLUS4( "+4" ), JOKER( "Joker" );

    /**
     * Numeración que representa los diferentes símbolos que pueden contener los mapas del juego toString()
     */
    private String valeur;

    /**
     * constructeur
     * @param valeur :valor del símbolo
     */
    private Symbole( String valeur ) {
        this.valeur = valeur;
    }

    /**
     * @return valor del símbolo
     */
    public String getValeur() {
        return valeur;
    }
}
