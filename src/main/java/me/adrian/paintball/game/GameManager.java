package me.adrian.paintball.game;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private final PaintballPlugin plugin;

    public GameManager(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    // =========================
    // Variables de juego
    // =========================
    private final HashMap<UUID, Player> players = new HashMap<>();
    private final HashMap<UUID, Boolean> alive = new HashMap<>();

    private final HashMap<UUID, Integer> kills = new HashMap<>();
    private final HashMap<UUID, Integer> totalKills = new HashMap<>();
    private final HashMap<UUID, Integer> totalWins = new HashMap<>();
    private final HashMap<UUID, Integer> coins = new HashMap<>();

    private final HashMap<String, Arena> arenas = new HashMap<>();
    private Arena currentArena;

    // =========================
    // Métodos de jugador
    // =========================
    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), player);
        alive.put(player.getUniqueId(), true);
        kills.putIfAbsent(player.getUniqueId(), 0);
        totalKills.putIfAbsent(player.getUniqueId(), 0);
        totalWins.putIfAbsent(player.getUniqueId(), 0);
        coins.putIfAbsent(player.getUniqueId(), 0);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        alive.remove(player.getUniqueId());
    }

    public boolean isPlaying(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean isAlive(Player player) {
        return alive.getOrDefault(player.getUniqueId(), false);
    }

    // =========================
    // Kills, Wins y Coins
    // =========================
    public void addKill(Player player) {
        UUID uuid = player.getUniqueId();
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
        totalKills.put(uuid, totalKills.getOrDefault(uuid, 0) + 1);
    }

    public int getTotalKills(Player player) {
        return totalKills.getOrDefault(player.getUniqueId(), 0);
    }

    public void addWin(Player player) {
        UUID uuid = player.getUniqueId();
        totalWins.put(uuid, totalWins.getOrDefault(uuid, 0) + 1);
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player.getUniqueId(), 0);
    }

    public void addCoins(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        coins.put(uuid, coins.getOrDefault(uuid, 0) + amount);
    }

    public int getCoins(Player player) {
        return coins.getOrDefault(player.getUniqueId(), 0);
    }

    // =========================
    // Manejo de Arenas
    // =========================
    public void createArena(Player player, String name) {
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        currentArena = arena;
    }

    public void removeArena(String name) {
        arenas.remove(name);
        if (currentArena != null && currentArena.getName().equals(name)) {
            currentArena = null;
        }
    }

    public void editArena(Player player, String name) {
        if (!arenas.containsKey(name)) return;
        currentArena = arenas.get(name);
    }

    public void setTeams(Player player, int teams) {
        if (currentArena != null) currentArena.setTeams(teams);
    }

    public void setSpawn(Player player, String teamColor) {
        if (currentArena != null) currentArena.setSpawn(teamColor, player.getLocation());
    }

    public void setCurrentArena(String name) {
        currentArena = arenas.get(name);
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    // =========================
    // Inicio y fin de juego
    // =========================
    public void startGame() {
        for (UUID uuid : players.keySet()) {
            alive.put(uuid, true);
            kills.put(uuid, 0);
        }
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkWin, 20L, 20L); // Cada segundo
    }

    public void eliminate(Player player) {
        alive.put(player.getUniqueId(), false);
        player.sendMessage("§6[Paintball] §cHas sido eliminado!");

        checkWin();
    }

    public void checkWin() {
        // Verifica si solo queda un equipo o jugador vivo
        List<Player> alivePlayers = new ArrayList<>();
        for (UUID uuid : alive.keySet()) {
            if (alive.get(uuid)) alivePlayers.add(players.get(uuid));
        }

        if (alivePlayers.size() == 1) {
            Player winner = alivePlayers.get(0);
            addWin(winner);
            addCoins(winner, 50); // Coins por ganar ronda
            winner.sendMessage("§6[Paintball] §a¡Has ganado la ronda y recibido 50 coins!");
            // Reiniciar ronda
            startGame();
        }
    }

    // =========================
    // Getter de jugadores
    // =========================
    public Collection<Player> getPlayers() {
        return players.values();
    }

    public Map<UUID, Boolean> getAliveMap() {
        return alive;
    }
}
