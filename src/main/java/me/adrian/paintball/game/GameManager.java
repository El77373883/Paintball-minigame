package me.adrian.paintball.game;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private final PaintballPlugin plugin;

    // Jugadores en la partida
    private final Set<UUID> players = new HashSet<>();
    private final Set<UUID> alivePlayers = new HashSet<>();

    // Teams: RED, BLUE, PINK, GREEN
    private final Map<UUID, GameTeam> playerTeams = new HashMap<>();

    // Kills, victorias y coins
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalWins = new HashMap<>();
    private final Map<UUID, Integer> coins = new HashMap<>();

    // Arenas
    private final Map<String, Arena> arenas = new HashMap<>();
    private Arena currentArena;

    // Estado del juego
    private GameState state = GameState.WAITING;

    // Tiempo de juego
    private int gameTime = 0;

    public GameManager(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    // =======================
    // JUGADORES
    // =======================
    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        players.add(uuid);
        alivePlayers.add(uuid);
    }

    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        players.remove(uuid);
        alivePlayers.remove(uuid);
        playerTeams.remove(uuid);
    }

    public void leave(Player player) {
        removePlayer(player);
        // Podrías agregar teleport al lobby si quieres
    }

    public boolean isPlaying(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean isAlive(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    // =======================
    // KILLS, VICTORIAS Y COINS
    // =======================
    public void addKill(Player player) {
        UUID uuid = player.getUniqueId();
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
        totalKills.put(uuid, totalKills.getOrDefault(uuid, 0) + 1);
    }

    public int getKills(Player player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
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

    // =======================
    // EQUIPOS
    // =======================
    public void setTeam(Player player, GameTeam team) {
        playerTeams.put(player.getUniqueId(), team);
    }

    public GameTeam getTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    // =======================
    // ARENAS
    // =======================
    public void createArena(Player player, String name) {
        if (!arenas.containsKey(name)) {
            arenas.put(name, new Arena(name));
        }
    }

    public void editArena(Player player, String name) {
        currentArena = arenas.get(name);
    }

    public void removeArena(String name) {
        arenas.remove(name);
    }

    public void setTeams(Player player, int count) {
        if (currentArena != null) currentArena.setTeamCount(count);
    }

    public void setSpawn(Player player, GameTeam team, Location location) {
        if (currentArena != null) currentArena.setSpawn(team, location);
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    public String getMapName() {
        return currentArena != null ? currentArena.getName() : "Ninguna";
    }

    // =======================
    // TIEMPO
    // =======================
    public void setGameTime(int seconds) {
        this.gameTime = seconds;
    }

    public int getGameTime() {
        return gameTime;
    }

    // =======================
    // ESTADO DEL JUEGO
    // =======================
    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    // =======================
    // INICIO Y FIN DEL JUEGO
    // =======================
    public void startGame() {
        if (currentArena == null) return;
        setState(GameState.PLAYING);
        alivePlayers.addAll(players);
        kills.clear();
        gameTime = 0;
        // Puedes agregar lógica de teleporte a spawns
    }

    public void endGame() {
        setState(GameState.FINISHED);
        // Dar coins a todos los jugadores vivos
        for (UUID uuid : alivePlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) addCoins(p, 50); // Ejemplo
        }
        alivePlayers.clear();
        playerTeams.clear();
    }

    public void eliminate(Player shooter, Player victim) {
        UUID uuid = victim.getUniqueId();
        if (!alivePlayers.contains(uuid)) return;

        alivePlayers.remove(uuid);
        addKill(shooter);
        addCoins(shooter, 10); // coins por eliminar

        // Si quedan jugadores de un solo equipo, terminar ronda
        Set<GameTeam> remainingTeams = new HashSet<>();
        for (UUID alive : alivePlayers) {
            remainingTeams.add(playerTeams.get(alive));
        }
        if (remainingTeams.size() <= 1) {
            endGame();
        }
    }
}
