package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.Machine;
import com.example.miniproyecto_3.models.Player;
import com.example.miniproyecto_3.view.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class GameController {

    private final int BOARD_SIZE = 10;
    private final int CELL_SIZE = 40;

    @FXML
    private AnchorPane playerBoard;
    @FXML
    private AnchorPane machineBoard;
    @FXML
    private AnchorPane shipPort;
    @FXML
    private Button showButton;

    private Player player;
    private Machine machine;
    private int placedPlayerBoats;
    private boolean playerTurn; // Para alternar turnos
    private int machineSunkenBoats;
    private int playerSunkenBoats;

    Image bombaImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/bomba.png"));
    ImagePattern bombaPattern = new ImagePattern(bombaImage);
    Image xImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/boton-x.png"));
    ImagePattern xPattern = new ImagePattern(xImage);
    Image sunkedImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/crucero.png"));
    ImagePattern sunkedPattern = new ImagePattern(sunkedImage);

    public void initializeBoard() {
        player = new Player();
        machine = new Machine();
        placedPlayerBoats = 0;
        playerTurn = false;
        machineSunkenBoats = 0;
        playerSunkenBoats = 0;

        createBoard(playerBoard);
        createBoard(machineBoard);
        machine.autoFillBoats();
        addMouseEventsToMachineBoard();

        createShips(1,0,4,1);
        createShips(3,2,3,2);
        createShips(12,2,2,3);
        createShips(7,0,1,4);
    }

    public void createBoard(AnchorPane board) {

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.web("#9EECFF"));
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
            if (cant == 1) {
                ship.setFill(Color.SALMON);
                ship.setStroke(Color.BLACK);
            }
            if (cant == 2) {
                ship.setFill(Color.YELLOW);
                ship.setStroke(Color.BLACK);
            }
            if (cant == 3) {
                ship.setFill(Color.CORAL);
                ship.setStroke(Color.BLACK);
            }
            if (cant == 4) {
                ship.setFill(Color.YELLOWGREEN);
                ship.setStroke(Color.BLACK);
            }
            ship.setWidth(lenght * CELL_SIZE);
            ship.setHeight(CELL_SIZE);

            double initialX = startX * CELL_SIZE;
            double initialY = startY * CELL_SIZE;
            ship.setX(initialX);
            ship.setY(initialY);
            ship.setId(String.valueOf(lenght));

            addMouseEventsToShip(ship, initialX, initialY);

            shipPort.getChildren().add(ship);
            startX += lenght+1;
        }

    }

    public void onHandleShowButton(ActionEvent event) throws IOException {

        updateBoardWithModel(machineBoard, machine.getmachineTable());
    }

    public void updateBoardWithModel(AnchorPane board, ArrayList<ArrayList<Integer>> modelBoard) {

        // funcion para crear barco en el tablero de la maquina a partir de la matriz interna
        for(int i = 0; i < board.getChildren().size(); i++) {

            int fila = i / 10;
            int col = i % 10;
            Rectangle cell = (Rectangle) board.getChildren().get(i);
            cell.setStroke(Color.BLACK);

            if(board.getId().equals("playerBoard") || !showButton.isDisabled()) {
                if(modelBoard.get(fila).get(col) == 1) {
                    cell.setFill(Color.YELLOWGREEN);
                }
                if(modelBoard.get(fila).get(col) == 2) {
                    cell.setFill(Color.CORAL);
                }
                if(modelBoard.get(fila).get(col) == 3) {
                    cell.setFill(Color.YELLOW);
                }
                if(modelBoard.get(fila).get(col) == 4) {
                    cell.setFill(Color.SALMON);
                }
            }

            if(modelBoard.get(fila).get(col) < 0) {

                if(modelBoard.get(fila).get(col) == -5) {
                    cell.setFill(sunkedPattern);
                } else {
                    cell.setFill(bombaPattern);
                }
            }
        }

    }

    public void resetBoardColors(AnchorPane board) {
        // Recorre todos los nodos del AnchorPane
        for (int i = 0; i < board.getChildren().size(); i++) {
            if (board.getChildren().get(i) instanceof Rectangle) {
                Rectangle cell = (Rectangle) board.getChildren().get(i);
                // Restablecer el color a un estado inicial (por ejemplo, color azul claro)
                cell.setFill(Color.web("#9EECFF"));
                cell.setStroke(Color.BLACK);
            }
        }
    }

    private void addMouseEventsToShip(Rectangle ship, double initialX, double initialY) {

        ship.setOnMousePressed(new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) //clic secundario que permite cambiar la orientación del barco
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
            @Override
            public void handle(MouseEvent event)
            {
                double x = 40 * (int) Math.floor(ship.getX() / 40);
                double y = 40 * (int) Math.floor(ship.getY() / 40);
                ship.setX(x);
                ship.setY(y);

                Bounds shipBounds = ship.localToScene(ship.getBoundsInLocal());
                Bounds playerBoardBounds = playerBoard.localToScene(playerBoard.getBoundsInLocal());

                if(playerBoardBounds.contains(shipBounds)) {

                    int cellsPlaced = 0;
                    int shipSize = Integer.parseInt(ship.getId());
                    boolean vertical = ship.getHeight() / 40 == shipSize;
                    int filaInicial = 0, colInicial = 0;

                    for(int i = 0; i < playerBoard.getChildren().size(); i++) {
                        Rectangle cell = (Rectangle) playerBoard.getChildren().get(i);
                        Bounds cellBounds = cell.localToScene(cell.getBoundsInLocal());
                        int fila = i / 10;
                        int col = i % 10;

                        if(shipBounds.contains(cellBounds) && player.freePosition(fila, col)) {
                            if(cellsPlaced == 0) {
                                filaInicial = fila;
                                colInicial = col;
                            }
                            cellsPlaced++;
                        }
                    }
                    if(cellsPlaced == shipSize) {
                        player.boatPosition(vertical, filaInicial, colInicial, shipSize);
                        ship.setVisible(false);
                        updateBoardWithModel(playerBoard, player.getPlayerTable());
                        placedPlayerBoats++;

                        if(placedPlayerBoats == 10) {
                            startGame();
                        }

                    } else {
                        ship.setX(initialX);
                        ship.setY(initialY);
                    }

                } else {
                    ship.setX(initialX);
                    ship.setY(initialY);
                }

            }
        });

    }

    private void startGame() {

        new AlertBox().showAlert("INFORMATION", "¡El juego ha iniciado!", "Prepárate para comenzar la partida disparando a tu oponente...BUENA SUERTE :)", Alert.AlertType.INFORMATION);
        showButton.setDisable(true);
        resetBoardColors(machineBoard);
        playerTurn = true;
        playerBoard.toBack();
        machineBoard.toFront();
    }

    private void addMouseEventsToMachineBoard() {

        for(int i = 0; i < machineBoard.getChildren().size(); i++) {

            Rectangle cell = (Rectangle) machineBoard.getChildren().get(i);

            cell.setOnMousePressed(new EventHandler <MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if(playerTurn) {
                        System.out.println("Turno del jugador");
                        if(event.getButton().equals(MouseButton.PRIMARY)) {
                            int cellIndex = machineBoard.getChildren().indexOf(cell);
                            int fila = cellIndex / 10;
                            int col = cellIndex % 10;
                            int boatSize = machine.getValue(fila, col);

                            String shotResult = machine.checkShot(fila, col);
                            cell.setOnMousePressed(null); //deshabilitar la celda que ya ha seleccionado para que no repita tiro

                            if(shotResult.equals("Tocado")) {

                                cell.setFill(bombaPattern);
                                cell.setStroke(Color.BLACK);

                                if(machine.checkBoatSunken(fila, col, boatSize)) {
                                    machineSunkenBoats++;
                                    updateBoardWithModel(machineBoard, machine.getmachineTable());

                                    if(machineSunkenBoats == 10) {
                                        new AlertBox().showAlert("INFORMATION", "¡El juego ha terminado!", "GANASTE :)", Alert.AlertType.INFORMATION);
                                        playerBoard.getChildren().clear();
                                        machineBoard.getChildren().clear();
                                        shipPort.getChildren().clear();
                                        initializeBoard();
                                        showButton.setDisable(false);
    
                                    }
                                }
                            } else {
                                cell.setFill(xPattern);
                                cell.setStroke(Color.BLACK);
                                machineTurn();
                            }

                        }
                    }
                }
            });
        }

    }

    public void machineTurn() {
        System.out.println("Turno de la máquina");
        playerTurn = false;
        int[] posTiro = machine.makeShot(player.getPlayerTable());
        int fila = posTiro[0];
        int col = posTiro[1];
        int boatSize = player.getValue(fila, col);
        String machineShotResult = player.checkShot(fila, col);
        int playerCellIndex = fila*10 + col;
        Rectangle playerCell = (Rectangle) playerBoard.getChildren().get(playerCellIndex);

        if(machineShotResult.equals("Tocado")) {
            playerCell.setFill(bombaPattern);
            playerCell.setStroke(Color.BLACK);

            if(player.checkBoatSunken(fila, col, boatSize)) {
                playerSunkenBoats++;
                updateBoardWithModel(playerBoard, player.getPlayerTable());

                if(playerSunkenBoats == 10) {
                    new AlertBox().showAlert("INFORMATION", "¡El juego ha terminado!", "PERDISTE :/", Alert.AlertType.INFORMATION);
                    playerBoard.getChildren().clear();
                    machineBoard.getChildren().clear();
                    shipPort.getChildren().clear();
                    initializeBoard();
                    showButton.setDisable(false);
                    return;}
            }

            machineTurn();
        } else {
            playerCell.setFill(xPattern);
            playerCell.setStroke(Color.BLACK);
            playerTurn = true;
        }
    }
    public void onHandleInstructionsButton(ActionEvent actionEvent){
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "   ", Alert.AlertType.INFORMATION);
    }
}
