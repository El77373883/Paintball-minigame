package me.adrian.paintball.command;

import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.events.PaintballEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private static PaintballPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Guardar config por defecto
        saveDefaultConfig();

        // Registrar comandos
        getCommand("paintball").setExecutor(new PaintballCommand());
        getCommand("paintballadmin").setExecutor(new PaintballAdminCommand());

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PaintballEvents(), this);

        getLogger().info("PaintballPlugin habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin deshabilitado.");
    }

    // Método para acceder desde otras clases
    public static PaintballPlugin getInstance() {
        return instance;
    }
}
