package org.example.teamup;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML
    private StackPane contentArea;

    @FXML
    private ImageView bienvenidaImage;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/org/example/teamup/images/harambe.png").toExternalForm());
        bienvenidaImage.setImage(image);
    }


    // Ya no necesitamos initialize()
}
