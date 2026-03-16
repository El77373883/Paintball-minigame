package me.adrian.paintball;

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

        if (!(sender instanceof Player)) {
            sender.sendMessage("§6[Paintball] §cSolo jugadores pueden usar estos comandos");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§6[Paintball] §fUsa /paintball join, leave, start o stats");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                plugin.getGameManager().addPlayer(player);
                player.sendMessage("§6[Paintball] §aTe has unido a la partida!");
                break;

            case "leave":
                plugin.getGameManager().removePlayer(player);
                player.sendMessage("§6[Paintball] §cHas salido de la partida!");
                break;

            case "start":
                plugin.getGameManager().startGame();
                break;

            case "stats":
                int kills = plugin.getGameManager().getTotalKills(player);
                int wins = plugin.getGameManager().getTotalWins(player);
                player.sendMessage("§6[Paintball] §fKills: §a" + kills + " §fVictorias: §a" + wins);
                break;

            default:
                player.sendMessage("§6[Paintball] §cComando no válido. Usa /paintball join, leave, start o stats");
        }

        return true;
    }
}
