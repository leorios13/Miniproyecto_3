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
            playerTable.get(i).set(j, -1); // Marcamos como tocado
            return "Tocado";
        } else {
            return "Agua";
        }
    }

    public ArrayList<ArrayList<Integer>> getplayerTable() {
        return playerTable;
    }

    public void setValue(int i, int j, int value) {
        playerTable.get(i).set(j, value);
    }
}
