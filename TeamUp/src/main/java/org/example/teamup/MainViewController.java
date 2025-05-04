package org.example.teamup;


import com.google.gson.Gson;
import org.example.teamup.API.UserApiExample;
import org.example.teamup.models.UsuarioDTO;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private ImageView bienvenidaImage;
    @FXML
    private VBox imageContainer;
    @FXML
    private VBox textContainer;

    private UsuarioDTO usuario; // aquí guardamos el usuario aleatorio
    private final String token = "TU_TOKEN"; // ⚠️ Sustituye por el token real o pásalo desde login

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

                // Aquí ya tienes todos los datos en 'usuario'
                System.out.println("Usuario recibido:");
                System.out.println("Nombre: " + usuario.Nombre);
                System.out.println("Edad: " + usuario.Edad);
                System.out.println("Región: " + usuario.Region);
                System.out.println("FotoPerfil: " + usuario.FotoPerfil);

                // Luego puedes usar Platform.runLater(...) para actualizar la vista si hace falta

            } catch (Exception e) {
                System.out.println("Error al cargar usuario aleatorio:");
                System.out.println(e.getMessage());
            }
        }).start();
    }
}
