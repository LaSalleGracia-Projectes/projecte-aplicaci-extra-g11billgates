package org.example.teamup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private ImageView bienvenidaImage;
    @FXML
    private VBox imageContainer;
    @FXML
    private VBox textContainer;

    @FXML
    public void initialize() {
        // Cargar imagen
        Image image = new Image(getClass().getResource("/org/example/teamup/images/harambe_ancho.jpg").toExternalForm());
        bienvenidaImage.setImage(image);
        bienvenidaImage.setFitWidth(600); // Puedes ajustar si quieres tamaño inicial

        // Controlar proporciones de ancho
        imageContainer.setMaxWidth(Double.MAX_VALUE);
        textContainer.setMaxWidth(Double.MAX_VALUE);

        // Aplicar proporciones manualmente
        imageContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            imageContainer.setPrefWidth(imageContainer.getParent().getLayoutBounds().getWidth() * 0.6);
        });

        textContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            textContainer.setPrefWidth(textContainer.getParent().getLayoutBounds().getWidth() * 0.4);
        });
    }
}
