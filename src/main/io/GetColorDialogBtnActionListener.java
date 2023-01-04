package main.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GetColorDialogBtnActionListener implements ActionListener {

    /**
     * el nombre del color elegido
     */
    String         couleur;
    /**
     *referencia en la ventana asociada
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
     * este método contiene el código para ejecutar al hacer clic
     */
    @Override
    public void actionPerformed( ActionEvent e ) {
        dialog.selectedColor = couleur; // guardar el color elegido
        System.out.println( "Color seleccionado = " + couleur );
        dialog.dispose(); // cerrar la ventana
    }
}
