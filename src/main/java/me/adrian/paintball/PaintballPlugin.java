import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.scoreboard.ScoreboardTask;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;
    private ScoreboardTask scoreboardTask;

    @Override
    public void onEnable() {
        // Inicializar GameManager
        gameManager = new GameManager();

        // Iniciar el scoreboard que se actualiza cada segundo (20 ticks)
        scoreboardTask = new ScoreboardTask(gameManager);
        scoreboardTask.runTaskTimer(this, 0, 20L); // 0 = empieza de inmediato, 20L = cada segundo

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
