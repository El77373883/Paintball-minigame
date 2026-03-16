package me.adrian.paintball;

import me.adrian.paintball.gui.PaintballPanel;
import me.adrian.paintball.gui.PaintballShop;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PaintballAdminCommand implements CommandExecutor {

    private final PaintballPlugin plugin;

    public PaintballAdminCommand(PaintballPlugin plugin) {
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
            player.sendMessage("§6[Paintball] §fUsa /pa help para ver todos los comandos");
            return true;
        }

        switch (args[0].toLowerCase()) {

            // =========================
            // Selección y creación de arenas
            // =========================
            case "select":
                player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
                player.sendMessage("§6[Paintball] §aHacha de selección dada! Marca dos puntos y usa /pb create <nombre>");
                break;

            case "create":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cDebes escribir el nombre de la arena");
                    return true;
                }
                String arenaName = args[1];
                plugin.getGameManager().createArena(player, arenaName);
                player.sendMessage("§6[Paintball] §aArena '" + arenaName + "' creada!");
                break;

            case "remove":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cDebes escribir el nombre de la arena");
                    return true;
                }
                plugin.getGameManager().removeArena(args[1]);
                player.sendMessage("§6[Paintball] §cArena '" + args[1] + "' eliminada!");
                break;

            // =========================
            // Edición avanzada de arenas
            // =========================
            case "edit":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cDebes escribir el nombre de la arena");
                    return true;
                }
                plugin.getGameManager().editArena(player, args[1]);
                player.sendMessage("§6[Paintball] §aEditando arena '" + args[1] + "'");
                break;

            case "setteams":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cDebes indicar 2 o 4");
                    return true;
                }
                int teams = Integer.parseInt(args[1]);
                plugin.getGameManager().setTeams(player, teams);
                player.sendMessage("§6[Paintball] §aNúmero de equipos seteado a " + teams);
                break;

            case "setspawn":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cDebes indicar RED, BLUE, PINK o GREEN");
                    return true;
                }
                plugin.getGameManager().setSpawn(player, args[1].toUpperCase());
                player.sendMessage("§6[Paintball] §aSpawn del equipo " + args[1].toUpperCase() + " seteado!");
                break;

            // =========================
            // Información del plugin
            // =========================
            case "version":
                player.sendMessage("§6[Paintball] §fVersión del plugin: 1.0");
                break;

            case "creator":
                player.sendMessage("§6[Paintball] §fCreador: soyadrianyt001");
                break;

            case "help":
                player.sendMessage("§6[Paintball] §fComandos de administrador:");
                player.sendMessage("§a/paintball join/leave/start/stats");
                player.sendMessage("§a/pb select");
                player.sendMessage("§a/pb create <nombre>");
                player.sendMessage("§a/pa remove <nombre>");
                player.sendMessage("§a/pa edit <arena>");
                player.sendMessage("§a/pa setteams <2|4>");
                player.sendMessage("§a/pa setspawn <RED|BLUE|PINK|GREEN>");
                player.sendMessage("§a/pa version");
                player.sendMessage("§a/pa creator");
                player.sendMessage("§a/pa panel");
                player.sendMessage("§a/pa shop");
                player.sendMessage("§a/pa editshop");
                player.sendMessage("§a/pa help");
                break;

            // =========================
            // Panel GUI para admins
            // =========================
            case "panel":
                PaintballPanel panel = new PaintballPanel(plugin);
                panel.openPanel(player);
                break;

            // =========================
            // Tienda accesible a todos
            // =========================
            case "shop":
                PaintballShop shop = new PaintballShop(plugin);
                shop.openShop(player);
                break;

            // =========================
            // Editor de tienda solo admins
            // =========================
            case "editshop":
                if (!player.hasPermission("paintball.admin")) {
                    player.sendMessage("§6[Paintball] §cNo tienes permisos para editar la tienda!");
                    return true;
                }
                PaintballShop shopEditor = new PaintballShop(plugin);
                shopEditor.openEditShop(player);
                break;

            default:
                player.sendMessage("§6[Paintball] §cComando no válido. Usa /pa help");
        }

        return true;
    }
}
