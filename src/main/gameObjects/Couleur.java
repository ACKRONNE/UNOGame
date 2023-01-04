package main.gameObjects;

/**
 * �número que presenta los posibles colores de las cartas del juego uno
 *
 */
public enum Couleur {

    ROUGE( "Rouge" ), JAUNE( "Jaune" ), VERT( "Vert" ), BLEU( "Bleu" ), NOIR( "Noir" );

    /**
     * el valor del color, de hecho es una cadena de caracteres que contiene el nombre del color
      * útil en el método toString()
     */
    private String valeur;

    /**
     * contructeur
     * @param valeur : el valor del color
     */
    private Couleur( String valeur ) {
        this.valeur = valeur;
    }

    /**
     * @return la variable del color
     */
    public String getValeur() {
        return valeur;
    }

    public static Couleur getCouleur( String selectedColor ) {
        switch ( selectedColor ) {
        case "Bleu":
            return BLEU;
        case "Jaune":
            return JAUNE;
        case "Rouge":
            return ROUGE;
        case "Vert":
            return VERT;
        default:
            break;
        }
        return null;
    }
}
