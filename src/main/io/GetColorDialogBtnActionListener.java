package main.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GetColorDialogBtnActionListener implements ActionListener {

    /**
     * le nom de la couleur choisie
     */
    String         couleur;
    /**
     * r�f�rence sur la fen�tre associ�e
     */
    GetColorDialog dialog;

    /**
     * constructeur
     * @param dialog
     * @param couleur
     */
    public GetColorDialogBtnActionListener( GetColorDialog dialog, String couleur ) {
        this.dialog = dialog;
        this.couleur = couleur;
    }

    /**
     * cette m�thode contient le code � �x�cuter lors d'un clique
     */
    @Override
    public void actionPerformed( ActionEvent e ) {
        dialog.selectedColor = couleur; // sauvegarder la couleur choisie
        System.out.println( "Color seleccionado = " + couleur );
        dialog.dispose(); // fermer la fen�tre
    }
}
