package me.adrian.paintball;

import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.events.PaintballEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private static PaintballPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getCommand("paintball").setExecutor(new PaintballCommand());
        getCommand("paintballadmin").setExecutor(new PaintballAdminCommand());

        getServer().getPluginManager().registerEvents(new PaintballEvents(), this);

        getLogger().info("PaintballPlugin habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin deshabilitado.");
    }

    public static PaintballPlugin getInstance() {
        return instance;
    }
}
