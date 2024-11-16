package com.example.miniproyecto_3;

import com.example.miniproyecto_3.view.WelcomeView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class to launch the JavaFX application.
 * This is the entry point of the program, where the JavaFX application starts.
 * It extends the Application class and overrides the start method to set up the initial scene.
 * @author Celeste Berrio - Leonardo Rios - Juan Montealegre
 */
public class Main extends Application {

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command-line arguments passed to the program
     */
    public static void main(String[] args) {
        launch(args); // Calls the launch method to start the JavaFX application
    }

    /**
     * The start method is called after the application is launched.
     * This method initializes the primary stage and sets the initial scene to the WelcomeView.
     *
     * @param primaryStage The primary stage for this application
     * @throws IOException If there is an issue loading the WelcomeView
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        WelcomeView.getInstance(); // Gets the instance of WelcomeView
    }
}
