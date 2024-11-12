module org.example.videoplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.databind;

    opens org.example.videoplayer to javafx.fxml;
    exports org.example.videoplayer;
}