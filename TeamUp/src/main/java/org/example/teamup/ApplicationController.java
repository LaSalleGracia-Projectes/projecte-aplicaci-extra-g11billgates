package org.example.teamup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeText;
    @FXML
    private TextField emailField;
    //@FXML
    //private PasswordField passwordField;
    @FXML
    private Hyperlink enlace_register;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Comprob");
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}