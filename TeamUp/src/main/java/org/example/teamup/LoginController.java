package org.example.teamup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Scene;

public class LoginController {
    @FXML
    private Label welcomeText;




    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Comprob");
    } //hacer el proceso de login
    protected void cambiarRegister() {
        Scene scene  = new Scene();

    }
}