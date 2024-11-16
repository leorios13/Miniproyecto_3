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

/**
 * GameController is the main class for managing the game flow and interaction with the UI.
 * It controls the player's board, machine's board, and ship placement,
 * as well as handling the turn-based gameplay.
 * @author Celeste Berrio - Leonardo Rios - Juan Montealegre
 */
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
    private boolean playerTurn; // To alternate turns
    private int machineSunkenBoats;
    private int playerSunkenBoats;

    // Image patterns for various board states
    Image bombaImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/bomba.png"));
    ImagePattern bombaPattern = new ImagePattern(bombaImage);
    Image xImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/boton-x.png"));
    ImagePattern xPattern = new ImagePattern(xImage);
    Image sunkedImage = new Image(getClass().getResourceAsStream("/com/example/miniproyecto_3/images/crucero.png"));
    ImagePattern sunkedPattern = new ImagePattern(sunkedImage);

    /**
     * Initializes the game board with the player's nickname.
     *
     * @param nickName The player's nickname.
     */
    public void initializeBoard(String nickName) {
        player = new Player(nickName);
        machine = new Machine();
        placedPlayerBoats = 0;
        playerTurn = false;
        machineSunkenBoats = 0;
        playerSunkenBoats = 0;

        createBoard(playerBoard);
        createBoard(machineBoard);
        machine.autoFillBoats();
        addMouseEventsToMachineBoard();

        // Create ships with different sizes
        createShips(1, 0, 4, 1); // 1 ship of length 4
        createShips(3, 2, 3, 2); // 2 ships of length 3
        createShips(12, 2, 2, 3); // 3 ships of length 2
        createShips(7, 0, 1, 4); // 4 ships of length 1

        GameStateHandler.clearSavedGame(); // Clear previous saved games
    }

    /**
     * Creates the visual board by adding rectangles to represent cells.
     *
     * @param board The board (player or machine) to be created.
     */
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
    /**
     * Creates ships of specific lengths and quantities for the player.
     *
     * @param startX Starting X position.
     * @param startY Starting Y position.
     * @param lenght Length of the ship.
     * @param cant Number of ships to create.
     */
    public void createShips(int startX, int startY, int lenght, int cant) {
        for (int i = 0; i < cant; i++) {
            Rectangle ship = new Rectangle();
            // Assign colors based on ship size
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

    /**
     * Displays the machine's board to the player when the show button is clicked.
     *
     * @param event The button click event.
     */
    public void onHandleShowButton(ActionEvent event) throws IOException {
        updateBoardWithModel(machineBoard, machine.getmachineTable());
    }

    /**
     * Updates the board visually based on the current model state.
     *
     * @param board The board to update (player or machine).
     * @param modelBoard The model of the board to be reflected visually.
     */
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
    /**
     * Resets the color of all cells on the board to the default color.
     *
     * @param board The board to reset.
     */
    public void resetBoardColors(AnchorPane board) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            if (board.getChildren().get(i) instanceof Rectangle) {
                Rectangle cell = (Rectangle) board.getChildren().get(i);
                cell.setFill(Color.web("#9EECFF"));
                cell.setStroke(Color.BLACK);
            }
        }
    }

    /**
     * Adds mouse events for rotating and dragging the ships on the player's board.
     *
     * @param ship The ship rectangle to add events to.
     * @param initialX The initial X position of the ship.
     * @param initialY The initial Y position of the ship.
     */
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
    /**
     * Starts the game after all ships have been placed.
     */
    private void startGame() {
        new AlertBox().showAlert("INFORMATION", "¡El juego ha iniciado!", "Prepárate para comenzar la partida disparando a tu oponente...BUENA SUERTE :)", Alert.AlertType.INFORMATION);
        showButton.setDisable(true);
        resetBoardColors(machineBoard);
        playerTurn = true;
        playerBoard.toBack();
        machineBoard.toFront();
    }
    /**
     * Adds mouse click events to each cell of the machine's board, allowing the player to attack.
     * The method handles the player's turn by checking the shot result and updating the board accordingly.
     * It also handles end-of-game scenarios when all of the machine's boats are sunk.
     */
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
                                    initializeBoard(player.getPlayerNickName());
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

    /**
     * Executes the machine's turn in the game. The machine makes a shot at the player's board,
     * evaluates the result (hit or miss), and updates the game state accordingly.
     * If the machine sinks all the player's boats, the game ends with the player losing.
     */
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
                    initializeBoard(player.getPlayerNickName());
                    showButton.setDisable(false);
                    GameStateHandler.clearSavedGame(); // Clear the saved game state after losing
                    return;
                }
            }
            saveGame(); // Save the game state after a successful hit by the machine
            // Machine gets another turn after a successful hit
            machineTurn();
        } else {
            playerCell.setFill(xPattern);
            playerCell.setStroke(Color.BLACK);
            playerTurn = true;
            saveGame(); // Save the game state after a miss by the machine
        }
    }

    public void onHandleInstructionsButton(ActionEvent actionEvent) {
        new AlertBox().showAlert("INFORMATION", "INSTRUCCIONES DEL JUEGO", "- Colocación de barcos:\n\n * 1 portaaviones: ocupa 4 casillas.\n" +
                "* 2 submarinos: ocupan 3 casillas cada uno.\n"  +
                "* 3 destructores: ocupan 2 casillas cada uno.\n" +
                "* 4 fragatas: ocupan 1 casilla cada uno.\n\n - Ubicalos en tú tablero y empieza a jugar.\n\n - Tienes disparos ilimitados pero cuidado, el que primero derribe los barcos del enemigo ganará.\n\n - Realiza los disparos en el tablero principal (lado derecho) para intentar hundir los barcos del enemigo", Alert.AlertType.INFORMATION);
    }
    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        try {
            GameStateHandler.GameState state = new GameStateHandler.GameState(
                    player.getPlayerTable(),
                    machine.getmachineTable(),
                    player.getPlayerNickName(),
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

    /**
     * Loads a previously saved game state.
     */
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
                this.player = new Player(savedState.getPlayerNickName());
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
                showButton.setDisable(true);

                // Actualizar la visualización de los tableros incluyendo los disparos al agua
                for (int i = 0; i < machineBoard.getChildren().size(); i++) {
                    Rectangle machineCell = (Rectangle) machineBoard.getChildren().get(i);
                    Rectangle playerCell = (Rectangle) playerBoard.getChildren().get(i);
                    int row = i / BOARD_SIZE;
                    int col = i % BOARD_SIZE;

                    // Actualizar celda del tablero de la máquina
                    int machineCellValue = machine.getValue(row, col);
                    machineCell.setOnMousePressed(null);
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

                }

                addMouseEventsToMachineBoard();

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
                initializeBoard(player.getPlayerNickName());
            }
        } else {
            System.err.println("No se pudo cargar el juego guardado");
            initializeBoard(player.getPlayerNickName());
        }
    }

}
