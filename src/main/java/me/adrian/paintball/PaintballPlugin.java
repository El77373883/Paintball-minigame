package me.adrian.paintball.command;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PaintballCommand implements CommandExecutor {

    private final PaintballPlugin plugin;

    public PaintballCommand() {
        this.plugin = PaintballPlugin.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Has ejecutado el comando /paintball!");
        plugin.getLogger().info(sender.getName() + " ejecutó /paintball");
        return true;
    }
}
