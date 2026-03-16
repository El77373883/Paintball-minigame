package me.adrian.paintball.game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private Arena currentArena;
    private Map<Player, Boolean> alivePlayers = new HashMap<>();
    private Map<Player, Integer> totalWins = new HashMap<>();

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

    // Métodos que usan tus listeners y scoreboard
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
        // Devuelve un tiempo de ejemplo (puedes modificarlo según tu lógica)
        return 300; // 5 minutos
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player, 0);
    }

    // Métodos para manipular alivePlayers (ejemplo)
    public void addPlayer(Player player) {
        alivePlayers.put(player, true);
    }

    public void removePlayer(Player player) {
        alivePlayers.remove(player);
    }
}
