import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Sound;

public void eliminatePlayer(Player shooter, Player eliminated) {
    // Lógica que marca al jugador como eliminado en tu GameManager
    gameManager.eliminate(shooter, eliminated);

    // Ubicación del jugador eliminado
    Location loc = eliminated.getLocation();
    World world = loc.getWorld();

    if (world != null) {
        // Efecto visual de trueno
        world.spawnParticle(Particle.CRIT_MAGIC, loc, 30, 0.5, 0.5, 0.5, 0.1);

        // Efecto de sonido de trueno
        world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);

        // Opcional: rayo visual (solo visual, no daña)
        world.strikeLightningEffect(loc);
    }
}
