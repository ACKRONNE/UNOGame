package main.gameObjects;

import java.util.Random;

/**
 *esta clase presenta las características comunes entre el pico y el talón
 *
 */
public class Pile extends Main {

    /**
     * generador de valores aleatorios
     */
    protected Random rand = new Random();

    /**
     *aparece la parte superior de la pila
     * @return la carte supprim�e
     */
    public Carte depiler() {
        return cartes.remove( 0 );
    }

    /**
     * te permite apilar una carta
     * @param carte : la carte � empiler
     */
    public void empiler( Carte carte ) {
        cartes.add( 0, carte );
    }

    /**
     * permite barajar la pila usando el algoritmo: Fisher-Yates shuffle
     * @see https://fr.wikipedia.org/wiki/M%C3%A9lange_de_Fisher-Yates
     * académicamente hablando, ¡no puedes mezclar una pila! pero en este contexto, la pila no es
     * no es una estructura FIFO (primero en entrar, primero en salir) ordinaria, podemos mezclarla
      * este es un método común entre las clases Pickaxe y Talon
     */
    public void melanger() {
        for ( int i = cartes.size() - 1; i > 0; i-- ) {
        	// Intercambiar una tarjeta aleatoria entre la primera 
        	// y la última tarjeta en el ciclo
            int pick = rand.nextInt( i ); // entero aleatorio entre 0 y i - 1
            Carte randCard = cartes.get( pick );
            Carte lastCard = cartes.get( i );
            cartes.set( i, randCard );
            cartes.set( pick, lastCard );
        }
    }

    /**
     * @return una cadena que representa la pila actual
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return la parte superior de la pila
     */
    public Carte sommet() {
        if ( cartes.isEmpty() ) {
            return null;
        }
        return cartes.get( 0 ); // Devuelve la parte superior de la pila (sin eliminarla)
    }

}
