package org.example.teamup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.teamup.API.AuthSession;
import org.example.teamup.API.ChatApiExample;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatListController {

    @FXML
    private VBox chatListContainer;

    @FXML
    private Button inicioBtn;

    @FXML
    private Button juegosBtn;


    @FXML
    public void initialize() {
        cargarChats();
    }

    private void cargarChats() {
        new Thread(() -> {
            try {
                String response = ChatApiExample.sendRequest(
                        "GET", "http://127.0.0.1:8000/api/listchats",
                        AuthSession.getToken(), null
                );
                JSONObject json = new JSONObject(response);
                JSONArray chats = json.getJSONArray("chats");

                int currentUserId = AuthSession.getUserId();
                Platform.runLater(() -> chatListContainer.getChildren().clear());

                for (int i = 0; i < chats.length(); i++) {
                    JSONObject chat = chats.getJSONObject(i);
                    JSONObject match = chat.getJSONObject("match_user");

                    JSONObject usuario1 = match.getJSONObject("usuario1");
                    JSONObject usuario2 = match.getJSONObject("usuario2");

                    int id1 = usuario1.getInt("id");
                    int id2 = usuario2.getInt("id");

                    // Log para depuración
                    System.out.println("Chat #" + chat.getInt("IDChat"));
                    System.out.println("Usuario1: " + usuario1.getString("Nombre") + " (id: " + id1 + ")");
                    System.out.println("Usuario2: " + usuario2.getString("Nombre") + " (id: " + id2 + ")");
                    System.out.println("Usuario actual (autenticado): " + currentUserId);

                    JSONObject otherUser;
                    if (id1 == currentUserId && id2 != currentUserId) {
                        otherUser = usuario2;
                    } else if (id2 == currentUserId && id1 != currentUserId) {
                        otherUser = usuario1;
                    } else {
                        System.out.println("⚠️ Error: el usuario autenticado no está en el match. Saltando chat.");
                        continue; // Por seguridad, salta este chat
                    }

                    String nombre = otherUser.getString("Nombre");
                    String foto = otherUser.optString("FotoPerfil", "");

                    JSONArray mensajes = chat.getJSONArray("mensajes");
                    String ultimoMensaje = mensajes.length() > 0
                            ? mensajes.getJSONObject(mensajes.length() - 1).optString("Texto", "")
                            : "";

                    Platform.runLater(() ->
                            agregarChatVisual(chat.getInt("IDChat"), nombre, foto, ultimoMensaje)
                    );
                }
            } catch (IOException e) {
                System.out.println("❌ Error al cargar la lista de chats: " + e.getMessage());
            }
        }).start();
    }



    private void agregarChatVisual(int idChat, String nombre, String fotoPerfil, String ultimoMensaje) {
        HBox chatItem = new HBox(15);
        chatItem.setStyle("-fx-background-color: #ffe6e6; -fx-background-radius: 10; -fx-padding: 10;"); // rojo claro + padding
        chatItem.setPrefHeight(80);
        chatItem.setMaxWidth(Double.MAX_VALUE);

        // Imagen circular
        ImageView imagen = new ImageView(new Image("http://localhost:8000/storage/" + fotoPerfil, true));
        imagen.setFitWidth(60);
        imagen.setFitHeight(60);
        imagen.setPreserveRatio(false);
        imagen.setStyle("-fx-background-radius: 30; -fx-border-radius: 30;");
        imagen.setClip(new javafx.scene.shape.Circle(30, 30, 30)); // Clip circular

        // Texto
        VBox textoBox = new VBox(5);
        Label nombreLabel = new Label(nombre);
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label mensajeLabel = new Label(ultimoMensaje);
        mensajeLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        textoBox.getChildren().addAll(nombreLabel, mensajeLabel);

        chatItem.getChildren().addAll(imagen, textoBox);

        chatItem.setOnMouseClicked(e -> abrirChat(idChat));

        chatListContainer.getChildren().add(chatItem);
    }

    @FXML
    private void irAInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) chatListContainer.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al ir a la vista de inicio: " + e.getMessage());
        }
    }

    @FXML
    private void irAJuegos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/juego-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) chatListContainer.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al ir a la vista de juegos: " + e.getMessage());
        }
    }
    private void abrirChat(int idChat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/chat-view.fxml"));
            Parent root = loader.load();
            ChatController controller = loader.getController();
            controller.setIdChat(idChat);

            Stage stage = (Stage) chatListContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/org/example/teamup/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al abrir el chat: " + e.getMessage());
        }
    }

}
