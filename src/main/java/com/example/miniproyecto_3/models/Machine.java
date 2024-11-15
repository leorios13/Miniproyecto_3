package com.example.miniproyecto_3.models;

import java.util.ArrayList;
import java.util.Random;

public class Machine {
    private ArrayList<ArrayList<Integer>> machineTable;

    public Machine() {
        machineTable = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            machineTable.add(new ArrayList<>());
            for (int j = 0; j < 10; j++) {
                machineTable.get(i).add(0);
            }
        }
    }

    public void autoFillBoats() {
        Random random = new Random();

        placeBoat(4, 1, random);
        placeBoat(3, 2, random);
        placeBoat(2, 3, random);
        placeBoat(1, 4, random);
    }

    private void placeBoat(int size, int count, Random random) {
        for (int n = 0; n < count; n++) {
            boolean placed = false;

            while (!placed) {
                boolean vertical = random.nextBoolean();
                int i = random.nextInt(10);
                int j = random.nextInt(10);

                if (canPlaceBoat(i, j, size, vertical)) {
                    boatPosition(vertical, i, j, size);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceBoat(int i, int j, int size, boolean vertical) {
        if (vertical) {
            if (i + size > 10) return false;
            for (int k = 0; k < size; k++) {
                if (machineTable.get(i + k).get(j) != 0) return false;
            }
        } else {
            if (j + size > 10) return false;
            for (int k = 0; k < size; k++) {
                if (machineTable.get(i).get(j + k) != 0) return false;
            }
        }
        return true;
    }

    private void boatPosition(boolean vertical, int i, int j, int value) {
        if (vertical) {
            for (int k = 0; k < value; k++) {
                machineTable.get(i + k).set(j, value);
            }
        } else {
            for (int k = 0; k < value; k++) {
                machineTable.get(i).set(j + k, value);
            }
        }
    }

    public String checkShot(int i, int j) {
        if (machineTable.get(i).get(j) != 0) {
            if (machineTable.get(i).get(j) == 1) {
                machineTable.get(i).set(j, -1);
            }
            if (machineTable.get(i).get(j) == 2) {
                machineTable.get(i).set(j, -2);
            }
            if (machineTable.get(i).get(j) == 3) {
                machineTable.get(i).set(j, -3);
            }
            if (machineTable.get(i).get(j) == 4) {
                machineTable.get(i).set(j, -4);
            }// Marcamos como tocado
            return "Tocado";
        } else {
            machineTable.get(i).set(j, 5);
            return "Agua";
        }
    }

    public int[] makeShot(ArrayList<ArrayList<Integer>> playerTable) {
        Random random = new Random();
        int i = random.nextInt(10);
        int j = random.nextInt(10);

        while(playerTable.get(i).get(j) < 0 || playerTable.get(i).get(j) == 5) {
            i = random.nextInt(10);
            j = random.nextInt(10);
        }

        return new int[]{i, j};
    }

    public ArrayList<ArrayList<Integer>> getmachineTable() {
        return machineTable;
    }


    public int getValue(int i, int j) { return machineTable.get(i).get(j); }

    public boolean checkBoatSunken(int row, int col, int value) {

        int consecutiveTouches = 0;
        ArrayList<Integer> positionsToUpdate = new ArrayList<>();

        // Recorre la fila
        for (int i = 0; i < 10; i++) {
            if (machineTable.get(i).get(col) == -value) {
                positionsToUpdate.add(i);
                consecutiveTouches++;

                // Si el número de impactos consecutivos equivale al tamaño del barco
                if (consecutiveTouches == value) {
                    for (int pos : positionsToUpdate) {
                        machineTable.get(pos).set(col, -5);
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
            if (machineTable.get(row).get(j) == -value) {
                positionsToUpdate.add(j);
                consecutiveTouches++;

                // Si el número de impactos consecutivos equivale al tamaño del barco
                if (consecutiveTouches == value) {
                    for (int pos : positionsToUpdate) {
                        machineTable.get(row).set(pos, -5);
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
