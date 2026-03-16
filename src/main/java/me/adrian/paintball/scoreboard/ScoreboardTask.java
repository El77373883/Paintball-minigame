package me.adrian.paintball.scoreboard;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameState;
import me.adrian.paintball.game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class ScoreboardTask extends BukkitRunnable {

    private final GameManager gameManager;

    public ScoreboardTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        // Aumentar tiempo si el juego está en curso
        if (gameManager.getState() == GameState.INGAME) {
            gameManager.setGameTime(gameManager.getGameTime() + 1);
        } else {
            gameManager.setGameTime(0);
        }

        // Actualizar scoreboard de todos los jugadores online
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateBoard(player);
        }
    }

    private void updateBoard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        // ---------- TÍTULO PREMIUM ----------
        Objective obj = board.registerNewObjective("paintball", "dummy",
                ChatColor.DARK_GRAY + "▬▬▬▬▬▬\n" +
                ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball Minigame\n" +
                ChatColor.DARK_GRAY + "▬▬▬▬▬▬");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        // ---------- DATOS DEL JUGADOR ----------
        String time = formatTime(gameManager.getGameTime());
        if (gameManager.getGameTime() > 120) time = ChatColor.GREEN + time;
        else if (gameManager.getGameTime() > 60) time = ChatColor.YELLOW + time;
        else time = ChatColor.RED + time;

        String teamName = ChatColor.GRAY + "None";
        if (gameManager.getTeam(player) != null) {
            teamName = gameManager.getTeam(player).getColoredName();
        }

        obj.getScore(ChatColor.AQUA + "Mapa: " + ChatColor.WHITE + gameManager.getMapName()).setScore(15);
        obj.getScore(ChatColor.WHITE + "Nombre: " + ChatColor.YELLOW + player.getName()).setScore(14);
        obj.getScore(ChatColor.YELLOW + "Tiempo: " + ChatColor.WHITE + time).setScore(13);
        obj.getScore(ChatColor.YELLOW + "Equipo: " + teamName).setScore(12);

        obj.getScore(ChatColor.GRAY + " ").setScore(11); // separador

        // ---------- VIVOS POR EQUIPO ----------
        obj.getScore(ChatColor.YELLOW + "Vivos: " + ChatColor.WHITE + gameManager.getAliveCount()).setScore(10);
        obj.getScore(ChatColor.BLUE + "Azul: " + ChatColor.WHITE + getAliveNames(GameTeam.BLUE)).setScore(9);
        obj.getScore(ChatColor.RED + "Rojo: " + ChatColor.WHITE + getAliveNames(GameTeam.RED)).setScore(8);
        obj.getScore(ChatColor.GREEN + "Verde: " + ChatColor.WHITE + getAliveNames(GameTeam.GREEN)).setScore(7);
        obj.getScore(ChatColor.LIGHT_PURPLE + "Rosa: " + ChatColor.WHITE + getAliveNames(GameTeam.PINK)).setScore(6);

        obj.getScore(ChatColor.GRAY + " ").setScore(5); // separador

        // ---------- STATS DEL JUGADOR ----------
        obj.getScore(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + gameManager.getKills(player)).setScore(4);
        obj.getScore(ChatColor.GOLD + "Victorias: " + ChatColor.WHITE + gameManager.getTotalWins(player)).setScore(3);

        obj.getScore(ChatColor.DARK_GRAY + " ").setScore(2);
        obj.getScore(ChatColor.GOLD + "PaintballMinigame").setScore(1);

        player.setScoreboard(board);
    }

    // Obtener nombres de jugadores vivos por equipo con color
    private String getAliveNames(GameTeam team) {
        StringBuilder sb = new StringBuilder();
        for (UUID uuid : gameManager.getAlivePlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && gameManager.getTeam(p) == team) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(gameManager.getTeam(p).getColor()).append(p.getName());
            }
        }
        if (sb.length() == 0) return "-";
        return sb.toString();
    }

    // Formatear tiempo mm:ss
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
