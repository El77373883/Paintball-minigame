package me.adrian.paintball.listener;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PaintballListener implements Listener {

    private final GameManager gameManager;

    public PaintballListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (!(victim instanceof Player player)) {
            return;
        }

        if (!(damager instanceof Snowball snowball)) {
            return;
        }

        if (!(snowball.getShooter() instanceof Player shooter)) {
            return;
        }

        if (gameManager.getState() != GameState.INGAME) {
            return;
        }

        if (!gameManager.isPlaying(player) || !gameManager.isPlaying(shooter)) {
            return;
        }

        if (!gameManager.isAlive(player) || !gameManager.isAlive(shooter)) {
            return;
        }

        event.setCancelled(true);
        gameManager.eliminate(player, shooter);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (gameManager.isPlaying(event.getPlayer())) {
            gameManager.leave(event.getPlayer());
        }
    }
}
