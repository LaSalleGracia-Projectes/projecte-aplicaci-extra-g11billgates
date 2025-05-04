package org.example.teamup;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.teamup.API.UserApiExample;
import org.example.teamup.models.UsuarioDTO;
import org.example.teamup.API.AuthSession;

public class MainViewController {

    @FXML
    private ImageView bienvenidaImage;
    @FXML
    private VBox imageContainer;
    @FXML
    private VBox textContainer;
    @FXML
    private Label tituloLabel;

    private UsuarioDTO usuario;
    private final String token = AuthSession.getToken();

    @FXML
    public void initialize() {
        cargarImagenFondo();
        ajustarLayout();
        cargarUsuarioAleatorio();
    }

    private void cargarImagenFondo() {
        Image image = new Image(getClass().getResource("/org/example/teamup/images/harambe_vertical.jpg").toExternalForm());
        bienvenidaImage.setImage(image);
        bienvenidaImage.setFitWidth(600);
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

                // Actualizar UI en el hilo de JavaFX
                Platform.runLater(() -> {
                    // Cambiar título
                    tituloLabel.setText(usuario.Nombre + " " + usuario.Edad);

                    // Mostrar imagen de perfil desde backend
                    String fotoUrl = "http://localhost:8000/storage/" + usuario.FotoPerfil;
                    bienvenidaImage.setImage(new Image(fotoUrl));
                });

            } catch (Exception e) {
                System.out.println("Error al cargar usuario aleatorio:");
                System.out.println(e.getMessage());
            }
        }).start();
    }
}

