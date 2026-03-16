package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCommand implements CommandExecutor {

    private final PaintballPlugin plugin;

    public PaintballCommand(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        GameManager gm = plugin.getGameManager();

        if (args.length == 0) {
            player.sendMessage("§aUsa /paintball join|leave|start");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                if (gm.join(player)) {
                    player.sendMessage("§aTe uniste al Paintball!");
                } else {
                    player.sendMessage("§cNo puedes unirte ahora.");
                }
                break;
            case "leave":
                if (gm.leave(player)) {
                    player.sendMessage("§aSaliste del Paintball!");
                } else {
                    player.sendMessage("§cNo estás en la partida.");
                }
                break;
            case "start":
                gm.startGame();
                break;
            default:
                player.sendMessage("§aUsa /paintball join|leave|start");
        }

        return true;
    }
}
