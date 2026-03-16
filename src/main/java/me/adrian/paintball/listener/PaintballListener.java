package me.adrian.paintball.listener;

import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import me.adrian.paintball.game.GameManager;

public class PaintballListener implements Listener {

    private final GameManager gameManager;

    public PaintballListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void eliminatePlayer(Player shooter, Player eliminated) {
        gameManager.eliminate(shooter, eliminated);

        Location loc = eliminated.getLocation();
        var world = loc.getWorld();

        if (world != null) {
            world.spawnParticle(Particle.CRIT_MAGIC, loc, 30, 0.5, 0.5, 0.5, 0.1);
            world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
            world.strikeLightningEffect(loc);
        }
    }

    // Aquí van otros eventos si los tienes
}
