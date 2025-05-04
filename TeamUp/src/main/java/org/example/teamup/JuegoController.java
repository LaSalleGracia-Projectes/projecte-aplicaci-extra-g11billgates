package org.example.teamup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.teamup.API.AuthSession;
import org.example.teamup.API.JuegoApiExample;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


import javafx.event.ActionEvent;
import java.io.IOException;

public class JuegoController {

    private final Map<String, Integer> juegosMap = new HashMap<>();

    @FXML
    private VBox juegosContainer;
    @FXML
    private Button guardarBtn;
    @FXML
    private Button inicioBtn;

    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        cargarJuegos();

        guardarBtn.setOnAction(event -> guardarJuegoSeleccionado());
        inicioBtn.setOnAction(event -> {
            try {
                irAInicio(event);
            } catch (IOException e) {
                System.out.println("Error al volver al inicio: " + e.getMessage());
            }
        });
    }

    private void cargarJuegos() {
        new Thread(() -> {
            try {
                String token = AuthSession.getToken();
                String jsonResponse = JuegoApiExample.obtenerTodosLosJuegos(token);
                JSONArray juegos = new JSONArray(jsonResponse);

                for (int i = 0; i < juegos.length(); i++) {
                    JSONObject juego = juegos.getJSONObject(i);
                    String nombre = juego.getString("NombreJuego");
                    int id = juego.getInt("IDJuego");

                    juegosMap.put(nombre, id); // Guardar nombre -> id

                    RadioButton rb = new RadioButton(nombre);
                    rb.setUserData(id);
                    rb.setToggleGroup(toggleGroup);

                    javafx.application.Platform.runLater(() -> juegosContainer.getChildren().add(rb));
                }
            } catch (IOException e) {
                System.out.println("Error al cargar juegos: " + e.getMessage());
            }
        }).start();
    }


    private void guardarJuegoSeleccionado() {
        RadioButton seleccionado = (RadioButton) toggleGroup.getSelectedToggle();
        if (seleccionado == null) return;

        int idJuego = (int) seleccionado.getUserData();

        new Thread(() -> {
            try {
                // Intentar agregar el juego primero
                JuegoApiExample.addJuego(idJuego, AuthSession.getToken());
                System.out.println("Juego agregado: " + idJuego);
            } catch (IOException addEx) {
                // Si el juego ya estaba agregado, lo eliminamos
                try {
                    JuegoApiExample.removeJuego(idJuego, AuthSession.getToken());
                    System.out.println("Juego eliminado: " + idJuego);
                } catch (IOException deleteEx) {
                    System.out.println("Error al agregar/eliminar juego: " + deleteEx.getMessage());
                }
            }
        }).start();
    }

    @FXML
    public void irAInicio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/MainView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Añadir estilos
        scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }


}