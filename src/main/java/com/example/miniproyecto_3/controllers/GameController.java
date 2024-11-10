package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class GameController {
    private Player player;
    private Machine machine;
    @FXML
    void OnHandlePlayButton(ActionEvent event) {
        player = new Player();
        machine = new Machine();
        System.out.println("Play button pressed");
        ArrayList<ArrayList<Integer>> positionTable = player.getplayerTable();
        player.boatPosition(false,0,0,4);
        ArrayList<ArrayList<Integer>> mainTable = machine.getmachineTable();
        machine.autoFillBoats();
        System.out.println(positionTable);
        System.out.println(mainTable);

    }
}
