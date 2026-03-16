package me.adrian.paintball.game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private GameState state = GameState.LOBBY;
    private Arena currentArena;

    private final Map<Player, GameTeam> teams = new HashMap<>();
    private final Map<Player, Integer> kills = new HashMap<>();
    private final Map<Player, Integer> totalWins = new HashMap<>();

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentArena(Arena arena) {
        this.currentArena = arena;
    }

    // Equipo
    public GameTeam getTeam(Player player) {
        return teams.getOrDefault(player, GameTeam.NONE);
    }

    public void setTeam(Player player, GameTeam team) {
        teams.put(player, team);
    }

    // Jugador
    public boolean isPlaying(Player player) {
        return teams.containsKey(player);
    }

    public boolean isAlive(Player player) {
        return isPlaying(player) && kills.getOrDefault(player, 0) >= 0;
    }

    public void leave(Player player) {
        teams.remove(player);
        kills.remove(player);
    }

    public void eliminate(Player eliminated, Player killer) {
        kills.put(eliminated, kills.getOrDefault(eliminated, 0) + 1);
        totalWins.put(killer, totalWins.getOrDefault(killer, 0) + 1);
        leave(eliminated);
    }

    public int getKills(Player player) {
        return kills.getOrDefault(player, 0);
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player, 0);
    }

    public int getTeamCount() {
        return currentArena != null ? currentArena.getTeamCount() : 0;
    }

    public void setTeamCount(int count) {
        if (currentArena != null) currentArena.setTeamCount(count);
    }

    public String getMapName() {
        return currentArena != null ? currentArena.getName() : "Unknown";
    }

    public int getGameTime() {
        // Aquí pon la lógica de tiempo de juego
        return 0;
    }
}
