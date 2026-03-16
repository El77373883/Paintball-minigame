package me.adrian.paintball;

import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.listener.PaintballListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaintballPlugin extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager();

        if (getCommand("paintball") != null) {
            getCommand("paintball").setExecutor(new PaintballCommand(gameManager));
        }

        getServer().getPluginManager().registerEvents(new PaintballListener(gameManager), this);

        getLogger().info("PaintballMinigame enabled! Created by soyadrianyt001");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballMinigame disabled!");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
