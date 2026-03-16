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
    private GameState state = GameState.WAITING;

    private Location lobbySpawn;
    private final List<Location> arenaSpawns = new ArrayList<>();

    public boolean join(Player player) {
        if (state == GameState.INGAME || state == GameState.ENDING) {
            return false;
        }

        if (players.contains(player.getUniqueId())) {
            return false;
        }

        players.add(player.getUniqueId());
        alivePlayers.add(player.getUniqueId());
        kills.put(player.getUniqueId(), 0);

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0);
        player.setFoodLevel(20);

        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        }

        giveGun(player);
        checkStart();
        return true;
    }

    public boolean leave(Player player) {
        boolean removed = players.remove(player.getUniqueId());
        alivePlayers.remove(player.getUniqueId());
        kills.remove(player.getUniqueId());

        player.getInventory().clear();
        checkWin();
        return removed;
    }

    public void eliminate(Player player, Player killer) {
        UUID uuid = player.getUniqueId();

        if (!alivePlayers.contains(uuid)) {
            return;
        }

        alivePlayers.remove(uuid);

        if (killer != null) {
            kills.put(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0) + 1);
        }

        player.getInventory().clear();
        player.setHealth(20.0);

        if (lobbySpawn != null) {
            player.teleport(lobbySpawn);
        }

        if (killer != null) {
            Bukkit.broadcastMessage("§c" + player.getName() + " was eliminated by " + killer.getName() + "!");
        } else {
            Bukkit.broadcastMessage("§c" + player.getName() + " was eliminated!");
        }

        checkWin();
    }

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

            if (lobbySpawn != null) {
                player.teleport(lobbySpawn);
            }
        }

        alivePlayers.clear();
        players.clear();
        kills.clear();

        Bukkit.broadcastMessage("§ePaintball game ended.");
    }

    public void checkStart() {
        if (state == GameState.WAITING && players.size() >= 2) {
            Bukkit.broadcastMessage("§eEnough players joined. Use /paintball start");
        }
    }

    public void checkWin() {
        if (state != GameState.INGAME) {
            return;
        }

        if (alivePlayers.size() == 1) {
            UUID winnerUUID = alivePlayers.iterator().next();
            Player winner = Bukkit.getPlayer(winnerUUID);

            if (winner != null) {
                Bukkit.broadcastMessage("§6" + winner.getName() + " won the paintball game!");
            }

            stopGame();
            return;
        }

        if (alivePlayers.isEmpty()) {
            Bukkit.broadcastMessage("§cNo winners this round.");
            stopGame();
        }
    }

    public void giveGun(Player player) {
        ItemStack snowballs = new ItemStack(Material.SNOWBALL, 16);
        player.getInventory().addItem(snowballs);
    }

    public boolean isPlaying(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean isAlive(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    public int getAliveCount() {
        return alivePlayers.size();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getKills(Player player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public GameState getState() {
        return state;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void addArenaSpawn(Location location) {
        arenaSpawns.add(location);
    }

    public List<Location> getArenaSpawns() {
        return arenaSpawns;
    }
}
