package me.adrian.paintball.command;

import org.bukkit.Bukkit;
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
            sender.sendMessage("[Paintball] Solo jugadores pueden usar estos comandos");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§6[Paintball] §fUsa /paintball help para ver los comandos disponibles");
            return true;
        }

        switch (args[0].toLowerCase()) {

            // =========================
            // Unirse a la partida
            // =========================
            case "join":
                if (plugin.getGameManager().isPlaying(player)) {
                    player.sendMessage("§6[Paintball] §cYa estás en la partida");
                    return true;
                }
                plugin.getGameManager().addPlayer(player);
                player.sendMessage("§6[Paintball] §aTe has unido a la partida!");
                break;

            // =========================
            // Salir de la partida
            // =========================
            case "leave":
                if (!plugin.getGameManager().isPlaying(player)) {
                    player.sendMessage("§6[Paintball] §cNo estás en la partida");
                    return true;
                }
                plugin.getGameManager().removePlayer(player);
                player.sendMessage("§6[Paintball] §cHas salido de la partida");
                break;

            // =========================
            // Iniciar partida
            // =========================
            case "start":
                plugin.getGameManager().startGame();
                Bukkit.broadcastMessage("§6[Paintball] §aLa partida ha comenzado!");
                break;

            // =========================
            // Ver estadísticas
            // =========================
            case "stats":
                int kills = plugin.getGameManager().getTotalKills(player);
                int wins = plugin.getGameManager().getTotalWins(player);
                int coins = plugin.getGameManager().getCoins(player);

                player.sendMessage("§6[Paintball] §fTus estadísticas:");
                player.sendMessage("§aKills: §f" + kills);
                player.sendMessage("§aVictorias: §f" + wins);
                player.sendMessage("§aCoins: §f" + coins);
                break;

            // =========================
            // Solo ver coins
            // =========================
            case "coins":
                int c = plugin.getGameManager().getCoins(player);
                player.sendMessage("§6[Paintball] §fTienes §a" + c + " coins§f.");
                break;

            // =========================
            // Help
            // =========================
            case "help":
                player.sendMessage("§6[Paintball] §fComandos disponibles:");
                player.sendMessage("§a/paintball join §7- Unirse a la partida");
                player.sendMessage("§a/paintball leave §7- Salir de la partida");
                player.sendMessage("§a/paintball start §7- Iniciar la partida");
                player.sendMessage("§a/paintball stats §7- Ver kills, victorias y coins");
                player.sendMessage("§a/paintball coins §7- Ver solo tus coins");
                break;

            default:
                player.sendMessage("§6[Paintball] §cComando no válido. Usa /paintball help");
        }

        return true;
    }
}
