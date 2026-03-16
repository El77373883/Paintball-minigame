package me.adrian.paintball.game;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.*;

public class GameManager {

    private final PaintballPlugin plugin;

    // Estados
    private GameState state = GameState.WAITING;

    // Arenas
    private final Map<String, Arena> arenas = new HashMap<>();
    private Arena currentArena;

    // Jugadores
    private final Map<UUID, String> teams = new HashMap<>();
    private final Set<UUID> alivePlayers = new HashSet<>();

    // Kills y victorias
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalWins = new HashMap<>();

    // Coins
    private final Map<UUID, Integer> coins = new HashMap<>();

    // Tiempo de partida
    private int gameTime = 0;

    public GameManager(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    // --- Estado de la partida ---
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int time) {
        this.gameTime = time;
    }

    // --- Arenas ---
    public void addArena(Arena arena) {
        arenas.put(arena.getName(), arena);
    }

    public void removeArena(String name) {
        arenas.remove(name);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public void setCurrentArena(String name) {
        currentArena = arenas.get(name);
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    // --- Métodos para comandos de admin ---
    public void createArena(Player player, String name) {
        Arena arena = new Arena(name);
        addArena(arena);
        currentArena = arena;
        player.sendMessage("§aArena " + name + " creada y seleccionada.");
    }

    public void editArena(Player player, String name) {
        if (!arenas.containsKey(name)) {
            player.sendMessage("§cLa arena no existe!");
            return;
        }
        currentArena = arenas.get(name);
        player.sendMessage("§aEditando la arena " + name);
    }

    public void setTeams(Player player, int numTeams) {
        if (currentArena != null) {
            currentArena.setMaxTeams(numTeams);
            player.sendMessage("§aSe definieron " + numTeams + " equipos para la arena " + currentArena.getName());
        }
    }

    public void setSpawn(Player player, String team) {
        if (currentArena != null) {
            currentArena.setSpawn(team, player.getLocation());
            player.sendMessage("§aSpawn del equipo " + team + " establecido!");
        }
    }

    // --- Jugadores ---
    public void addPlayer(Player player) {
        alivePlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        alivePlayers.remove(player.getUniqueId());
        teams.remove(player.getUniqueId());
    }

    public boolean isPlaying(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    public boolean isAlive(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    public void setTeam(Player player, String team) {
        teams.put(player.getUniqueId(), team);
    }

    public String getTeam(Player player) {
        return teams.getOrDefault(player.getUniqueId(), "NONE");
    }

    // --- Kills y victorias ---
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
        addCoins(player, 10); // Da coins al ganar
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player.getUniqueId(), 0);
    }

    public int getTotalKills(Player player) {
        return totalKills.getOrDefault(player.getUniqueId(), 0);
    }

    // --- Coins ---
    public void addCoins(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        coins.put(uuid, coins.getOrDefault(uuid, 0) + amount);
        player.sendMessage("§aHas recibido §e" + amount + " coins§a!");
    }

    public int getCoins(Player player) {
        return coins.getOrDefault(player.getUniqueId(), 0);
    }

    // --- Gameplay ---
    public void startGame() {
        if (currentArena == null) return;
        state = GameState.PLAYING;
        alivePlayers.clear();
        alivePlayers.addAll(teams.keySet());
        gameTime = 0;
        Bukkit.broadcastMessage("§aLa partida ha comenzado!");
    }

    public void eliminate(Player victim, Player killer) {
        alivePlayers.remove(victim.getUniqueId());
        addKill(killer);

        // Rayo de muerte
        World world = victim.getWorld();
        world.strikeLightningEffect(victim.getLocation());

        // Mensaje
        victim.sendMessage("§cHas sido eliminado por " + killer.getName());
        killer.sendMessage("§aHas eliminado a " + victim.getName());

        // Check si hay ganador
        checkWin();
    }

    private void checkWin() {
        Map<String, Integer> teamCounts = new HashMap<>();
        for (UUID uuid : alivePlayers) {
            String team = teams.get(uuid);
            if (team == null) continue;
            teamCounts.put(team, teamCounts.getOrDefault(team, 0) + 1);
        }
        if (teamCounts.size() == 1) {
            String winningTeam = teamCounts.keySet().iterator().next();
            Bukkit.broadcastMessage("§6¡El equipo " + winningTeam + " ha ganado!");
            for (UUID uuid : teams.keySet()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null && getTeam(p).equals(winningTeam)) {
                    addWin(p);
                }
            }
            state = GameState.FINISHED;
        }
    }

    // --- Scoreboard ---
    public void startScoreboardTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == GameState.PLAYING) gameTime++;
                for (UUID uuid : teams.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        // Aquí iría tu scoreboard premium (puedes integrarlo después)
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // cada segundo
    }
}
