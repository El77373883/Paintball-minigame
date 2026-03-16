package me.adrian.paintball.listener;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class PaintballListener implements Listener {

    private final GameManager gameManager;

    public PaintballListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Player killer = dead.getKiller();

        // Solo ejecutar si el juego está en curso
        if (gameManager.getState() == GameState.IN_GAME) {
            if (killer != null) {
                gameManager.eliminate(dead, killer);
            } else {
                gameManager.eliminate(dead, null);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isPlaying(player)) {
            gameManager.leave(player);
        }
    }
}
