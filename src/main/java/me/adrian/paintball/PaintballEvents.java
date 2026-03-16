package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PaintballEvents implements Listener {

    private final PaintballPlugin plugin;
    private final GameManager gameManager;

    public PaintballEvents(PaintballPlugin plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    // Eliminar jugador si le pega una bola de nieve
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball)) return;
        if (!(e.getHitEntity() instanceof Player)) return;

        Player victim = (Player) e.getHitEntity();
        if (!gameManager.getAlivePlayers().contains(victim.getUniqueId())) return;

        Player shooter = null;
        if (e.getEntity().getShooter() instanceof Player) {
            shooter = (Player) e.getEntity().getShooter();
        }

        gameManager.eliminate(victim, shooter);
    }

    // Evitar que jugadores desaparezcan sin contar kills
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        gameManager.leave(e.getPlayer());
    }

}
