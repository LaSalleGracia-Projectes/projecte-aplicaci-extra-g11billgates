package org.example.teamup;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.teamup.API.ChatApiExample;
import org.example.teamup.API.MatchApiExample;
import org.example.teamup.API.UserApiExample;
import org.example.teamup.models.UsuarioDTO;
import org.example.teamup.API.AuthSession;

import java.io.IOException;

public class MainViewController {

    @FXML
    private Button dislikeButton;
    @FXML
    private Button juegosButton;
    @FXML
    private ImageView bienvenidaImage;
    @FXML
    private Button likeButton;
    @FXML
    private VBox imageContainer;
    @FXML
    private VBox textContainer;
    @FXML
    private Label tituloLabel;
    @FXML
    private Button chatsButton;



    private UsuarioDTO usuario;
    private final String token = AuthSession.getToken();

    @FXML
    public void initialize() {
        ajustarLayout();
        cargarUsuarioAleatorio();
        likeButton.setOnAction(event -> handleLikeAndCheckMatch());
        juegosButton.setOnAction(event -> irAJuegos());
        chatsButton.setOnAction(event -> irAChats());


    }

    private void ajustarLayout() {
        imageContainer.setMaxWidth(Double.MAX_VALUE);
        textContainer.setMaxWidth(Double.MAX_VALUE);

        imageContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            imageContainer.setPrefWidth(imageContainer.getParent().getLayoutBounds().getWidth() * 0.6);
        });

        textContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            textContainer.setPrefWidth(textContainer.getParent().getLayoutBounds().getWidth() * 0.4);
        });
    }

    private void cargarUsuarioAleatorio() {
        new Thread(() -> {
            try {
                int randomId = UserApiExample.getRandomUserId(token);
                String userJson = UserApiExample.getUserById(randomId, token);
                Gson gson = new Gson();
                usuario = gson.fromJson(userJson, UsuarioDTO.class);

                Platform.runLater(() -> {
                    tituloLabel.setText(usuario.Nombre + " " + usuario.Edad);
                    String fotoUrl = "http://localhost:8000/storage/" + usuario.FotoPerfil;
                    bienvenidaImage.setImage(new Image(fotoUrl, true));
                });

            } catch (Exception e) {
                System.out.println("Error al cargar usuario aleatorio:");
                System.out.println(e.getMessage());
            }
        }).start();
    }
    private void handleLikeAndCheckMatch() {
        if (usuario == null) return;

        new Thread(() -> {
            try {
                int usuarioActualId = usuario.id;
                String token = AuthSession.getToken();

                // 1) Enviamos like
                MatchApiExample.like(usuarioActualId, token);

                // 2) Comprobamos si hay match
                boolean hayMatch = MatchApiExample.checkMutualLike(usuarioActualId, token);

                if (hayMatch) {
                    // 3) Borramos el like inverso
                    MatchApiExample.unlikeReceived(usuarioActualId, token);

                    // 4) Creamos el match y obtenemos su ID
                    int idMatch = MatchApiExample.createMatch(usuarioActualId, token);
                    System.out.println("¡Match creado con ID " + idMatch + "!");

                    // 5) Llamamos al endpoint para crear el chat
                    ChatApiExample.crearChat(idMatch, token);
                    System.out.println("Chat creado para el match " + idMatch);
                }

            } catch (IOException e) {
                System.out.println("Error en proceso de like/match/chat:");
                System.out.println(MatchApiExample.getResponseError());
            } finally {
                Platform.runLater(this::cargarUsuarioAleatorio);
            }
        }).start();
    }
    private void irAJuegos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamup/juego-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) juegosButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Error al ir a la vista de juegos: " + e.getMessage());
        }
    }
    private void irAChats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/org/example/teamup/chat-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) chatsButton.getScene().getWindow();
            Scene scene = new Scene(root);

            // Reaplicas los estilos
            scene.getStylesheets().add(
                    org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(
                    getClass().getResource("/org/example/teamup/style.css")
                            .toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al ir a la vista de chats: " + e.getMessage());
        }
    }



}
