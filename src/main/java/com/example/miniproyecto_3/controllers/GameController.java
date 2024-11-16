package com.example.miniproyecto_3.controllers;

import com.example.miniproyecto_3.models.GameStateHandler;
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

        createShips(1, 0, 4, 1);
        createShips(3, 2, 3, 2);
        createShips(12, 2, 2, 3);
        createShips(7, 0, 1, 4);

        GameStateHandler.clearSavedGame(); // Limpiar juegos guardados anteriores
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

    public void createShips(int startX, int startY, int lenght, int cant) {
        for (int i = 0; i < cant; i++) {
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
            startX += lenght + 1;
        }
    }

    public void onHandleShowButton(ActionEvent event) throws IOException {
        updateBoardWithModel(machineBoard, machine.getmachineTable());
    }

    public void updateBoardWithModel(AnchorPane board, ArrayList<ArrayList<Integer>> modelBoard) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            int fila = i / 10;
            int col = i % 10;
            Rectangle cell = (Rectangle) board.getChildren().get(i);
            cell.setStroke(Color.BLACK);

            if (board.getId().equals("playerBoard") || !showButton.isDisabled()) {
                if (modelBoard.get(fila).get(col) == 1) {
                    cell.setFill(Color.YELLOWGREEN);
                }
                if (modelBoard.get(fila).get(col) == 2) {
                    cell.setFill(Color.CORAL);
                }
                if (modelBoard.get(fila).get(col) == 3) {
                    cell.setFill(Color.YELLOW);
                }
                if (modelBoard.get(fila).get(col) == 4) {
                    cell.setFill(Color.SALMON);
                }
            }

            if (modelBoard.get(fila).get(col) < 0) {
                if (modelBoard.get(fila).get(col) == -5) {
                    cell.setFill(sunkedPattern);
                } else {
                    cell.setFill(bombaPattern);
                }
            }
        }
    }

    public void resetBoardColors(AnchorPane board) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            if (board.getChildren().get(i) instanceof Rectangle) {
                Rectangle cell = (Rectangle) board.getChildren().get(i);
                cell.setFill(Color.web("#9EECFF"));
                cell.setStroke(Color.BLACK);
            }
        }
    }

    private void addMouseEventsToShip(Rectangle ship, double initialX, double initialY) {
        ship.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                if (event.getClickCount() == 1) {
                    double width = ship.getWidth();
                    double height = ship.getHeight();
                    ship.setWidth(height);
                    ship.setHeight(width);
                }
            }
        });

        ship.setOnMouseDragged(event -> {
            ship.setX(event.getX());
            ship.setY(event.getY());
            event.setDragDetect(false);
        });

        ship.setOnMouseReleased(event -> {
            double x = 40 * (int) Math.floor(ship.getX() / 40);
            double y = 40 * (int) Math.floor(ship.getY() / 40);
            ship.setX(x);
            ship.setY(y);

            Bounds shipBounds = ship.localToScene(ship.getBoundsInLocal());
            Bounds playerBoardBounds = playerBoard.localToScene(playerBoard.getBoundsInLocal());

            if (playerBoardBounds.contains(shipBounds)) {
                int cellsPlaced = 0;
                int shipSize = Integer.parseInt(ship.getId());
                boolean vertical = ship.getHeight() / 40 == shipSize;
                int filaInicial = 0, colInicial = 0;

                for (int i = 0; i < playerBoard.getChildren().size(); i++) {
                    Rectangle cell = (Rectangle) playerBoard.getChildren().get(i);
                    Bounds cellBounds = cell.localToScene(cell.getBoundsInLocal());
                    int fila = i / 10;
                    int col = i % 10;

                    if (shipBounds.contains(cellBounds) && player.freePosition(fila, col)) {
                        if (cellsPlaced == 0) {
                            filaInicial = fila;
                            colInicial = col;
                        }
                        cellsPlaced++;
                    }
                }
                if (cellsPlaced == shipSize) {
                    player.boatPosition(vertical, filaInicial, colInicial, shipSize);
                    ship.setVisible(false);
                    updateBoardWithModel(playerBoard, player.getPlayerTable());
                    placedPlayerBoats++;

                    if (placedPlayerBoats == 10) {
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
        for (int i = 0; i < machineBoard.getChildren().size(); i++) {
            Rectangle cell = (Rectangle) machineBoard.getChildren().get(i);

            cell.setOnMousePressed(event -> {
                if (playerTurn) {
                    System.out.println("Turno del jugador");
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        int cellIndex = machineBoard.getChildren().indexOf(cell);
                        int fila = cellIndex / 10;
                        int col = cellIndex % 10;
                        int boatSize = machine.getValue(fila, col);

                        String shotResult = machine.checkShot(fila, col);
                        cell.setOnMousePressed(null);

                        if (shotResult.equals("Tocado")) {
                            cell.setFill(bombaPattern);
                            cell.setStroke(Color.BLACK);

                            if (machine.checkBoatSunken(fila, col, boatSize)) {
                                machineSunkenBoats++;
                                updateBoardWithModel(machineBoard, machine.getmachineTable());

                                if (machineSunkenBoats == 10) {
                                    new AlertBox().showAlert("INFORMATION", "¡El juego ha terminado!", "GANASTE :)", Alert.AlertType.INFORMATION);
                                    playerBoard.getChildren().clear();
                                    machineBoard.getChildren().clear();
                                    shipPort.getChildren().clear();
                                    initializeBoard();
                                    showButton.setDisable(false);
                                    GameStateHandler.clearSavedGame(); // Limpiar el juego guardado al terminar
                                }
                            }
                            saveGame(); // Guardar después de un disparo exitoso
                        } else {
                            cell.setFill(xPattern);
                            cell.setStroke(Color.BLACK);
                            saveGame(); // Guardar después de un disparo al agua
                            machineTurn();
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
        int playerCellIndex = fila * 10 + col;
        Rectangle playerCell = (Rectangle) playerBoard.getChildren().get(playerCellIndex);

        if (machineShotResult.equals("Tocado")) {
            playerCell.setFill(bombaPattern);
            playerCell.setStroke(Color.BLACK);

            if (player.checkBoatSunken(fila, col, boatSize)) {
                playerSunkenBoats++;
                updateBoardWithModel(playerBoard, player.getPlayerTable());

                if (playerSunkenBoats == 10) {
                    new AlertBox().showAlert("INFORMATION", "¡El juego ha terminado!", "PERDISTE :/", Alert.AlertType.INFORMATION);
                    playerBoard.getChildren().clear();
                    machineBoard.getChildren().clear();
                    shipPort.getChildren().clear();
                    initializeBoard();
                    showButton.setDisable(false);
                    GameStateHandler.clearSavedGame(); // Limpiar el juego guardado al terminar
                    return;
                }
            }
            saveGame(); // Guardar después de un disparo exitoso de la máquina
            machineTurn();
        } else {
            playerCell.setFill(xPattern);
            playerCell.setStroke(Color.BLACK);
            playerTurn = true;
            saveGame(); // Guardar después de un disparo al agua de la máquina
        }
    }

    public void onHandleInstructionsButton(ActionEvent actionEvent) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "   ", Alert.AlertType.INFORMATION);
    }

    public void saveGame() {
        try {
            GameStateHandler.GameState state = new GameStateHandler.GameState(
                    player.getPlayerTable(),
                    machine.getmachineTable(),
                    machineSunkenBoats,
                    playerSunkenBoats,
                    playerTurn,
                    true
            );
            GameStateHandler.saveGameState(state);
            System.out.println("Juego guardado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al guardar el juego: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void loadGame() {
        GameStateHandler.GameState savedState = GameStateHandler.loadGameState();
        if (savedState != null) {
            try {
                // Limpiar los tableros existentes
                playerBoard.getChildren().clear();
                machineBoard.getChildren().clear();
                shipPort.getChildren().clear();

                // Recrear los tableros visuales
                createBoard(playerBoard);
                createBoard(machineBoard);

                // Restaurar el estado del juego
                this.player = new Player();
                this.machine = new Machine();
                this.placedPlayerBoats = 10; // Todos los barcos ya están colocados

                // Restaurar las tablas de juego
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        player.getPlayerTable().get(i).set(j, savedState.getPlayerTable().get(i).get(j));
                        machine.getmachineTable().get(i).set(j, savedState.getMachineTable().get(i).get(j));
                    }
                }

                // Restaurar variables de estado
                this.machineSunkenBoats = savedState.getMachineSunkenBoats();
                this.playerSunkenBoats = savedState.getPlayerSunkenBoats();
                this.playerTurn = savedState.isPlayerTurn();

                // Configurar la interfaz para juego en progreso
                shipPort.setVisible(false);
                showButton.setDisable(true);

                // Actualizar la visualización de los tableros incluyendo los disparos al agua
                for (int i = 0; i < machineBoard.getChildren().size(); i++) {
                    Rectangle machineCell = (Rectangle) machineBoard.getChildren().get(i);
                    Rectangle playerCell = (Rectangle) playerBoard.getChildren().get(i);
                    int row = i / BOARD_SIZE;
                    int col = i % BOARD_SIZE;

                    // Actualizar celda del tablero de la máquina
                    int machineCellValue = machine.getValue(row, col);
                    if (machineCellValue == 5) {
                        machineCell.setFill(xPattern);
                        machineCell.setStroke(Color.BLACK);
                    } else if (machineCellValue < 0) {
                        if (machineCellValue == -5) {
                            machineCell.setFill(sunkedPattern);
                        } else {
                            machineCell.setFill(bombaPattern);
                        }
                        machineCell.setStroke(Color.BLACK);
                    }

                    // Actualizar celda del tablero del jugador
                    int playerCellValue = player.getValue(row, col);
                    if (playerCellValue == 5) {
                        playerCell.setFill(xPattern);
                        playerCell.setStroke(Color.BLACK);
                    } else if (playerCellValue < 0) {
                        if (playerCellValue == -5) {
                            playerCell.setFill(sunkedPattern);
                        } else {
                            playerCell.setFill(bombaPattern);
                        }
                        playerCell.setStroke(Color.BLACK);
                    } else if (playerCellValue > 0 && playerCellValue != 5) {
                        // Mostrar los barcos del jugador
                        switch (playerCellValue) {
                            case 1: playerCell.setFill(Color.YELLOWGREEN); break;
                            case 2: playerCell.setFill(Color.CORAL); break;
                            case 3: playerCell.setFill(Color.YELLOW); break;
                            case 4: playerCell.setFill(Color.SALMON); break;
                        }
                        playerCell.setStroke(Color.BLACK);
                    }

                    // Agregar eventos solo a celdas no disparadas en el tablero de la máquina
                    if (machineCellValue >= 0 && machineCellValue != 5) {
                        final int finalRow = row;
                        final int finalCol = col;

                        machineCell.setOnMousePressed(event -> {
                            if (playerTurn && event.getButton().equals(MouseButton.PRIMARY)) {
                                int boatSize = machine.getValue(finalRow, finalCol);
                                String shotResult = machine.checkShot(finalRow, finalCol);
                                machineCell.setOnMousePressed(null);

                                if (shotResult.equals("Tocado")) {
                                    machineCell.setFill(bombaPattern);
                                    machineCell.setStroke(Color.BLACK);

                                    if (machine.checkBoatSunken(finalRow, finalCol, boatSize)) {
                                        machineSunkenBoats++;
                                        updateBoardWithModel(machineBoard, machine.getmachineTable());

                                        if (machineSunkenBoats == 10) {
                                            new AlertBox().showAlert("INFORMATION", "¡El juego ha terminado!",
                                                    "GANASTE :)", Alert.AlertType.INFORMATION);
                                            playerBoard.getChildren().clear();
                                            machineBoard.getChildren().clear();
                                            shipPort.getChildren().clear();
                                            initializeBoard();
                                            showButton.setDisable(false);
                                            GameStateHandler.clearSavedGame();
                                            return;
                                        }
                                    }
                                    saveGame();
                                } else {
                                    machineCell.setFill(xPattern);
                                    machineCell.setStroke(Color.BLACK);
                                    saveGame();
                                    machineTurn();
                                }
                            }
                        });
                    }
                }

                // Colocar los tableros en la posición correcta
                playerBoard.toBack();
                machineBoard.toFront();

                // Mostrar alerta de que el juego se ha cargado
                new AlertBox().showAlert("INFORMATION", "Partida Cargada",
                        "La partida se ha cargado exitosamente. " +
                                (playerTurn ? "Es tu turno para disparar." : "Espera tu turno..."),
                        Alert.AlertType.INFORMATION);

                // Si no es el turno del jugador, hacer que la máquina juegue
                if (!playerTurn) {
                    machineTurn();
                }

                System.out.println("Juego cargado exitosamente");
            } catch (Exception e) {
                System.err.println("Error durante la carga del juego: " + e.getMessage());
                e.printStackTrace();
                initializeBoard();
            }
        } else {
            System.err.println("No se pudo cargar el juego guardado");
            initializeBoard();
        }
    }

}