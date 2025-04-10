package org.example.teamup;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.teamup.API.AuthApiExample;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//TODO ARREGLAR EL REGISTER DESPUES DE PONER NUEVO METODO PARA IMPORTAR ERRORES
public class RegisterController {

    private Stage stage ;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> regionField;
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
    @FXML
    private Label statusLabel;
    @FXML
    private Label welcomeText;

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
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String region = regionField.getValue();

        int edad;
        try {
            edad = Integer.parseInt(edadField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("La edad debe ser un número.");
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Llamada al método de registro que realiza la petición a la API
                    String response = AuthApiExample.register(nombre, email, password, confirmPassword, edad, region);
                    // Si el registro es exitoso, la API retornará un JSON con éxito
                    Platform.runLater(() -> statusLabel.setText("Registro exitoso: " + response));
                } catch (IOException ex) {
                    // Si se recibe un error, se parsea el contenido JSON para extraer los mensajes
                    String errorResponse = AuthApiExample.getResponseError();
                    String errorMessages = parseErrorMessages(errorResponse);
                    Platform.runLater(() -> statusLabel.setText(errorMessages));
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * Método para parsear el JSON de errores y devolver un mensaje concatenado.
     * @param jsonResponse La cadena JSON de la respuesta de error.
     * @return Los mensajes de error concatenados.
     */
    private String parseErrorMessages(String jsonResponse) {
        StringBuilder messages = new StringBuilder();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject.has("errors")) {
            JSONObject errors = jsonObject.getJSONObject("errors");
            for (String field : errors.keySet()) {
                JSONArray errorArray = errors.getJSONArray(field);
                for (int i = 0; i < errorArray.length(); i++) {
                    messages.append(errorArray.getString(i)).append("\n");
                }
            }
        }
        return messages.toString();
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Comprob");
    }

}
