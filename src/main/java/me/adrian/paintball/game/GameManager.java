package me.adrian.paintball.game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private Arena currentArena;
    private Map<Player, Boolean> alivePlayers = new HashMap<>();
    private Map<Player, Integer> totalWins = new HashMap<>();
    private Map<Player, Integer> kills = new HashMap<>();
    private Map<Player, String> teams = new HashMap<>();
    private GameState state = GameState.LOBBY;

    // Estados posibles del juego
    public enum GameState {
        LOBBY,
        IN_GAME,
        END
    }

    // Constructor
    public GameManager(Arena arena) {
        this.currentArena = arena;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentArena(Arena arena) {
        this.currentArena = arena;
    }

    // Métodos existentes
    public boolean isPlaying(Player player) {
        return alivePlayers.containsKey(player);
    }

    public boolean isAlive(Player player) {
        return alivePlayers.getOrDefault(player, false);
    }

    public void leave(Player player) {
        alivePlayers.remove(player);
    }

    public String getMapName() {
        return currentArena != null ? currentArena.getName() : "Ninguna";
    }

    public int getGameTime() {
        return 300; // tiempo de ejemplo
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player, 0);
    }

    // NUEVOS MÉTODOS

    // 1. Obtener estado del juego
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    // 2. Eliminar un jugador de otro (registrar kill)
    public void eliminate(Player eliminator, Player eliminated) {
        alivePlayers.put(eliminated, false); // Muerto
        kills.put(eliminator, getKills(eliminator) + 1);
    }

    // 3. Obtener equipo de un jugador
    public String getTeam(Player player) {
        return teams.getOrDefault(player, "Ninguno");
    }

    public void setTeam(Player player, String teamName) {
        teams.put(player, teamName);
    }

    // 4. Obtener kills de un jugador
    public int getKills(Player player) {
        return kills.getOrDefault(player, 0);
    }

    // Métodos para manipular alivePlayers
    public void addPlayer(Player player) {
        alivePlayers.put(player, true);
    }

    public void removePlayer(Player player) {
        alivePlayers.remove(player);
    }
}
