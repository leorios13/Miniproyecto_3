package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.GameStateHandler;
import com.example.miniproyecto_3.view.GameView;
import com.example.miniproyecto_3.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Optional;

public class WelcomeController {

    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {
        if (GameStateHandler.savedGameExists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Juego guardado encontrado");
            alert.setHeaderText("Se encontró una partida guardada");
            alert.setContentText("¿Deseas continuar la partida anterior o iniciar una nueva?");

            ButtonType buttonTypeLoad = new ButtonType("Cargar partida");
            ButtonType buttonTypeNew = new ButtonType("Nueva partida");

            alert.getButtonTypes().setAll(buttonTypeLoad, buttonTypeNew);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeLoad) {
                GameView.getInstance().getGameController().loadGame();
            } else {
                GameStateHandler.clearSavedGame();
                GameView.getInstance().getGameController().initializeBoard();
            }
        } else {
            GameView.getInstance().getGameController().initializeBoard();
        }
    }

    public void onHandleInstructionsButton(ActionEvent actionEvent) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "   ", Alert.AlertType.INFORMATION);
    }
}