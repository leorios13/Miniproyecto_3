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
            machineTable.get(i).set(j, -1); // Marcamos como tocado
            return "Tocado";
        } else {
            return "Agua";
        }
    }

    public int[] makeShot() {
        Random random = new Random();
        int i = random.nextInt(10);
        int j = random.nextInt(10);
        return new int[]{i, j};
    }

    public ArrayList<ArrayList<Integer>> getmachineTable() {
        return machineTable;
    }

    public void setValue(int i, int j, int value) {
        machineTable.get(i).set(j, value);
    }
}
