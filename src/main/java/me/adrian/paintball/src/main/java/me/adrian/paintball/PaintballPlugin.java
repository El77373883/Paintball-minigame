package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.scoreboard.ScoreboardTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;
    private ScoreboardTask scoreboardTask;

    @Override
    public void onEnable() {
        // Inicializar GameManager
        this.gameManager = new GameManager();

        // Crear arenas de ejemplo
        GameManager.Arena arena1 = new GameManager.Arena("Desert");
        arena1.addSpawn(new Location(Bukkit.getWorld("world"), 100, 65, 100));
        arena1.addSpawn(new Location(Bukkit.getWorld("world"), 105, 65, 100));

        GameManager.Arena arena2 = new GameManager.Arena("Forest");
        arena2.addSpawn(new Location(Bukkit.getWorld("world"), 200, 65, 200));
        arena2.addSpawn(new Location(Bukkit.getWorld("world"), 205, 65, 200));

        // Agregar arenas al GameManager
        gameManager.addArena(arena1);
        gameManager.addArena(arena2);

        // Elegir arena inicial
        gameManager.setCurrentArena("Desert");

        // Iniciar scoreboard que se actualiza cada segundo
        this.scoreboardTask = new ScoreboardTask(gameManager);
        this.scoreboardTask.runTaskTimer(this, 0, 20L);

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PaintballEvents(this), this);

        // Registrar comando /paintball
        this.getCommand("paintball").setExecutor(new PaintballCommand(this));

        // Mensaje en consola
        getServer().getConsoleSender().sendMessage("§aPaintball Plugin habilitado!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cPaintball Plugin deshabilitado!");
    }

    // Getter para GameManager
    public GameManager getGameManager() {
        return gameManager;
    }
}
