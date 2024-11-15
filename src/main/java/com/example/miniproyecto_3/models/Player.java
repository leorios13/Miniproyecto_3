package com.example.miniproyecto_3.models;

import java.util.ArrayList;

public class Player {
    private ArrayList<ArrayList<Integer>> playerTable;

    public Player() {
        playerTable = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            playerTable.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                playerTable.get(i).add(0);
            }
        }
    }

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

    public boolean freePosition(int i, int j) {
        return playerTable.get(i).get(j) == 0;
    }

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
            return "Agua";
        }
    }

    public ArrayList<ArrayList<Integer>> getPlayerTable() {
        return playerTable;
    }

    public int getValue(int i, int j) { return playerTable.get(i).get(j); }

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
}
