package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import com.example.miniproyecto_3.view.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class WelcomeController {

    private Player player;
    private Machine machine;
    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {
        player = new Player();
        machine = new Machine();
        GameView.getInstance().getGameController().initializeBoard();

    }
}
