package me.adrian.paintball;

import org.bukkit.plugin.java.JavaPlugin;
import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.events.PaintballEvents;

public class PaintballPlugin extends JavaPlugin {

    private static PaintballPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Registrar comandos
        this.getCommand("paintball").setExecutor(new PaintballCommand());
        this.getCommand("paintballadmin").setExecutor(new PaintballAdminCommand());

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PaintballEvents(), this);

        getLogger().info("PaintballPlugin habilitado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin deshabilitado!");
    }

    public static PaintballPlugin getInstance() {
        return instance;
    }
}
