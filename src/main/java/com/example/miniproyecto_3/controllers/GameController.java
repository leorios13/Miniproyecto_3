package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GameController {

    private final int BOARD_SIZE = 10;
    private final int CELL_SIZE = 50;

    @FXML
    private AnchorPane playerBoard;
    @FXML
    private AnchorPane enemyBoard;
    @FXML
    private AnchorPane shipPort;


    public void createBoard(AnchorPane board) {

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.BLACK);

                cell.setX(col * CELL_SIZE);
                cell.setY(row * CELL_SIZE);
                board.getChildren().add(cell);
            }
        }

    }

    public void initializeBoard() {
        createBoard(playerBoard);
        createBoard(enemyBoard);
        createShips(1,0,4,1);
        createShips(2,3,3,2);
        createShips(9,0,2,3);
        createShips(10,3,1,4);
    }

    public void createShips(int startX,int startY, int lenght, int cant){

        for(int i=0; i<cant; i++){

            Rectangle ship = new Rectangle();
            ship.setFill(Color.GRAY);
            ship.setStroke(Color.BLACK);

            ship.setWidth(lenght * CELL_SIZE);
            ship.setHeight(CELL_SIZE);

            ship.setX(startX * CELL_SIZE);
            ship.setY(startY * CELL_SIZE);

            shipPort.getChildren().add(ship);
            startX += lenght+1;
        }


    }

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
