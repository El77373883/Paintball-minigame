package me.adrian.paintball.game;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private final PaintballPlugin plugin;

    // Estados de juego
    private GameState state = GameState.WAITING;

    // Mapas/Arenas
    private final Map<String, Arena> arenas = new HashMap<>();
    private Arena currentArena;

    // Jugadores vivos en la partida
    private final Set<UUID> alivePlayers = new HashSet<>();

    // Kills y victorias
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalWins = new HashMap<>();

    // Equipos por jugador
    private final Map<UUID, GameTeam> teams = new HashMap<>();

    // Tiempo de juego en segundos
    private int gameTime = 0;

    // Coins por jugador
    private final Map<UUID, Integer> coins = new HashMap<>();

    public GameManager(PaintballPlugin plugin) {
        this.plugin = plugin;
        startTimer();
    }

    // =======================
    // GETTERS & SETTERS
    // =======================
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setCurrentArena(String name) {
        this.currentArena = arenas.get(name);
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public String getMapName() {
        return currentArena != null ? currentArena.getName() : "N/A";
    }

    public int getGameTime() {
        return gameTime;
    }

    public GameTeam getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }

    public int getKills(Player player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
    }

    public int getTotalKills(Player player) {
        return totalKills.getOrDefault(player.getUniqueId(), 0);
    }

    public void addKill(Player player) {
        UUID uuid = player.getUniqueId();
        kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
        totalKills.put(uuid, totalKills.getOrDefault(uuid, 0) + 1);
    }

    public int getTotalWins(Player player) {
        return totalWins.getOrDefault(player.getUniqueId(), 0);
    }

    public void addWin(Player player) {
        UUID uuid = player.getUniqueId();
        totalWins.put(uuid, totalWins.getOrDefault(uuid, 0) + 1);
        addCoins(player, 50); // 50 coins por victoria
    }

    public void addCoins(Player player, int amount) {
        coins.put(player.getUniqueId(), coins.getOrDefault(player.getUniqueId(), 0) + amount);
    }

    public int getCoins(Player player) {
        return coins.getOrDefault(player.getUniqueId(), 0);
    }

    public boolean isAlive(Player player) {
        return alivePlayers.contains(player.getUniqueId());
    }

    public boolean isPlaying(Player player) {
        return teams.containsKey(player.getUniqueId());
    }

    // =======================
    // JUGADORES
    // =======================
    public void join(Player player, GameTeam team) {
        if (!isPlaying(player)) {
            teams.put(player.getUniqueId(), team);
            alivePlayers.add(player.getUniqueId());
            kills.put(player.getUniqueId(), 0);
            player.sendMessage("§aTe has unido al equipo " + team.name());
        }
    }

    public void leave(Player player) {
        teams.remove(player.getUniqueId());
        alivePlayers.remove(player.getUniqueId());
        kills.remove(player.getUniqueId());
        player.sendMessage("§cHas salido de la partida");
    }

    public void eliminate(Player killer, Player victim) {
        if (!isAlive(victim)) return;

        alivePlayers.remove(victim.getUniqueId());
        victim.sendMessage("§cHas sido eliminado!");
        killer.sendMessage("§aHas eliminado a " + victim.getName());
        addKill(killer);

        // Rayo de muerte
        victim.getWorld().strikeLightningEffect(victim.getLocation());

        checkWin();
    }

    // =======================
    // CHEQUEO DE VICTORIA
    // =======================
    private void checkWin() {
        Set<GameTeam> remainingTeams = new HashSet<>();
        for (UUID uuid : alivePlayers) {
            remainingTeams.add(teams.get(uuid));
        }

        if (remainingTeams.size() <= 1 && alivePlayers.size() > 0) {
            // Ganador
            alivePlayers.forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) addWin(p);
            });

            // Reiniciar partida
            resetGame();
        }
    }

    private void resetGame() {
        state = GameState.WAITING;
        alivePlayers.clear();
        kills.clear();
        teams.clear();
        gameTime = 0;
        Bukkit.broadcastMessage("§6La partida ha terminado! Se ha reiniciado la arena.");
    }

    // =======================
    // TIMER
    // =======================
    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == GameState.PLAYING) {
                    gameTime++;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // Cada segundo
    }

    // =======================
    // ARENAS
    // =======================
    public void addArena(Arena arena) {
        arenas.put(arena.getName(), arena);
    }

    public void removeArena(String name) {
        arenas.remove(name);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

}
