package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.GameStateHandler;
import com.example.miniproyecto_3.view.GameView;
import com.example.miniproyecto_3.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

/**
 * WelcomeController class handles the events and interactions on the welcome screen.
 * It manages user input for the nickname and handles button actions for playing the game or viewing instructions.
 * @author Celeste Berrio - Leonardo Rios - Juan Montealegre
 */
public class WelcomeController {

    @FXML
    private TextField nicknameTextField; // TextField for user to input their nickname

    /**
     * Event handler for the "Play" button.
     * It checks if there is a saved game and prompts the user to either load the saved game or start a new one.
     * If no saved game is found, it initializes a new game board.
     *
     * @param event The ActionEvent triggered by clicking the "Play" button
     * @throws IOException If there is an error in loading the GameView
     */
    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {

        // Get the nickname from the TextField
        String nickName = nicknameTextField.getText();

        // Check if a saved game exists
        if (GameStateHandler.savedGameExists()) {
            // Create a confirmation alert to ask the user about loading the saved game
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Juego guardado encontrado");
            alert.setHeaderText("Se encontró una partida guardada");
            alert.setContentText("¿Deseas continuar la partida anterior o iniciar una nueva?");
            // Define button options for the user
            ButtonType buttonTypeLoad = new ButtonType("Cargar partida");
            ButtonType buttonTypeNew = new ButtonType("Nueva partida");

            // Set the buttons in the alert dialog
            alert.getButtonTypes().setAll(buttonTypeLoad, buttonTypeNew);

            // Show the alert and wait for the user's choice
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeLoad) {
                // Load the saved game if the user chooses "Load Game"
                GameView.getInstance().getGameController().loadGame();
            } else {
                // Clear saved game data and initialize a new game if "New Game" is chosen
                GameStateHandler.clearSavedGame();
                GameView.getInstance().getGameController().initializeBoard(nickName);
            }
        } else {
            // If no saved game exists, initialize a new game board
            GameView.getInstance().getGameController().initializeBoard(nickName);
        }

        // Close the current window
        ((javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * Event handler for the "Instructions" button.
     * Displays an alert box with game instructions.
     *
     * @param actionEvent The ActionEvent triggered by clicking the "Instructions" button
     */
    public void onHandleInstructionsButton(ActionEvent actionEvent) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "Bienvenidos a BattleField. \n\n- Escoge tú flota de barcos y enfrenta a tú enemigo.\n\n- Ingresa tú nombre en el campo proporcionado.\n\n- Si ya has jugado antes puedes cargar una partida anterior o jugar una nueva partida.  ", Alert.AlertType.INFORMATION);
    }
}