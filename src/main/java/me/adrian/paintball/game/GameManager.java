package me.adrian.paintball.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    // Jugadores
    private final Set<UUID> players = new HashSet<>();
    private final Set<UUID> alivePlayers = new HashSet<>();

    // Estadísticas
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalWins = new HashMap<>();

    // Equipos
    private final Map<UUID, GameTeam> playerTeams = new HashMap<>();

    // Estado y tiempo
    private GameState state = GameState.WAITING;
    private int gameTime = 0;

    // Lobby
    private Location lobbySpawn;

    // Arenas
    private final List<Arena> arenas = new ArrayList<>();
    private Arena currentArena;
    private String mapName = "Lobby";

    // -------------------- CLASE ARENA --------------------
    public static class Arena {
        private final String name;
        private final List<Location> spawns = new ArrayList<>();

        public Arena(String name) { this.name = name; }

        public String getName() { return name; }

        public List<Location> getSpawns() { return spawns; }

        public void addSpawn(Location loc) { spawns.add(loc); }
    }

    public void addArena(Arena arena) { arenas.add(arena); }

    public Arena getCurrentArena() { return currentArena; }

    public void setCurrentArena(String name) {
        for (Arena arena : arenas) {
            if (arena.getName().equalsIgnoreCase(name)) {
                currentArena = arena;
                mapName = arena.getName();
                break;
            }
        }
    }

    // -------------------- GETTERS / SETTERS --------------------
    public GameState getState() { return state; }

    public int getGameTime() { return gameTime; }

    public void setGameTime(int time) { this.gameTime = time; } // ✅ Agregado

    public String getMapName() { return mapName; }

    public int getAliveCount() { return alivePlayers.size(); }

    public GameTeam getTeam(Player player) { return playerTeams.get(player.getUniqueId()); }

    public boolean isPlaying(Player player) { return players.contains(player.getUniqueId()); }

    public boolean isAlive(Player player) { return alivePlayers.contains(player.getUniqueId()); }

    // -------------------- JUGAR / SALIR --------------------
    public boolean join(Player player) {
        if (state == GameState.INGAME || state == GameState.ENDING) return false;
        if (players.contains(player.getUniqueId())) return false;

        players.add(player.getUniqueId());
        alivePlayers.add(player.getUniqueId());
        kills.put(player.getUniqueId(), 0);
        totalKills.putIfAbsent(player.getUniqueId(), 0);
        totalWins.putIfAbsent(player.getUniqueId(), 0);

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0);
        player.setFoodLevel(20);

        if (lobbySpawn != null) player.teleport(lobbySpawn);
        giveGun(player);

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        checkStart();
        return true;
    }

    public boolean leave(Player player) {
        boolean removed = players.remove(player.getUniqueId());
        alivePlayers.remove(player.getUniqueId());
        kills.remove(player.getUniqueId());
        playerTeams.remove(player.getUniqueId());

        player.getInventory().clear();
        if (lobbySpawn != null) player.teleport(lobbySpawn);

        checkWin();
        return removed;
    }

    // -------------------- ELIMINAR / GANAR --------------------
    public void eliminate(Player player, Player killer) {
        UUID uuid = player.getUniqueId();
        if (!alivePlayers.contains(uuid)) return;

        alivePlayers.remove(uuid);

        if (killer != null) addKill(killer);

        player.getInventory().clear();
        player.setHealth(20.0);
        if (lobbySpawn != null) player.teleport(lobbySpawn);

        if (killer != null) {
            Bukkit.broadcastMessage("§c" + player.getName() + " fue eliminado por " + killer.getName() + "!");
        } else {
            Bukkit.broadcastMessage("§c" + player.getName() + " fue eliminado!");
        }

        checkWin();
    }

    public void checkWin() {
        if (state != GameState.INGAME) return;

        if (alivePlayers.size() == 1) {
            UUID winnerUUID = alivePlayers.iterator().next();
            Player winner = Bukkit.getPlayer(winnerUUID);
            if (winner != null) {
                addWin(winner);
                Bukkit.broadcastMessage("§6" + winner.getName() + " ganó la partida de Paintball!");
            }
            stopGame();
        } else if (alivePlayers.isEmpty()) {
            Bukkit.broadcastMessage("§cNo hay ganadores esta ronda.");
            stopGame();
        }
    }

    // -------------------- INICIAR / DETENER --------------------
    public void startGame() {
        if (players.size() < 2) return;
        if (currentArena == null || currentArena.getSpawns().size() < players.size()) return;

        state = GameState.INGAME;
        alivePlayers.clear();
        assignTeams();
        gameTime = 0;

        List<Location> shuffledSpawns = new ArrayList<>(currentArena.getSpawns());
        Collections.shuffle(shuffledSpawns);

        int index = 0;
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            alivePlayers.add(uuid);
            kills.put(uuid, 0);

            player.getInventory().clear();
            player.teleport(shuffledSpawns.get(index));
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            giveGun(player);
            index++;
        }

        Bukkit.broadcastMessage("§a¡Partida de Paintball iniciada en arena: " + mapName + "!");
    }

    public void stopGame() {
        state = GameState.WAITING;

        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            player.getInventory().clear();
            player.setHealth(20.0);
            if (lobbySpawn != null) player.teleport(lobbySpawn);
        }

        alivePlayers.clear();
        players.clear();
        kills.clear();
        playerTeams.clear();
        gameTime = 0;

        Bukkit.broadcastMessage("§ePartida de Paintball finalizada.");
    }

    // -------------------- ARMAS --------------------
    public void giveGun(Player player) {
        ItemStack snowballs = new ItemStack(Material.SNOWBALL, 16);
        player.getInventory().addItem(snowballs);
    }

    // -------------------- EQUIPOS --------------------
    public void assignTeams() {
        List<UUID> playerList = new ArrayList<>(players);
        Collections.shuffle(playerList);
        GameTeam[] teams = GameTeam.values();
        playerTeams.clear();

        for (int i = 0; i < playerList.size(); i++) {
            playerTeams.put(playerList.get(i), teams[i % teams.length]);
        }
    }

    public int getAliveTeamCount(GameTeam team) {
        int count = 0;
        for (UUID uuid : alivePlayers) {
            if (playerTeams.get(uuid) == team) count++;
        }
        return count;
    }

    public Set<UUID> getAlivePlayers() { return alivePlayers; }

    // -------------------- ESTADÍSTICAS --------------------
    public void addKill(Player player) {
        UUID uuid = player.getUniqueId();
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
        totalKills.put(uuid, totalKills.getOrDefault(uuid, 0) + 1);
    }

    public int getKills(Player player) { return kills.getOrDefault(player.getUniqueId(), 0); }

    public int getTotalKills(Player player) { return totalKills.getOrDefault(player.getUniqueId(), 0); }

    public void addWin(Player player) {
        UUID uuid = player.getUniqueId();
        totalWins.put(uuid, totalWins.getOrDefault(uuid, 0) + 1);
    }

    public int getTotalWins(Player player) { return totalWins.getOrDefault(player.getUniqueId(), 0); }

    // -------------------- LOBBY / SPAWNS --------------------
    public void setLobbySpawn(Location loc) { this.lobbySpawn = loc; }

    public void addArenaSpawn(Location loc) {
        if (currentArena != null) currentArena.addSpawn(loc);
    }

    // -------------------- INICIO AUTOMÁTICO (OPCIONAL) --------------------
    private void checkStart() {
        // lógica automática de inicio si quieres
    }
}
