package org.example.teamup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

//imports para la conexión con la base de datos de mysql

import java.sql.Connection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("/css/custom-bootstrapfx.css").toExternalForm());
        stage.setTitle("TeamUp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        /*
        try (Connection con = ConnectDB.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario"))
        {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("IDUsuario") +
                        ", Nombre: " + rs.getString("Nombre") +
                        ", Email: " + rs.getString("Correo") +
                        ", Edad: " + rs.getString("Edad") +
                        ", Region: " + rs.getString("Region")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
         */
        launch();
        //prueba de de funcionamiento de la base de datos

    }
}