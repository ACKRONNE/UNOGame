package main;

import org.newdawn.slick.state.StateBasedGame;

import main.gameObjects.Jeu;

public class GameRunnable implements Runnable {
    /**
     * objeto que representa el juego
     */
    Jeu            jeu = null;
    /**
     * con este ítem puedes cambiar de un estado a otro
     */
    StateBasedGame sbg = null;

    /**
     * constructeur
     * @param jeu
     * @param sbg
     */
    public GameRunnable( Jeu jeu, StateBasedGame sbg ) {
        this.jeu = jeu;
        this.sbg = sbg;
    }

    /**
     *el código a ejecutar por el Thread
     */
    @Override
    public void run() {
        try {
            jeu.lancer( sbg ); // lancer le jeu
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
