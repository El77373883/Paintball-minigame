package me.adrian.paintball.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameManager.GameState;

public class PaintballListener implements Listener {

    private final GameManager gameManager;

    public PaintballListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    // Método para eliminar jugadores con efectos
    public void eliminatePlayer(Player shooter, Player eliminated) {
        gameManager.eliminate(shooter, eliminated);

        Location loc = eliminated.getLocation();
        var world = loc.getWorld();

        if (world != null) {
            // Efecto de partículas de magia
            world.spawnParticle(Particle.CRIT_MAGIC, loc, 30, 0.5, 0.5, 0.5, 0.1);
            // Sonido de trueno
            world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
            // Efecto visual de rayo sin dañar
            world.strikeLightningEffect(loc);
        }
    }

    // Evento de disparo: detecta cuando un jugador daña a otro
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player shooter = (Player) event.getDamager();

        // Solo funciona si el juego está en curso
        if (gameManager.getState() != GameState.IN_GAME) return;

        // Solo si ambos jugadores están activos en el juego
        if (!gameManager.isPlaying(shooter) || !gameManager.isPlaying(victim)) return;

        // Aquí eliminamos al jugador
        eliminatePlayer(shooter, victim);

        // Cancelamos el daño real para que no afecte la vida de Minecraft
        event.setCancelled(true);
    }

    // Puedes añadir más eventos aquí si quieres, como unión de juego, compras, etc.
}
