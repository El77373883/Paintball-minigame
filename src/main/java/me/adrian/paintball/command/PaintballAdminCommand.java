package me.adrian.paintball.command;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballAdminCommand implements CommandExecutor {

    private final PaintballPlugin plugin;

    // Constructor
    public PaintballAdminCommand() {
        this.plugin = PaintballPlugin.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Usa /paintballadmin help para ver los comandos de admin.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                player.sendMessage("Configuración del plugin recargada.");
                break;

            case "stopgame":
                // Aquí iría la lógica para detener la partida
                player.sendMessage("La partida ha sido detenida.");
                break;

            case "help":
                player.sendMessage("Comandos de Admin de Paintball:");
                player.sendMessage("/paintballadmin reload - Recargar configuración");
                player.sendMessage("/paintballadmin stopgame - Detener partida");
                player.sendMessage("/paintballadmin help - Mostrar ayuda");
                break;

            default:
                player.sendMessage("Comando no reconocido. Usa /paintballadmin help");
        }

        return true;
    }
}
