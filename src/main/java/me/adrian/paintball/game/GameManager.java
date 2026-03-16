package me.adrian.paintball.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    private final Set<UUID> players = new HashSet<>();
    private final Set<UUID> alivePlayers = new HashSet<>();
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalWins = new HashMap<>();
    private final Map<UUID, GameTeam> playerTeams = new HashMap<>();

    private GameState state = GameState.WAITING;
    private Location lobbySpawn;
    private final List<Location> arenaSpawns = new ArrayList<>();
    private String mapName = "Lobby";
    private int gameTime = 0;

    // --------------------------- JOIN / LEAVE ---------------------------
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

        // Inicializar scoreboard vacío, ScoreboardTask lo actualizará automáticamente
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

    // --------------------------- ELIMINATE / CHECKWIN ---------------------------
    public void eliminate(Player player, Player killer) {
        UUID uuid = player.getUniqueId();
        if (!alivePlayers.contains(uuid)) return;

        alivePlayers.remove(uuid);

        if (killer != null) addKill(killer);

        player.getInventory().clear();
        player.setHealth(20.0);
        if (lobbySpawn != null) player.teleport(lobbySpawn);

        if (killer != null) {
            Bukkit.broadcastMessage("§c" + player.getName() + " was eliminated by " + killer.getName() + "!");
        } else {
            Bukkit.broadcastMessage("§c" + player.getName() + " was eliminated!");
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
                Bukkit.broadcastMessage("§6" + winner.getName() + " won the paintball game!");
            }
            stopGame();
        } else if (alivePlayers.isEmpty()) {
            Bukkit.broadcastMessage("§cNo winners this round.");
            stopGame();
        }
    }

    // --------------------------- START / STOP GAME ---------------------------
    public void startGame() {
        if (players.size() < 2) {
            Bukkit.broadcastMessage("§cNeed at least 2 players to start.");
            return;
        }
        if (arenaSpawns.size() < players.size()) {
            Bukkit.broadcastMessage("§cNot enough arena spawns set.");
            return;
        }

        state = GameState.INGAME;
        alivePlayers.clear();
        assignTeams();
        gameTime = 0;

        List<Location> shuffledSpawns = new ArrayList<>(arenaSpawns);
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

        Bukkit.broadcastMessage("§aPaintball game started!");
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

        Bukkit.broadcastMessage("§ePaintball game ended.");
    }

    // --------------------------- GUN ---------------------------
    public void giveGun(Player player) {
        ItemStack snowballs = new ItemStack(Material.SNOWBALL, 16);
        player.getInventory().addItem(snowballs);
    }

    // --------------------------- TEAMS ---------------------------
    public void assignTeams() {
        List<UUID> playerList = new ArrayList<>(players);
        Collections.shuffle(playerList);
        GameTeam[] teams = GameTeam.values();
        playerTeams.clear();

        for (int i = 0; i < playerList.size(); i++) {
            playerTeams.put(playerList.get(i), teams[i % teams.length]);
        }
    }

    public GameTeam getTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    public int getAliveTeamCount(GameTeam team) {
        int count = 0;
        for (UUID uuid : alivePlayers) {
            if (playerTeams.get(uuid) == team) count++;
        }
        return count;
    }

    // --------------------------- STATS ---------------------------
    public void addKill(Player player) {
        UUID uuid = player.getUniqueId();
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
        totalKills.put(uuid, totalKills.getOrDefault(uuid, 0) + 1);
    }

    public int getKills(Player player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
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

    // --------------------------- MAP / TIME ---------------------------
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int time) {
        this.gameTime = time;
    }

    // --------------------------- OTHERS ---------------------------
    public int getAliveCount() {
        return alivePlayers.size();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void setLobbySpawn(Location loc) {
        this.lobbySpawn = loc;
    }

    public void addArenaSpawn(Location loc) {
        arenaSpawns.add(loc);
    }

    public List<Location> getArenaSpawns() {
        return arenaSpawns;
    }

    public Set<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    private void checkStart() {
        // Aquí puedes poner lógica para empezar automáticamente si hay X jugadores
    }
}
