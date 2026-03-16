package me.adrian.paintball;

import me.adrian.paintball.command.PaintballCommand;
import me.adrian.paintball.command.PaintballAdminCommand;
import me.adrian.paintball.events.PaintballEvents;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.gui.PaintballPanel;
import me.adrian.paintball.gui.PaintballShop;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballPlugin extends JavaPlugin {

    // Instancia estática para poder acceder al plugin desde cualquier clase
    private static PaintballPlugin instance;

    // GameManager controla todo lo relacionado con los juegos y arenas
    private GameManager gameManager;

    // GUI del panel y tienda
    private PaintballPanel panel;
    private PaintballShop shop;

    @Override
    public void onEnable() {
        // Guardamos la instancia del plugin
        instance = this;

        // Inicializamos GameManager y le pasamos la instancia del plugin
        this.gameManager = new GameManager(this);

        // Inicializamos GUI
        this.panel = new PaintballPanel(this);
        this.shop = new PaintballShop(this);

        // Registrar comandos
        if (this.getCommand("pa") != null) {
            this.getCommand("pa").setExecutor(new PaintballAdminCommand(this));
        }
        if (this.getCommand("paintball") != null) {
            this.getCommand("paintball").setExecutor(new PaintballCommand(this));
        }

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new PaintballEvents(this), this);

        // Mensaje de activación
        getLogger().info("PaintballPlugin activado correctamente!");
    }

    @Override
    public void onDisable() {
        // Mensaje de desactivación
        getLogger().info("PaintballPlugin desactivado correctamente.");
    }

    // ------------------- GETTERS -------------------

    // Retorna la instancia del plugin
    public static PaintballPlugin getInstance() {
        return instance;
    }

    // Retorna el GameManager
    public GameManager getGameManager() {
        return gameManager;
    }

    // Retorna el panel
    public PaintballPanel getPanel() {
        return panel;
    }

    // Retorna la tienda
    public PaintballShop getShop() {
        return shop;
    }
}
