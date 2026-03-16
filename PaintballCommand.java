package me.adrian.paintball;

import org.bukkit.plugin.java.JavaPlugin;

public final class PaintballPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PaintballMinigame enabled! Created by soyadrianyt001");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballMinigame disabled!");
    }
}
