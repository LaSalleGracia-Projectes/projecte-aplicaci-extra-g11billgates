package org.example.teamup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML
    private Label mainText;

    /**
     * Método inicial para configurar el texto de la vista principal.
     */
    @FXML
    public void initialize() {
        mainText.setText("Vista Principal");
    }
}
