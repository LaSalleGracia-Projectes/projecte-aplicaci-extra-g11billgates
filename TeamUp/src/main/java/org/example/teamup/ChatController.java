package org.example.teamup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.teamup.API.AuthSession;
import org.example.teamup.API.ChatApiExample;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatController {

    @FXML
    private VBox mensajesContainer;
    @FXML
    private TextArea inputMensaje;
    @FXML
    private Button enviarBtn;
    @FXML
    private Button volverBtn;

    private int idChat;

    public void setIdChat(int idChat) {
        this.idChat = idChat;
        cargarMensajes();
    }

    @FXML
    public void initialize() {
        enviarBtn.setOnAction(e -> enviarMensaje());
        volverBtn.setOnAction(e -> volverInicio(e));
    }

    private void cargarMensajes() {
        new Thread(() -> {
            try {
                String response = ChatApiExample.obtenerMensajesPorChat(idChat, AuthSession.getToken());
                JSONObject json = new JSONObject(response);
                JSONArray mensajes = json.getJSONArray("mensajes");

                Platform.runLater(() -> {
                    mensajesContainer.getChildren().clear();
                    for (int i = 0; i < mensajes.length(); i++) {
                        JSONObject mensaje = mensajes.getJSONObject(i);
                        String texto = mensaje.optString("Texto", "[Sin texto]");
                        Label label = new Label(texto);
                        mensajesContainer.getChildren().add(label);
                    }
                });

            } catch (IOException e) {
                System.out.println("Error cargando mensajes: " + e.getMessage());
            }
        }).start();
    }

    private void enviarMensaje() {
        String texto = inputMensaje.getText();
        if (texto == null || texto.trim().isEmpty()) return;

        new Thread(() -> {
            try {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                ChatApiExample.enviarMensaje(idChat, "texto", fecha, texto, AuthSession.getToken());

                Platform.runLater(() -> {
                    inputMensaje.clear();
                    cargarMensajes();
                });
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje: " + e.getMessage());
            }
        }).start();
    }

    private void volverInicio(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al volver al inicio: " + e.getMessage());
        }
    }
}
