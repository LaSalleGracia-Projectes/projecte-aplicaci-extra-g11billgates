module org.example.teamup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires com.google.gson;
    requires java.sql;
    requires java.desktop;

    opens org.example.teamup to javafx.fxml;
    opens org.example.teamup.models to com.google.gson;
    exports org.example.teamup;
}