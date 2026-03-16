package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCommand implements CommandExecutor {

    private final PaintballPlugin plugin;
    private final GameManager gameManager;

    public PaintballCommand(PaintballPlugin plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§aUsa /paintball join | leave | start");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                if (gameManager.join(player)) {
                    player.sendMessage("§aTe uniste a la partida de Paintball!");
                } else {
                    player.sendMessage("§cNo puedes unirte ahora.");
                }
                break;

            case "leave":
                if (gameManager.leave(player)) {
                    player.sendMessage("§aSaliste de la partida de Paintball.");
                } else {
                    player.sendMessage("§cNo estabas en la partida.");
                }
                break;

            case "start":
                if (player.hasPermission("paintball.start")) {
                    gameManager.startGame();
                } else {
                    player.sendMessage("§cNo tienes permiso para iniciar la partida.");
                }
                break;

            default:
                player.sendMessage("§cComando inválido. Usa join | leave | start");
        }

        return true;
    }
}
