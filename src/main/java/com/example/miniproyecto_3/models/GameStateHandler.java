package com.example.miniproyecto_3.models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameStateHandler {
    private static final String GAME_STATE_FILE = "game_state.ser";
    private static final String GAME_INFO_FILE = "game_info.txt";

    public static class GameState implements Serializable {
        private ArrayList<ArrayList<Integer>> playerTable;
        private ArrayList<ArrayList<Integer>> machineTable;
        private int machineSunkenBoats;
        private int playerSunkenBoats;
        private boolean playerTurn;
        private boolean gameInProgress;


        public GameState(ArrayList<ArrayList<Integer>> playerTable,
                         ArrayList<ArrayList<Integer>> machineTable,
                         int machineSunkenBoats,
                         int playerSunkenBoats,
                         boolean playerTurn,
                         boolean gameInProgress) {
            this.playerTable = playerTable;
            this.machineTable = machineTable;
            this.machineSunkenBoats = machineSunkenBoats;
            this.playerSunkenBoats = playerSunkenBoats;
            this.playerTurn = playerTurn;
            this.gameInProgress = gameInProgress;
        }

        // Getters
        public ArrayList<ArrayList<Integer>> getPlayerTable() { return playerTable; }
        public ArrayList<ArrayList<Integer>> getMachineTable() { return machineTable; }
        public int getMachineSunkenBoats() { return machineSunkenBoats; }
        public int getPlayerSunkenBoats() { return playerSunkenBoats; }
        public boolean isPlayerTurn() { return playerTurn; }
        public boolean isGameInProgress() { return gameInProgress; }
    }

    public static void saveGameState(GameState state) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(Paths.get("saves"));

            // Guardar estado serializado
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("saves/" + GAME_STATE_FILE))) {
                oos.writeObject(state);
            }

            // Guardar información en archivo de texto
            try (PrintWriter writer = new PrintWriter(new FileWriter("saves/" + GAME_INFO_FILE))) {
                writer.println("Estado del juego:");
                writer.println("----------------");
                writer.println("Barcos hundidos del jugador: " + state.getPlayerSunkenBoats());
                writer.println("Barcos hundidos de la máquina: " + state.getMachineSunkenBoats());
                writer.println("Turno del jugador: " + (state.isPlayerTurn() ? "Sí" : "No"));
                writer.println("Juego en progreso: " + (state.isGameInProgress() ? "Sí" : "No"));
                writer.println("----------------");
                writer.println("Última actualización: " + java.time.LocalDateTime.now());

                // Guardar estado del tablero del jugador
                writer.println("\nTablero del Jugador:");
                saveTableToFile(writer, state.getPlayerTable());

                // Guardar estado del tablero de la máquina
                writer.println("\nTablero de la Máquina:");
                saveTableToFile(writer, state.getMachineTable());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveTableToFile(PrintWriter writer, ArrayList<ArrayList<Integer>> table) {
        for (ArrayList<Integer> row : table) {
            writer.println(row.toString());
        }
    }

    public static GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("saves/" + GAME_STATE_FILE))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static boolean savedGameExists() {
        File serFile = new File("saves/" + GAME_STATE_FILE);
        File txtFile = new File("saves/" + GAME_INFO_FILE);
        return serFile.exists() && serFile.length() > 0 && txtFile.exists();
    }

    public static void clearSavedGame() {
        File serFile = new File("saves/" + GAME_STATE_FILE);
        File txtFile = new File("saves/" + GAME_INFO_FILE);
        if (serFile.exists()) {
            serFile.delete();
        }
        if (txtFile.exists()) {
            txtFile.delete();
        }
    }
}