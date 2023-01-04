package main.gfx;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.common.Config;
import main.common.Debug;
import main.gameObjects.Couleur;
import main.gameObjects.Symbole;

/**
 *clase utilizada para cargar y devolver sprites/imágenes del juego
 *
 */
public class Sprite {
    /**
     * inactive : ¡el sprite / imagen gris transparente para agregar encima de las cartas no jugables!
     * hidden : el sprite/imagen del mapa oculto
     * separados de la lista de sprites, porque son los más utilizados (optimización)
     */
    //public static Image inactive = null, hidden = null;
    /**
     * liste des sprites
     */
    public static HashMap<String, Image> sprites = new HashMap<>();

    /**
     *devuelve el sprite del mapa con el símbolo pasado como parámetro
     * @param symbole	symbole de la carte dont on veut afficher
     * @return	l'image de la carte demand�e
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( Symbole symbole ) throws SlickException { // +4, Joker
        switch ( symbole ) {
        case PLUS4:
            return get( "+4" );
        case JOKER:
            return get( "wild" );
        default:
            break;
        }
        Debug.err( "Image null ! " + symbole );
        return null;
    }

    /**
     *devuelve el sprite del mapa con el nombre pasado como parámetro
     * @param spriteName nom de la carte dont on veut afficher
     * @return	l'image de la carte demand�e
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    private static Image get( String spriteName ) throws SlickException {
        if ( !sprites.containsKey( spriteName ) )
            Debug.err( "Image null ! " + spriteName );
        return sprites.get( spriteName );
    }

    /**
     *devuelve el sprite del mapa que tiene el símbolo y el color pasados ​​en los parámetros
     * @param symbole	symbole de la carte dont on veut afficher
     * @param couleur	couleur de la carte dont on veut afficher
     * @return	l'image de la carte demand�e
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( Symbole symbole, Couleur couleur ) throws SlickException { // exemple : passer-rouge
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        switch ( symbole ) {
        case INVERSER:
            return get( "reverse-" + nomCouleur );
        case JOKER:
            return get( symbole );
        case PASSER:
            return get( "skip-" + nomCouleur );
        case PLUS2:
            return get( "+2-" + nomCouleur );
        case PLUS4:
            return get( symbole );
        default:
            break;
        }
        return null;
    }

    /**
     * devuelve el sprite del mapa que tiene el valor y el color pasado en parámetros
     * @param valeur	valeur de la carte dont on veut afficher
     * @param couleur	couleur de la carte dont on veut afficher
     * @return	l'image de la carte demand�e
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image get( int valeur, Couleur couleur ) throws SlickException { // exemple : 0-bleu
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        return get( valeur + "-" + nomCouleur );
    }

    /*** ¡Devuelve un sprite/imagen gris transparente para agregar encima de los mapas no jugables!
     * @return	la sprite / image grise transparente � ajouter au dessus des cartes non jouables !
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getInactiveCard() throws SlickException {
        if ( !sprites.containsKey( "inactiveCard" ) )
            Debug.err( "getInactiveCard() null !" );
        return sprites.get( "inactiveCard" );
    }

    /**
     * devuelve el sprite/imagen del mapa oculto
     * @return	la sprite / image de la carte cach�e
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getHiddenCard() throws SlickException {
        if ( !sprites.containsKey( "hidden" ) )
            Debug.err( "getHiddenCard() null !" );
        return sprites.get( "hidden" );
    }

    /**
     * devuelve el sprite/imagen del fondo del juego (según el color pasado en el parámetro)
     * @param couleur	la couleur de l'arri�re-plan
     * @return	la sprite / image de l'arri�re-plan du jeu ( selon la couleur pass�e en param�tre )
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static Image getBackground( Couleur couleur ) throws SlickException {
        String nomCouleur = null;
        switch ( couleur ) {
        case ROUGE:
            nomCouleur = "red";
            break;
        case JAUNE:
            nomCouleur = "yellow";
            break;
        case VERT:
            nomCouleur = "green";
            break;
        case BLEU:
            nomCouleur = "blue";
            break;
        case NOIR:
            //
            break;
        }
        return get( "bg-" + nomCouleur );
    }

    /**
     * este método permite cargar todas las imágenes del juego
      * N.B.: TODAS las imágenes/sprites DEBEN cargarse desde el subproceso principal (subproceso GL)
      * de lo contrario tendremos la siguiente excepción: java.lang.RuntimeException: No se encontró contexto OpenGL en el hilo actual.
     * @throws SlickException	exception offerte par la librairie Slick2D
     */
    public static void load() throws SlickException {
        String colorNames[] = { "blue", "green", "red", "yellow" };
        String imageFileName;
        for ( String colorName : colorNames ) {
            // cartes chiffres : 0 .. 9
            for ( int i = 0; i <= 9; i++ ) {
                imageFileName = String.format( "%d-%s", i, colorName );
                sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            }
            // cartes +2
            imageFileName = String.format( "+2-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // cartes pasar
            imageFileName = String.format( "skip-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // cartes invertir
            imageFileName = String.format( "reverse-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
            // sprites / imágenes de fondo
            imageFileName = String.format( "bg-%s", colorName );
            sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        }
        // cartes joker
        imageFileName = "wild";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // cartes +4
        imageFileName = "+4";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // tarjeta oculta (el reverso de una tarjeta)
        imageFileName = "hidden";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
        // mapa no jugable (filtro gris transparente)
        imageFileName = "inactiveCard";
        sprites.put( imageFileName, new Image( Config.get( "imgPath" ) + imageFileName + ".png" ) );
    }
}
