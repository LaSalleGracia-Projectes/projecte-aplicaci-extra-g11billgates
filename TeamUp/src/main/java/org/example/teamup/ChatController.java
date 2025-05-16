package org.example.teamup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    private Button inicioBtn;
    @FXML
    private Button juegosBtn;
    @FXML
    private Button chatsBtn;
    @FXML
    private VBox mensajesContainer;
    @FXML
    private TextField mensajeField;
    @FXML
    private Button enviarBtn;
    @FXML
    private ScrollPane scrollPane;



    private int idChat;

    public void setIdChat(int idChat) {
        this.idChat = idChat;
        cargarMensajes();
    }

    @FXML
    public void initialize() {
        System.out.println("ChatController inicializado con idChat = " + idChat);
        enviarBtn.setOnAction(e -> enviarMensaje());
    }

    private void cargarMensajes() {
        new Thread(() -> {
            try {
                String response = ChatApiExample.obtenerMensajesPorChat(idChat, AuthSession.getToken());
                JSONObject json = new JSONObject(response);
                JSONArray mensajes = json.getJSONArray("mensajes");

                Platform.runLater(() -> {
                    mensajesContainer.getChildren().clear();
                    int currentUserId = AuthSession.getUserId();

                    for (int i = 0; i < mensajes.length(); i++) {
                        JSONObject mensaje = mensajes.getJSONObject(i);
                        String texto = mensaje.optString("Texto", "[Sin texto]");
                        int idRemitente = mensaje.optInt("IDUsuario", -1);

                        // Crear el Label del mensaje
                        Label label = new Label(texto);
                        label.setWrapText(true);
                        label.setPadding(new Insets(8));
                        label.setStyle("-fx-background-radius: 12; -fx-font-size: 13px;");
                        label.setMaxWidth(300);

                        // Contenedor del mensaje
                        HBox contenedorMensaje = new HBox(label);
                        contenedorMensaje.setPadding(new Insets(5, 10, 5, 10));
                        contenedorMensaje.setMaxWidth(Double.MAX_VALUE);

                        if (idRemitente == currentUserId) {
                            contenedorMensaje.setAlignment(Pos.CENTER_RIGHT);
                            label.setStyle(label.getStyle() + "-fx-background-color: #cce5ff; -fx-text-fill: black;");
                        } else {
                            contenedorMensaje.setAlignment(Pos.CENTER_LEFT);
                            label.setStyle(label.getStyle() + "-fx-background-color: #eeeeee; -fx-text-fill: black;");
                        }

                        mensajesContainer.getChildren().add(contenedorMensaje);
                    }

                    // Auto scroll al final
                    scrollPane.layout();
                    scrollPane.setVvalue(1.0);
                });



            } catch (IOException e) {
                System.out.println("Error cargando mensajes: " + e.getMessage());
            }
        }).start();
    }



    private void enviarMensaje() {
        String texto = mensajeField.getText();
        if (texto == null || texto.trim().isEmpty()) return;

        new Thread(() -> {
            try {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                ChatApiExample.enviarMensaje(idChat, "texto", fecha, texto, AuthSession.getToken());

                Platform.runLater(() -> {
                    mensajeField.clear();
                    cargarMensajes();
                });
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void irAInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/main-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) inicioBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al ir a Inicio: " + e.getMessage());
        }
    }

    @FXML
    private void irAJuegos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/juego-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) juegosBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al ir a Juegos: " + e.getMessage());
        }
    }

    @FXML
    private void irAListaDeChats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/chat-list-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) chatsBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al ir a la lista de chats: " + e.getMessage());
        }
    }



}
