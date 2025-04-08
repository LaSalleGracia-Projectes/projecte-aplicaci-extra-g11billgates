package org.example.teamup;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.example.teamup.API.AuthApiExample;


public class RegisterController {
    private Stage stage ;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> regionField;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField edadField;


    public void initialize(){
        regionField.getItems().addAll("Europa", "Norteamérica", "Sudamerica", "Asia", "Africa", "Oceania");
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void selectRegion(ActionEvent event) {
        String selected = regionField.getValue();
    }
    @FXML
    public void handleRegister(ActionEvent event) {
        // Recoger valores de los controles
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String region = regionField.getValue();
        // Convertir la edad a entero y validar que sea un número
        int edad;
        try {
            edad = Integer.parseInt(edadField.getText());
        } catch (NumberFormatException e) {
            welcomeText.setText("La edad debe ser un número.");
            return;
        }

        //Ejecutar la llamada a la API en un hilo aparte
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                try{
                    AuthApiExample.register(nombre, email, password, confirmPassword, edad, region);
                    // Actualización de la interfaz en el hilo de la UI
                    Platform.runLater(() -> welcomeText.setText("Registro exitoso"));

                } catch (IOException ex) {
                    String errorResponse = AuthApiExample.get;
                    Platform.runLater(() -> welcomeText.setText("Error en el registro"));
                }
                return null;
            }
        };
        new Thread(task).start();
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Comprob");
    } //hacer el proceso de login

}
