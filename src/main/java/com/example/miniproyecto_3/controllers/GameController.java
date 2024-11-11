package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class GameController {
    private Player player;
    private Machine machine;

    private boolean playerTurn = true; // Para alternar turnos

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

    @FXML
    void OnHandlePlayerShot(ActionEvent event) {
        if (playerTurn) {
            // Aquí puedes obtener la coordenada donde el jugador disparó
            int row = 2; // Ejemplo, reemplazar con la coordenada real
            int col = 3; // Ejemplo, reemplazar con la coordenada real

            String result = machine.checkShot(row, col);
            System.out.println("Resultado disparo jugador: " + result);

            playerTurn = false; // Cambiar turno
        }
    }

    @FXML
    void OnHandleMachineShot(ActionEvent event) {
        if (!playerTurn) {
            // Aquí puedes obtener las coordenadas donde la máquina dispara
            int[] shot = machine.makeShot();
            int row = shot[0];
            int col = shot[1];

            String result = player.checkShot(row, col);
            System.out.println("Resultado disparo máquina: " + result);

            playerTurn = true; // Cambiar turno
        }
    }
}
