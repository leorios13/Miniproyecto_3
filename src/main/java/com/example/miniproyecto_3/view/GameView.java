package com.example.miniproyecto_3.view;

import com.example.miniproyecto_3.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Stage {

    private GameController gameController; // Controlador del juego

    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto_3/game-view.fxml")
        );
        Parent root = loader.load(); // Cargar la raíz desde el archivo FXML
        this.gameController = loader.getController(); // Obtener el controlador asociado
        this.setTitle("Batalla Naval"); // Establecer el título de la ventana
        Scene scene = new Scene(root); // Crear la escena con la raíz cargada
        this.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/miniproyecto_3/images/crucero.png")
        )); // Establecer el icono de la ventana
        this.setScene(scene); // Establecer la escena en la ventana
        this.show(); // Mostrar la ventana
    }

    public GameController getGameController() {
        return this.gameController; // Devolver el controlador
    }

    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            return GameViewHolder.INSTANCE = new GameView(); // Crear nueva instancia si no existe
        } else {
            return GameViewHolder.INSTANCE; // Devolver la instancia existente
        }
    }

    private static class GameViewHolder {
        private static GameView INSTANCE; // Instancia única de GameView
    }
}