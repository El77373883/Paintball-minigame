package me.adrian.paintball;

import org.bukkit.plugin.java.JavaPlugin;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.listener.PaintballListener;
import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.command.PaintballAdminCommand;

public class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Inicializar el GameManager
        this.gameManager = new GameManager(this);

        // Registrar listener
        getServer().getPluginManager().registerEvents(new PaintballListener(gameManager), this);

        // Registrar comandos
        this.getCommand("paintball").setExecutor(new PaintballCommand(this));
        this.getCommand("paintballadmin").setExecutor(new PaintballAdminCommand(this));

        getLogger().info("PaintballPlugin habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin deshabilitado.");
    }

    // Getter para GameManager
    public GameManager getGameManager() {
        return gameManager;
    }
}
