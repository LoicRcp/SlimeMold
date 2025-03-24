module com.example.slimemolds {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens com.example.slimemolds to javafx.fxml;
    exports com.example.slimemolds;
    exports com.example.slimemolds.core;
    opens com.example.slimemolds.core to javafx.fxml;
    exports com.example.slimemolds.model;
    opens com.example.slimemolds.model to javafx.fxml;
}