package me.adrian.paintball.command;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCommand implements CommandExecutor {

    private final PaintballPlugin plugin;

    // Constructor: obtiene la instancia del plugin
    public PaintballCommand() {
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
            player.sendMessage("Usa /paintball help para ver los comandos.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                player.sendMessage("Te has unido a la partida de Paintball.");
                // Aquí podrías agregar la lógica para unir al jugador
                break;

            case "leave":
                player.sendMessage("Has salido de la partida de Paintball.");
                // Aquí podrías agregar la lógica para salir de la partida
                break;

            case "help":
                player.sendMessage("Comandos de Paintball:");
                player.sendMessage("/paintball join - Unirse al juego");
                player.sendMessage("/paintball leave - Salir del juego");
                player.sendMessage("/paintball help - Mostrar ayuda");
                break;

            default:
                player.sendMessage("Comando no reconocido. Usa /paintball help");
        }

        return true;
    }
}
