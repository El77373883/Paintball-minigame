package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.scoreboard.ScoreboardTask;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;
    private ScoreboardTask scoreboardTask;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager();

        // Inicia el scoreboard que se actualiza cada segundo (20 ticks = 1 segundo)
        this.scoreboardTask = new ScoreboardTask(gameManager);
        this.scoreboardTask.runTaskTimer(this, 0, 20L);

        getServer().getConsoleSender().sendMessage("§aPaintball Plugin habilitado!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cPaintball Plugin deshabilitado!");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
