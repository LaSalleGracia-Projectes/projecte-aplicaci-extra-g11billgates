package org.example.teamup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void initialize() {
        cargarChats();
    }

    private void cargarChats() {
        new Thread(() -> {
            try {
                String response = ChatApiExample.sendRequest("GET", "http://127.0.0.1:8000/api/listchats", AuthSession.getToken(), null);
                JSONObject json = new JSONObject(response);
                JSONArray chats = json.getJSONArray("chats");

                Platform.runLater(() -> chatListContainer.getChildren().clear());

                for (int i = 0; i < chats.length(); i++) {
                    JSONObject chat = chats.getJSONObject(i);
                    JSONObject match = chat.getJSONObject("match_user");
                    JSONObject otherUser = match.getInt("IDUsuario1") == AuthSession.getUserId()
                            ? match.getJSONObject("usuario2")
                            : match.getJSONObject("usuario1");

                    String nombre = otherUser.getString("Nombre");
                    String foto = otherUser.getString("FotoPerfil");

                    JSONArray mensajes = chat.getJSONArray("mensajes");
                    String ultimoMensaje = mensajes.length() > 0
                            ? mensajes.getJSONObject(mensajes.length() - 1).optString("Texto", "")
                            : "";

                    Platform.runLater(() -> agregarChatVisual(chat.getInt("IDChat"), nombre, foto, ultimoMensaje));
                }
            } catch (IOException e) {
                System.out.println("Error al cargar la lista de chats: " + e.getMessage());
            }
        }).start();
    }

    private void agregarChatVisual(int idChat, String nombre, String fotoPerfil, String ultimoMensaje) {
        HBox chatItem = new HBox(10);
        chatItem.getStyleClass().add("chat-item");

        ImageView imagen = new ImageView(new Image("http://localhost:8000/storage/" + fotoPerfil));
        imagen.setFitWidth(50);
        imagen.setFitHeight(50);
        imagen.setPreserveRatio(true);

        VBox textoBox = new VBox();
        Label nombreLabel = new Label(nombre);
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label mensajeLabel = new Label(ultimoMensaje);
        mensajeLabel.setStyle("-fx-text-fill: lightgray; -fx-font-size: 12px;");

        textoBox.getChildren().addAll(nombreLabel, mensajeLabel);
        chatItem.getChildren().addAll(imagen, textoBox);

        chatItem.setOnMouseClicked(e -> abrirChat(idChat));

        chatListContainer.getChildren().add(chatItem);
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
