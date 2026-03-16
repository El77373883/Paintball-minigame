package me.adrian.paintball.scoreboard;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardTask extends BukkitRunnable {

    private final GameManager gm;

    public ScoreboardTask(GameManager gm) {
        this.gm = gm;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = player.getScoreboard();
            if (board == null) board = Bukkit.getScoreboardManager().getNewScoreboard();

            Objective obj = board.getObjective("paintball");
            if (obj == null) {
                obj = board.registerNewObjective("paintball", "dummy", "§6[Paintball] §fMinigame");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            Score map = obj.getScore("§fMapa: §a" + gm.getMapName());
            Score time = obj.getScore("§fTiempo: §a" + gm.getGameTime() + "s");
            GameTeam team = gm.getTeam(player);
            String teamName = (team != null) ? team.name() : "Sin equipo";
            Score teamScore = obj.getScore("§fEquipo: §a" + teamName);
            Score kills = obj.getScore("§fKills: §a" + gm.getKills(player));
            Score wins = obj.getScore("§fVictorias: §a" + gm.getTotalWins(player));

            // Limpiar para que no duplique líneas
            for (String entry : board.getEntries()) {
                if (!entry.equals(obj.getDisplayName())) board.resetScores(entry);
            }

            player.setScoreboard(board);
        }
    }
}
