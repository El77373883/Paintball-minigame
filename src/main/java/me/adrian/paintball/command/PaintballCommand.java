package me.adrian.paintball.command;

import me.adrian.paintball.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCommand implements CommandExecutor {

    private final GameManager gameManager;

    public PaintballCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Use: /paintball <join|leave|start|stop|setlobby|addspawn>");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join" -> {
                if (gameManager.join(player)) {
                    player.sendMessage(ChatColor.GREEN + "You joined the paintball game.");
                } else {
                    player.sendMessage(ChatColor.RED + "You could not join the game.");
                }
            }

            case "leave" -> {
                if (gameManager.leave(player)) {
                    player.sendMessage(ChatColor.YELLOW + "You left the paintball game.");
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in the game.");
                }
            }

            case "start" -> {
                if (!player.hasPermission("paintball.admin")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission.");
                    return true;
                }

                gameManager.startGame();
            }

            case "stop" -> {
                if (!player.hasPermission("paintball.admin")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission.");
                    return true;
                }

                gameManager.stopGame();
            }

            case "setlobby" -> {
                if (!player.hasPermission("paintball.admin")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission.");
                    return true;
                }

                Location loc = player.getLocation();
                gameManager.setLobbySpawn(loc);
                player.sendMessage(ChatColor.GREEN + "Lobby spawn set.");
            }

            case "addspawn" -> {
                if (!player.hasPermission("paintball.admin")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission.");
                    return true;
                }

                Location loc = player.getLocation();
                gameManager.addArenaSpawn(loc);
                player.sendMessage(ChatColor.GREEN + "Arena spawn added.");
            }

            default -> player.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }

        return true;
    }
}
