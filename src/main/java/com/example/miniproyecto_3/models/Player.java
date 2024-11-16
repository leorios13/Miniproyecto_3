package com.example.miniproyecto_3.models;

import java.util.ArrayList;
/**
 * Represents a player in the battleship game, managing the player's board state, nickname,
 * and providing methods for setting boat positions, checking shots, and verifying sunken boats.
 */
public class Player {
    private ArrayList<ArrayList<Integer>> playerTable;
    private final String playerNickName;

    /**
     * Constructor to initialize the player with a nickname and create an empty 10x10 board.
     *
     * @param nickName The nickname of the player.
     */
    public Player(String nickName) {
        playerNickName = nickName;
        playerTable = new ArrayList<>();
        // Initialize the player's board with zeros (empty cells)
        for (int i = 0; i < 10; i++) {
            playerTable.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                playerTable.get(i).add(0);
            }
        }
    }
    /**
     * Places a boat on the player's board either vertically or horizontally.
     *
     * @param vertical Indicates if the boat is placed vertically (true) or horizontally (false).
     * @param i The starting row index for the boat.
     * @param j The starting column index for the boat.
     * @param value The size of the boat (1-4).
     */
    public void boatPosition(boolean vertical, int i, int j, int value) {
        if (vertical) {
            for (int k = 0; k < value; k++) {
                playerTable.get(i + k).set(j, value);
            }
        } else {
            for (int k = 0; k < value; k++) {
                playerTable.get(i).set(j + k, value);
            }
        }
    }
    /**
     * Checks if a specified position on the player's board is free (contains no boat).
     *
     * @param i The row index to check.
     * @param j The column index to check.
     * @return True if the position is free (value is 0), otherwise false.
     */

    public boolean freePosition(int i, int j) {
        return playerTable.get(i).get(j) == 0;
    }

    /**
     * Evaluates a shot on the player's board and updates the board state.
     *
     * @param i The row index of the shot.
     * @param j The column index of the shot.
     * @return "Tocado" if the shot hits a boat, "Agua" if it hits an empty cell.
     */
    public String checkShot(int i, int j) {
        if (playerTable.get(i).get(j) != 0) {
            // Barco tocado
            if (playerTable.get(i).get(j) == 1) {
                playerTable.get(i).set(j, -1);
            }
            if (playerTable.get(i).get(j) == 2) {
                playerTable.get(i).set(j, -2);
            }
            if (playerTable.get(i).get(j) == 3) {
                playerTable.get(i).set(j, -3);
            }
            if (playerTable.get(i).get(j) == 4) {
                playerTable.get(i).set(j, -4);
            }
            // Marcamos como tocado
            return "Tocado";
        } else {
            playerTable.get(i).set(j, 5);
            return "Agua";
        }
    }

    /**
     * Gets the player's board.
     *
     * @return A 2D ArrayList representing the player's board.
     */
    public ArrayList<ArrayList<Integer>> getPlayerTable() {
        return playerTable;
    }

    /**
     * Retrieves the value at a specific position on the player's board.
     *
     * @param i The row index.
     * @param j The column index.
     * @return The value at the specified board position.
     */
    public int getValue(int i, int j) { return playerTable.get(i).get(j); }

    /**
     * Checks if a boat at a specified position is completely sunken.
     *
     * @param row The starting row index to check.
     * @param col The starting column index to check.
     * @param value The size of the boat being checked.
     * @return True if the boat is completely sunken, otherwise false.
     */
    public boolean checkBoatSunken(int row, int col, int value) {

        int consecutiveTouches = 0;
        ArrayList<Integer> positionsToUpdate = new ArrayList<>();

        // Recorre la fila
        for (int i = 0; i < 10; i++) {
            if (playerTable.get(i).get(col) == -value) {
                positionsToUpdate.add(i);
                consecutiveTouches++;

                // Si el número de impactos consecutivos equivale al tamaño del barco
                if (consecutiveTouches == value) {
                    for (int pos : positionsToUpdate) {
                        playerTable.get(pos).set(col, -5);
                    }
                    return true;
                }
            } else {
                // Si encuentra un espacio no impactado, reinicia el contador
                consecutiveTouches = 0;
            }
        }

        consecutiveTouches = 0;
        positionsToUpdate.clear();

        // Recorre la columna
        for (int j = 0; j < 10; j++) {
            if (playerTable.get(row).get(j) == -value) {
                positionsToUpdate.add(j);
                consecutiveTouches++;

                // Si el número de impactos consecutivos equivale al tamaño del barco
                if (consecutiveTouches == value) {
                    for (int pos : positionsToUpdate) {
                        playerTable.get(row).set(pos, -5);
                    }
                    return true;
                }
            } else {
                // Si encuentra un espacio no impactado, reinicia el contador
                consecutiveTouches = 0;
            }
        }
        return false;
    }

    /**
     * Retrieves the player's nickname.
     *
     * @return The player's nickname.
     */
    public String getPlayerNickName() {
        return playerNickName;
    }
}
