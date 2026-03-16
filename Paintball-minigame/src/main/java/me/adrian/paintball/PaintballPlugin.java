package me.adrian.paintball;

import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.listener.PaintballListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Inicializar GameManager
        gameManager = new GameManager(this);

        // Registrar comandos
        this.getCommand("paintball").setExecutor(new PaintballCommand(gameManager));
        this.getCommand("pb").setExecutor(new PaintballAdminCommand(gameManager));
        this.getCommand("pa").setExecutor(new PaintballAdminCommand(gameManager));

        // Registrar listeners
        getServer().getPluginManager().registerEvents(new PaintballListener(gameManager), this);

        // Mensaje de inicio
        getLogger().info("§aPaintball Plugin habilitado!");
    }

    @Override
    public void onDisable() {
        // Mensaje de apagado
        getLogger().info("§cPaintball Plugin deshabilitado!");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
