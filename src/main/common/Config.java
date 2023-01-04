package main.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Config {

    /**
     * nombre del archivo de configuración (archivo .ini)
     */
    private static final String                  configFilePath = "config.ini";
    /**
     * contiene la configuración por defecto del juego
     */
    private static final Properties              defaults       = new Properties();

    private static final HashMap<String, String> values         = new HashMap<>();
    /**
     * bandera utilizada para garantizar que el método init() se invoque solo una vez
     */
    private static boolean                       isInitialized  = false;
    /**
     * Objeto utilizado para leer (analizar) el archivo de configuración
      */
    static Properties                            p              = new Properties();

    /**
     * método de inicialización, inicializar valores predeterminados
     */
    private static void init() {
        if ( isInitialized ) { 
            return; 
        }
        isInitialized = true; // en caso contrario marcamos que se invoque para no invocarlo una 2da vez
        // las configuraciones predeterminadas (útil cuando las configuraciones no se encuentran en el archivo)
        defaults.put( "nbJoueurs", "2" );
     
        defaults.put( "title", "UNO" );
        defaults.put( "imgPath", "res/gfx/" );
        defaults.put( "soundPath", "res/sons/" );
        defaults.put( "offsetPiocheTalon", "50" );

        defaults.put( "jouerMusique", "true" );
        defaults.put( "jouerSons", "true" );

        // Sonidos
        defaults.put( "clickSound", "cardClicked.wav" );
        defaults.put( "invalidClickSound", "invalidCardClicked.wav" );
        defaults.put( "unoSound", "uno.wav" );
        defaults.put( "skipSound", "passe.wav" );
        defaults.put( "reverseSound", "reverse.wav" );
        defaults.put( "plus2Sound", "+2.wav" );
        defaults.put( "wildSound", "wild.wav" );
        defaults.put( "hardLuckSound", "hardLuck.wav" );
        defaults.put( "noPlayableCardsSound", "noPlayableCards.wav" );
        defaults.put( "plus4Sound", "+4.wav" );
        defaults.put( "winSound", "win.wav" );
        // musica
        defaults.put( "bgMusic", "bgMusic.wav" );
    }

    /**
     * leer (analizar) el archivo de configuración
     */
    private static void readConfigFile() {
        try {
            init(); // Inicializacion
            InputStream is = new FileInputStream( configFilePath );
            p.load( is );
            is.close();
        } catch ( IOException e ) {
            Debug.err( "Fichier introuvable : " + configFilePath );
        }
    }

    /**
     * utilizado para obtener un valor del archivo de configuración
     * @param nombre clave del parámetro solicitado
     * @return el valor del parámetro solicitado
     */
    public static String get( String key ) {
        return values.get( key );
    }

    /**
     * te permite cargar configuraciones para usar en el juego
     */
    public static void load() {
        try {
            readConfigFile(); // leyendo el archivo
            // carga las configuraciones desde el archivo
            Enumeration<?> e = p.propertyNames();
            while ( e.hasMoreElements() ) {
                String key = (String) e.nextElement();
                values.put( key, p.getProperty( key ) );
            }
         // si faltan configuraciones ponemos las configuraciones por defecto
            e = defaults.propertyNames();
            while ( e.hasMoreElements() ) {
                String key = (String) e.nextElement();
                if ( !values.containsKey( key ) ) { // si el nombre del parámetro no se encuentra en la lista de configuraciones...
                	// ... lo agregamos
                    values.put( key, defaults.getProperty( key ) );
                }
            }
         // guardar configuraciones en el archivo
            OutputStream os = new FileOutputStream( configFilePath );
            p.store( os, null );
        } catch ( IOException e ) {
            Debug.err( "Archivo no encontrado: " + configFilePath );
        }
    }
}
