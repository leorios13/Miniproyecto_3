package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    private AnchorPane machineBoard;
    @FXML
    private AnchorPane shipPort;

    private Player player;
    private Machine machine;

    public void initializeBoard() {
        player = new Player();
        machine = new Machine();

        createBoard(playerBoard);
        createBoard(machineBoard);
        machine.autoFillBoats();
        updateBoardWithModel(machineBoard, machine.getmachineTable());

        createShips(1,0,4,1);
        createShips(3,2,3,2);
        createShips(9,0,2,3);
        createShips(11,2,1,4);
    }

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

    public void createShips(int startX,int startY, int lenght, int cant){

        for(int i=0; i<cant; i++){

            Rectangle ship = new Rectangle();
            ship.setFill(Color.GRAY);
            ship.setStroke(Color.BLACK);

            ship.setWidth(lenght * CELL_SIZE);
            ship.setHeight(CELL_SIZE);

            ship.setX(startX * CELL_SIZE);
            ship.setY(startY * CELL_SIZE);
            ship.setId(String.valueOf(lenght));

            ship.setOnMousePressed(new EventHandler <MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if(event.getButton().equals(MouseButton.SECONDARY)) {
                        if (event.getClickCount() == 1) {
                            double width = ship.getWidth();
                            double height = ship.getHeight();
                            ship.setWidth(height);
                            ship.setHeight(width);
                        }
                    }
                }
            });

            ship.setOnMouseDragged(new EventHandler <MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    ship.setX(event.getX());
                    ship.setY(event.getY());
                    event.setDragDetect(false);
                }
            });

            ship.setOnMouseReleased(new EventHandler <MouseEvent>()
            {
                public void handle(MouseEvent event)
                {
                    double x = 50 * (int) Math.floor(ship.getX() / 50);
                    double y = (50 * (int) Math.floor(ship.getY() / 50)) + 21;
                    ship.setX(x);
                    ship.setY(y);
                    ship.setMouseTransparent(false);

                    Bounds shipBounds = ship.localToScene(ship.getBoundsInLocal());
                    for(int i = 0; i < playerBoard.getChildren().size(); i++) {
                        Rectangle cell = (Rectangle) playerBoard.getChildren().get(i);
                        Bounds cellBounds = cell.localToScene(cell.getBoundsInLocal());

                        if(cellBounds.getMinX() >= shipBounds.getMinX() && cellBounds.getMaxX() <= shipBounds.getMaxX()
                        && cellBounds.getMinY() >= shipBounds.getMinY() && cellBounds.getMaxY() <= shipBounds.getMaxY()) {

                            int fila = i / 10;
                            int col = i % 10;
                            if(player.freePosition(fila, col)) {
                                player.setValue(fila, col, Integer.parseInt(ship.getId()));
                            }
                        }

                    }

                    /*
                    for(int j = 0; j < player.getplayerTable().size(); j++) {
                        for(int k = 0; k < player.getplayerTable().get(0).size(); k++) {
                            System.out.println(player.getplayerTable().get(j).get(k));
                        }
                    }

                     */


                }
            });


            shipPort.getChildren().add(ship);
            startX += lenght+1;
        }

    }

    public void updateBoardWithModel(AnchorPane board, ArrayList<ArrayList<Integer>> modelBoard) {

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                if(modelBoard.get(row).get(col) != 0) {
                    Rectangle ship = new Rectangle();
                    ship.setFill(Color.GRAY);
                    ship.setStroke(Color.BLACK);

                    ship.setWidth(CELL_SIZE);
                    ship.setHeight(CELL_SIZE);

                    ship.setX(row * CELL_SIZE);
                    ship.setY(col * CELL_SIZE);

                    board.getChildren().add(ship);
                }
            }
        }

    }



    private boolean playerTurn = true; // Para alternar turnos

    @FXML
    void OnHandlePlayButton(ActionEvent event) {
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
