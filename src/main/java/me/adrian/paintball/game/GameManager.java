package me.adrian.paintball.game;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private final PaintballPlugin plugin;

    // Estados y equipos
    private GameState state = GameState.WAITING;
    private final Map<Player, GameTeam> playerTeams = new HashMap<>();
    private final Map<Player, Integer> playerKills = new HashMap<>();
    private final Map<Player, Integer> playerCoins = new HashMap<>();

    // Arenas
    private final Map<String, Arena> arenas = new HashMap<>();
    private Arena currentArena;

    // Configuraciones temporales para edición de arena
    private final Map<Player, Location> selectionPoints = new HashMap<>();

    public GameManager(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    // ================== ARENAS ==================
    public void createArena(Player admin, String name) {
        if (selectionPoints.containsKey(admin)) {
            Arena arena = new Arena(name, selectionPoints.get(admin));
            arenas.put(name, arena);
            admin.sendMessage("§aArena '" + name + "' creada correctamente!");
        } else {
            admin.sendMessage("§cDebes seleccionar el área primero con /pb select!");
        }
    }

    public void editArena(Player admin, String name) {
        Arena arena = arenas.get(name);
        if (arena != null) {
            currentArena = arena;
            admin.sendMessage("§aEditando arena: " + name);
        } else {
            admin.sendMessage("§cArena no encontrada.");
        }
    }

    public void removeArena(Player admin, String name) {
        if (arenas.remove(name) != null) {
            admin.sendMessage("§aArena '" + name + "' eliminada.");
        } else {
            admin.sendMessage("§cArena no encontrada.");
        }
    }

    public void setTeams(Player admin, int numTeams) {
        if (currentArena != null) {
            currentArena.setTeamCount(numTeams);
            admin.sendMessage("§aNúmero de equipos de la arena actualizado a " + numTeams);
        }
    }

    public void setSpawn(Player admin, GameTeam team, Location loc) {
        if (currentArena != null) {
            currentArena.setSpawn(team, loc);
            admin.sendMessage("§aSpawn de " + team.name() + " actualizado!");
        }
    }

    public void setCurrentArena(String name) {
        this.currentArena = arenas.get(name);
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    // ================== JUGADORES ==================
    public void addPlayer(Player player) {
        if (!playerTeams.containsKey(player)) {
            playerTeams.put(player, null); // Team asignado luego
            playerKills.put(player, 0);
            playerCoins.put(player, 0);
            player.sendMessage("§aTe uniste al Paintball!");
        }
    }

    public void removePlayer(Player player) {
        playerTeams.remove(player);
        playerKills.remove(player);
        playerCoins.remove(player);
        player.sendMessage("§cSaliste del Paintball!");
    }

    public void startGame() {
        if (state == GameState.WAITING && currentArena != null) {
            state = GameState.INGAME;
            Bukkit.broadcastMessage("§aLa partida ha comenzado en " + currentArena.getName());
            assignTeams();
            startTimer();
        }
    }

    // ================== GAMEPLAY ==================
    private void assignTeams() {
        List<Player> players = new ArrayList<>(playerTeams.keySet());
        Collections.shuffle(players);
        GameTeam[] teams = GameTeam.values();

        int i = 0;
        for (Player p : players) {
            GameTeam team = teams[i % currentArena.getTeamCount()];
            playerTeams.put(p, team);
            i++;
            p.sendMessage("§aHas sido asignado al equipo: " + team.name());
        }
    }

    private void startTimer() {
        new BukkitRunnable() {
            int time = 300; // ejemplo 5 minutos

            @Override
            public void run() {
                if (state != GameState.INGAME) {
                    cancel();
                    return;
                }

                if (time <= 0) {
                    endGame();
                    cancel();
                }

                time--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void endGame() {
        state = GameState.END;
        Bukkit.broadcastMessage("§aLa partida ha terminado!");
        // Recompensar jugadores
        for (Player p : playerKills.keySet()) {
            int coins = playerKills.get(p) * 5; // 5 coins por kill
            playerCoins.put(p, playerCoins.getOrDefault(p, 0) + coins);
            p.sendMessage("§aGanaste " + coins + " coins!");
        }
        playerTeams.clear();
        playerKills.clear();
    }

    public void eliminate(Player shooter, Player victim) {
        if (state != GameState.INGAME) return;

        playerKills.put(shooter, playerKills.getOrDefault(shooter, 0) + 1);
        Bukkit.getServer().getWorld(victim.getWorld().getName()).strikeLightningEffect(victim.getLocation()); // rayo efecto

        removePlayer(victim);
    }

    // ================== GETTERS ==================
    public GameTeam getTeam(Player player) {
        return playerTeams.get(player);
    }

    public int getKills(Player player) {
        return playerKills.getOrDefault(player, 0);
    }

    public int getTotalKills(Player player) {
        return getKills(player);
    }

    public int getCoins(Player player) {
        return playerCoins.getOrDefault(player, 0);
    }

    public GameState getState() {
        return state;
    }

}
