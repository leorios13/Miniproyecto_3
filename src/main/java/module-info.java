module com.example.miniproyecto_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.miniproyecto_3 to javafx.fxml;
    opens com.example.miniproyecto_3.controllers to javafx.fxml;
    exports com.example.miniproyecto_3;
}