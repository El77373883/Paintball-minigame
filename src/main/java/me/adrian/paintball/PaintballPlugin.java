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
        this.gameManager = new GameManager(this);

        getServer().getPluginManager().registerEvents(new PaintballListener(gameManager), this);

        this.getCommand("paintball").setExecutor(new PaintballCommand(this));
        this.getCommand("paintballadmin").setExecutor(new PaintballAdminCommand(this));

        getLogger().info("PaintballPlugin habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin deshabilitado.");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
