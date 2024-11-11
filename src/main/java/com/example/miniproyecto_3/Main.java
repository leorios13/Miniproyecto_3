package com.example.miniproyecto_3;

import com.example.miniproyecto_3.view.WelcomeView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args); // Llamar al método launch para iniciar JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        WelcomeView.getInstance(); // Obtener la instancia de WelcomeView
    }
}