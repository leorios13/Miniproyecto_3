package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import com.example.miniproyecto_3.view.GameView;
import com.example.miniproyecto_3.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.IOException;



public class WelcomeController {

    //@FXML
    //private TextField nicknameTextField;

    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {
        GameView.getInstance().getGameController().initializeBoard();

    }
    public void onHandleInstructionsButton(ActionEvent actionEvent){
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "   ", Alert.AlertType.INFORMATION);
    }
}
