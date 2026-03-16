package me.adrian.paintball;

import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.events.PaintballEvents;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.gui.PaintballPanel;
import me.adrian.paintball.gui.PaintballShop;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    private static PaintballPlugin instance;
    private GameManager gameManager;
    private PaintballPanel panel;
    private PaintballShop shop;

    @Override
    public void onEnable() {
        instance = this;

        // Inicializamos GameManager y le pasamos la instancia del plugin
        this.gameManager = new GameManager(this);

        // Inicializamos GUI
        this.panel = new PaintballPanel(this);
        this.shop = new PaintballShop(this);

        // Registrar comandos
        this.getCommand("pa").setExecutor(new PaintballAdminCommand(this));
        this.getCommand("paintball").setExecutor(new PaintballCommand(this));

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PaintballEvents(this), this);

        // Mensaje de activación
        getLogger().info("PaintballPlugin activado correctamente!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintballPlugin desactivado.");
    }

    // Getter de instancia del plugin
    public static PaintballPlugin getInstance() {
        return instance;
    }

    // Getter del GameManager
    public GameManager getGameManager() {
        return gameManager;
    }

    // Getter del panel
    public PaintballPanel getPanel() {
        return panel;
    }

    // Getter de la tienda
    public PaintballShop getShop() {
        return shop;
    }
}
