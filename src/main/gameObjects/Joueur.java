package main.gameObjects;

import java.util.ArrayList;

import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.Game;
import main.gfx.Sprite;
import main.io.Audio;

/**
 * esta clase representa al jugador: lo que tiene y lo que puede hacer, sus propiedades y sus acciones
 * @author Stoufa
 *
 */
public abstract class Joueur extends GameObject {

    /**
     * la mano del jugador que contiene todas sus cartas
     */
    public Main     main;
    /**
     * el apodo del jugador
     */
    public String   pseudo;
    /**
     * la pila de sorteo: la pila de cartas donde el jugador puede tomar cartas si no tiene cartas jugables
     */
    public Pioche   pioche;
    /**
     * la pila de reserva: la pila de cartas donde los jugadores colocan sus cartas
     */
    public Talon    talon;
    /**
     * la posición del jugador en la pantalla: arriba, abajo, derecha, izquierda
     */
    public Position position;
    /**
     * identificación del jugador
     */
    public int      id;
    /**
     * la carta jugada
     */
    public Carte    playedCard;

    /**
     * contructeur
     * @param pseudo : apodo del jugador
     * @param pioche : La pila
     * @param talon : El talon
     */
    public Joueur( String pseudo, Pioche pioche, Talon talon ) {
        this.pseudo = pseudo;
        this.pioche = pioche;
        this.talon = talon;
        main = new Main();
    }

    /**
     * le permite tomar una carta de la pila de robo y agregarla a la mano del jugador
     * @return la carta dibujada
     */
    public Carte prendreCarte() {
        if ( pioche.nbCartes() == 0 ) { // ¡la pila de sorteo está vacía!
            // TODO : puedes agregar una animación aquí
            System.out.println( "¡La pila de sorteo está vacía!" );
            // en este caso, debemos usar el heel (excepto el top) para poblarlo nuevamente
            Carte sommetTalon = talon.depiler();
            while ( talon.nbCartes() != 0 ) { // este lazo vaciará el talón en el pico
                pioche.empiler( talon.depiler() );
            }
            pioche.melanger();
            talon.empiler( sommetTalon ); // volver a colocar la parte superior del talón
        }
        //Debug.log("pioche: " + pioche.nbCartes());
        Carte carte = pioche.depiler();
        main.ajouter( carte );
        return carte;
    }

    /**
     * este método muestra las cartas en la mano del jugador actual
      * 2 casos posibles:
      *(1) número de cartas pares: tenemos n cartas, n/2 se rotan por t, 2t, 3t,...
      * y los n/2 hacen rotaciones de -t, -2t, -3t, ... con t el ángulo de rotación del cual dependerá
      * el número de cartas en la mano
      * (2) número impar de cartas: tenemos n + 1 cartas, la carta del medio se queda como está sin rotación
      * las n/2 cartas a su derecha sufren rotaciones de t, 2t, 3t, ..., (n/2)t
      * y las n/2 cartas a su izquierda sufren rotaciones de -t, -2t, -3t, ...., -(n/2)t
     * @throws SlickException 
     */
    public void afficherMain( Graphics g ) throws SlickException {
        ArrayList<Carte> cartes = main.getCartes();
        int indiceCarteMilieu = cartes.size() / 2;

     // el dibujo / representación de las cartas debe ser en una dirección fija (de izquierda a derecha) 
     // usar la dirección opuesta en la detección de clic

        float coefficientAngle;
        Carte carte;

        float initialX = JoueurPainter.getStartPosX( position ), initialY = JoueurPainter.getStartPosY( position );
        // TODO : el ángulo y el desplazamiento deben ser proporcionales al número de tarjetas
        float angle = 10;
        float offset = 30;
        if ( cartes.size() < 10 ) {
            angle = 10;
            offset = 30;
        } else {
            angle = 5;
            offset = 20;
        }

        //Image oldCardImage = null;
        Image hiddenCardImg = Sprite.getHiddenCard();

        if ( cartes.size() % 2 == 0 ) { // nombre de cartes paire (1)

            int x = indiceCarteMilieu - 1;
            coefficientAngle = -( 1 / 2 + x );
            /**
             * exemple : 4 cartes
             * indiceCarteMilieu	=	2
             * x					=	1		0		-1		-2
             * coefficientAngle		=	-3/2	-1/2	1/2		3/2
             */

            /**
             * Rendering				0	-> n - 1
             * Checking click events	n-1	-> 0
             */
            for ( int i = 0; i < cartes.size(); i++ ) {
                // [ 0 .. indiceCarteMilieu - 1 ] les cartes � gauche
                // [ indiceCarteMilieu .. cartes.size() - 1 ] les cartes � droite

                carte = cartes.get( i ); // so we don't have a nullPointerException !
                if ( carte == null ) {
                    continue;
                } // to prevent unpredicted nullpointer exceptions !
                if ( JoueurPainter.getOffsetAxis( position ) == 'x' ) {
                    carte.x = initialX + coefficientAngle * offset;
                    carte.y = initialY;
                } else { // y
                    carte.x = initialX;
                    carte.y = initialY + coefficientAngle * offset;
                }

                carte.jouable = carte.compatible( talon.sommet() );

                if ( this instanceof Humain ) {
                    //if (id == Jeu.tour) {

                    carte.rotate( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    carte.render( g );

                } else {

                    hiddenCardImg.setRotation( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( carte.x, carte.y );

                }

                x--;
                coefficientAngle = -( 1 / 2 + x );
            }

        } else { // nombre de cartes impaire (2)

            coefficientAngle = -indiceCarteMilieu;

            //for (int i = 0; i < indiceCarteMilieu; i++) {	// les cartes � gauche
            for ( int i = 0; i < cartes.size(); i++ ) {

                carte = cartes.get( i );
                if ( JoueurPainter.getOffsetAxis( position ) == 'x' ) {
                    carte.x = initialX + coefficientAngle * offset;
                    carte.y = initialY;
                } else { // y
                    carte.x = initialX;
                    carte.y = initialY + coefficientAngle * offset;
                }

                carte.jouable = carte.compatible( talon.sommet() );

                if ( this instanceof Humain ) {
                    //if (id == Jeu.tour) {

                    carte.rotate( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    carte.render( g );

                } else {

                    hiddenCardImg.setRotation( JoueurPainter.getAngleCorrection( position ) * coefficientAngle * angle
                            + JoueurPainter.getAdditionalRotation( position ) );
                    hiddenCardImg.draw( carte.x, carte.y );

                }

                coefficientAngle++;
            }

            

        }
    }

    /**
     * a diferencia de nb Playable Cards(), esta función debe ser pública * otros jugadores pueden ver cuántas cartas tienes en la mano
     * @return el número de cartas que el jugador tiene en su mano
     */
    public int nbCartes() {
        return main.nbCartes();
    }

    /**
     * este método muestra las cartas en la mano del jugador actual
     */
    public void afficherMain() {
        String str = "";
        ArrayList<Carte> cartes = main.getCartes();
        if ( cartes.isEmpty() ) {
            str = "[VIDE]";
        }
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            str = str + i + ") " + carte.toString();
            if ( carte.compatible( talon.sommet() ) ) {
                str = str + " [Jouable]";
            }
            if ( i != cartes.size() - 1 ) { // if this isn't the last iteration
                str = str + "\n"; // add a new line
            }
        }
        System.out.println( str );
    }

    /**
     * este método debe ser tomado! ¡solo el jugador debe saber cuántas cartas jugables tiene!
     * @return le nombre de cartes jouables ��d : compatibles avec le sommet du talon 
     */
    protected int nbCartesJouables() {
        int n = 0;
        ArrayList<Carte> cartes = main.getCartes();
        if ( cartes.isEmpty() ) {
            return 0;
        }
        Carte sommetTalon = talon.sommet();
        for ( int i = 0; i < cartes.size(); ++i ) {
            Carte carte = cartes.get( i );
            if ( carte.compatible( sommetTalon ) ) {
                n++;
            }
        }
        return n;
    }

    /**
     * le permite esperar un poco a que el usuario pueda leer el mensaje mostrado
     */
    private void pause() {
        try {
            Thread.sleep( 3000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    /**
     * permite que el jugador tome su turno
     * @throws InterruptedException 
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     * @throws SlickException 
     */
    public void jouerTour() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException { // TODO : agregue el caso donde hay duplicados! ¡Debemos deshacernos de todas las ocurrencias de la carta jugada!
        playedCard = null;
        talon.afficherSommet();
        afficherMain(); // en la consola
        if ( nbCartesJouables() == 0 ) {
            System.out.println( "¡No tienes cartas jugables! debes dibujar!" );
            Audio.playSound( "noPlayableCardsSound" );
            pause();
            Carte c = prendreCarte();
            System.out.println( "La carte pioch�e est : " + c );
            if ( !c.compatible( talon.sommet() ) ) { // la tarjeta extraída recientemente no es compatible con la parte superior de la acción
                System.out.println(
                        "Sin suerte ! todavía no tienes ninguna carta jugable, tienes que pasar el turno" );
                System.out.println( "----------------------------------" );
                Audio.playSound( "hardLuckSound" );
                return; // saltar <=> método de salida
            } else {
                afficherMain(); //en la consola
            }
        }
        jouerCarte();
        System.out.println( "----------------------------------" );
    }

    public abstract void jouerCarte() throws InterruptedException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException, SlickException;

    @Override
    public void render( Graphics g ) throws SlickException {
        this.afficherMain( g );
        //mostrar el nombre del jugador
        int xPos, yPos, angleRotation;
        final int offsetHautBas = 50, offsetDroiteGauche = 50;
        String playerName = this.pseudo + " (" + nbCartes() + ")";

        Color oldColor = g.getColor();
        int tour = Jeu.tour; // ¡para asegurarse de que el valor de Game.turn no haya cambiado!
        if ( id == tour ) {
            // si es el jugador actual, cambiamos el color de su nombre para indicar la dirección del juego
            //oldColor = g.getColor();
            g.setColor( Color.black );
        }
        switch ( position ) {
        case BAS:
            g.drawString( playerName,
                    Game.WIDTH / 2 - g.getFont().getWidth( playerName ) / 2,
                    Game.HEIGHT - Carte.HEIGHT - offsetHautBas );
            break;
        case GAUCHE:
            xPos = Carte.HEIGHT + offsetDroiteGauche;
            yPos = Game.HEIGHT / 2 - g.getFont().getWidth( playerName ) / 2;
            angleRotation = 90;

            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        case HAUT:
            xPos = Game.WIDTH / 2 + g.getFont().getWidth( playerName ) / 2;
            yPos = Carte.HEIGHT + offsetHautBas;
            angleRotation = 180;
            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        case DROITE:
            xPos = Game.WIDTH - Carte.HEIGHT - offsetDroiteGauche;
            yPos = Game.HEIGHT / 2 + g.getFont().getWidth( playerName ) / 2; // -1/2 textWidth + textWidth
            angleRotation = 270;
            g.rotate( xPos, yPos, angleRotation ); // rotating
            g.drawString( playerName,
                    xPos,
                    yPos );
            g.rotate( xPos, yPos, -angleRotation ); // reset rotation
            break;
        default:
            break;
        }
        if ( id == tour ) {
            // si es el jugador actual, cambiamos el color de su nombre para indicar la dirección del juego
            g.setColor( oldColor );
        }
    }

}

/**
 *esta clase se usará como una <<Utilidad>> para ayudar 
 * a la clase Player a dibujar/representar al jugador
 * @author Stoufa
 *
 */
class JoueurPainter {

    public static final int offsetHautBas = 10, offsetDroiteGauche = 10;

    public static float getStartPosX( Position position ) {
        switch ( position ) {
        case BAS:
            return Game.WIDTH / 2 - Carte.WIDTH / 2;
        case DROITE:
            return Game.WIDTH - Carte.HEIGHT + offsetDroiteGauche;
        case HAUT:
            return Game.WIDTH / 2 - Carte.WIDTH / 2;
        case GAUCHE:
            return 25 + offsetDroiteGauche; // 25 para no contar los bordes de la ventana
        }
        return 0;
    }

    public static float getStartPosY( Position position ) {
        switch ( position ) {
        case BAS:
            return Game.HEIGHT - Carte.HEIGHT - offsetHautBas;
        case DROITE:
            return Game.HEIGHT / 2 - Carte.WIDTH / 2;
        case HAUT:
            //return 0 - Carte.WIDTH / 2;
            return offsetHautBas;
        case GAUCHE:
            return Game.HEIGHT / 2 - Carte.WIDTH / 2;
        }
        return 0;
    }

    public static float getAdditionalRotation( Position position ) {
        switch ( position ) {
        case BAS:
            return 0;
        case DROITE:
            return -90;
        case HAUT:
            return 180;
        case GAUCHE:
            return 90;
        }
        return 0;
    }

    public static char getOffsetAxis( Position position ) {
        switch ( position ) {
        case BAS:
        case HAUT:
            return 'x';
        case DROITE:
        case GAUCHE:
            return 'y';
        }
        return 0;
    }

    public static int getAngleCorrection( Position position ) {
        switch ( position ) {
        case BAS:
        case GAUCHE:
            return 1;
        case HAUT:
        case DROITE:
            return -1;
        }
        return 0;
    }
}
