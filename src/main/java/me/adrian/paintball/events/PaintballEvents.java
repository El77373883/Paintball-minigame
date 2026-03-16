package me.adrian.paintball.events;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PaintballEvents implements Listener {

    private final PaintballPlugin plugin;

    public PaintballEvents() {
        this.plugin = PaintballPlugin.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("¡Bienvenido al Paintball!");
        plugin.getLogger().info(event.getPlayer().getName() + " se ha unido al servidor.");
    }
}
