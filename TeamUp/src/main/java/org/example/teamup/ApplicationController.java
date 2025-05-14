package org.example.teamup;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import org.example.teamup.API.AuthApiExample;

import java.io.IOException;


public class ApplicationController {
    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private Label statusLabel;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
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

            // Aquí añadimos otra vez los estilos
            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    AuthApiExample.login(email, password);

                    // Cambio a MainView después de login exitoso
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                            Parent root = loader.load();
                            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            scene = new Scene(root);

                            // Añadir los estilos de nuevo
                            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
                            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException ex) {
                    Platform.runLater(() -> statusLabel.setText("Error en login: " + ex.getMessage()));
                }
                return null;
            }
        };
        new Thread(task).start();
    }


}