package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Snowball;

public class PaintballEvents implements Listener {

    private final PaintballPlugin plugin;

    public PaintballEvents(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball)) return;
        if (!(e.getHitEntity() instanceof Player)) return;

        Player target = (Player) e.getHitEntity();
        Player shooter = null;

        if (e.getEntity().getShooter() instanceof Player) {
            shooter = (Player) e.getEntity().getShooter();
        }

        GameManager gm = plugin.getGameManager();

        if (!gm.isPlaying(target) || !gm.isAlive(target)) return;

        gm.eliminate(target, shooter);

        // Mensaje agregado prefijo
        if (shooter != null) {
            Bukkit.broadcastMessage("§6[Paintball] §c" + target.getName() + " fue eliminado por " + shooter.getName() + "!");
        } else {
            Bukkit.broadcastMessage("§6[Paintball] §c" + target.getName() + " fue eliminado!");
        }
    }
}
