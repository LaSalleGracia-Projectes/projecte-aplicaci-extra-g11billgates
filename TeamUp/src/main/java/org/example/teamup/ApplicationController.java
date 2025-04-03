package org.example.teamup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> region;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Comprob");
    } //hacer el proceso de login



    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.show();
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        region.getItems().addAll("Europa", "Norteamérica", "Sudamerica", "Asia", "Africa", "Oceania");
        stage.show();
    }

    @FXML
    private void selectRegion(ActionEvent event) {
        String selected = region.getValue();
    }
}