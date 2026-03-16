package me.adrian.paintball;

import me.adrian.paintball.game.GameManager;
import me.adrian.paintball.game.GameTeam;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PaintballAdminCommand implements CommandExecutor {

    private final PaintballPlugin plugin;
    private final ArenaSelectionManager selectionManager = new ArenaSelectionManager();
    private final Map<Player, EditSession> editSessions = new HashMap<>();

    public PaintballAdminCommand(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§6[Paintball] §cSolo jugadores pueden usar estos comandos");
            return true;
        }

        Player player = (Player) sender;
        GameManager gm = plugin.getGameManager();

        if (args.length == 0) {
            player.sendMessage("§6[Paintball] §fUsa /pa help para ver todos los comandos");
            return true;
        }

        switch (args[0].toLowerCase()) {

            // --------------------
            // 1️⃣ HELP
            // --------------------
            case "help":
                player.sendMessage("§6[Paintball] §fComandos disponibles:");
                player.sendMessage("§f/pb select §7- Dar hacha de selección de área");
                player.sendMessage("§f/pb create <nombre> §7- Crear arena con selección");
                player.sendMessage("§f/pa edit <arena> §7- Editar arena y spawns por team");
                player.sendMessage("§f/pa setteams <2|4> §7- Configurar número de equipos");
                player.sendMessage("§f/pa setspawn <RED|BLUE|PINK|GREEN> §7- Configurar spawn de cada equipo");
                player.sendMessage("§f/pa remove <arena> §7- Remover arena existente");
                player.sendMessage("§f/pa version §7- Ver versión del plugin");
                player.sendMessage("§f/pa creator §7- Ver creador del plugin");
                player.sendMessage("§f/pa help §7- Mostrar esta ayuda");
                break;

            // --------------------
            // 2️⃣ SELECCIÓN DE ARENA
            // --------------------
            case "select":
                giveSelectionAxe(player);
                break;

            case "create":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cUsa: /pb create <nombre>");
                    return true;
                }
                String arenaName = args[1];
                if (!selectionManager.hasSelection(player)) {
                    player.sendMessage("§6[Paintball] §cDebes seleccionar las posiciones con el hacha primero!");
                    return true;
                }
                GameManager.Arena arena = new GameManager.Arena(arenaName);
                arena.addSpawn(selectionManager.getPos1(player));
                arena.addSpawn(selectionManager.getPos2(player));
                gm.addArena(arena);
                gm.setCurrentArena(arenaName);
                selectionManager.clearSelection(player);
                player.sendMessage("§6[Paintball] §aArena " + arenaName + " creada con éxito!");
                break;

            case "remove":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cUsa: /pa remove <nombre>");
                    return true;
                }
                String removeArena = args[1];
                if (gm.removeArena(removeArena)) {
                    player.sendMessage("§6[Paintball] §aArena " + removeArena + " eliminada con éxito!");
                } else {
                    player.sendMessage("§6[Paintball] §cArena " + removeArena + " no encontrada!");
                }
                break;

            // --------------------
            // 3️⃣ EDICIÓN DE ARENA
            // --------------------
            case "edit":
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cUsa: /pa edit <arena>");
                    return true;
                }
                String editArenaName = args[1];
                player.sendMessage("§6[Paintball] §aModo edición activado para arena: " + editArenaName);
                player.sendMessage("§6[Paintball] §fMarca los spawns de cada equipo con el hacha.");
                EditSession session = new EditSession(editArenaName);
                editSessions.put(player, session);
                player.sendMessage("§6[Paintball] §fElige cuántos equipos: 2 o 4 usando /pa setteams <2|4>");
                break;

            case "setteams":
                if (!editSessions.containsKey(player)) {
                    player.sendMessage("§6[Paintball] §cPrimero usa /pa edit <arena>");
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cUsa /pa setteams <2|4>");
                    return true;
                }
                EditSession s = editSessions.get(player);
                try {
                    int teams = Integer.parseInt(args[1]);
                    if (teams != 2 && teams != 4) throw new NumberFormatException();
                    s.setNumTeams(teams);
                    player.sendMessage("§6[Paintball] §aSe usarán " + teams + " equipos para esta arena");
                    player.sendMessage("§6[Paintball] §fAhora usa /pa setspawn <RED|BLUE|PINK|GREEN> para cada equipo");
                } catch (NumberFormatException ex) {
                    player.sendMessage("§6[Paintball] §cNúmero inválido. Solo 2 o 4.");
                }
                break;

            case "setspawn":
                if (!editSessions.containsKey(player)) {
                    player.sendMessage("§6[Paintball] §cPrimero usa /pa edit <arena>");
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage("§6[Paintball] §cUsa /pa setspawn <RED|BLUE|PINK|GREEN>");
                    return true;
                }
                EditSession edit = editSessions.get(player);
                GameTeam team;
                try {
                    team = GameTeam.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("§6[Paintball] §cEquipo inválido!");
                    return true;
                }
                edit.setSpawn(team, player.getLocation());
                player.sendMessage("§6[Paintball] §aSpawn de " + team.name() + " guardado!");

                if (edit.allSpawnsSet()) {
                    GameManager.Arena arenaEdit = new GameManager.Arena(edit.getArenaName());
                    for (Location loc : edit.getSpawns().values()) {
                        arenaEdit.addSpawn(loc);
                    }
                    plugin.getGameManager().addArena(arenaEdit);
                    plugin.getGameManager().setCurrentArena(edit.getArenaName());
                    player.sendMessage("§6[Paintball] §aArena " + edit.getArenaName() + " editada con éxito!");
                    editSessions.remove(player);
                }
                break;

            // --------------------
            // 4️⃣ INFORMACIÓN
            // --------------------
            case "version":
                player.sendMessage("§6[Paintball] §fVersión del plugin: §a1.0.0");
                break;

            case "creator":
                player.sendMessage("§6[Paintball] §fCreador del plugin: §asoyadrianyt001");
                break;

            default:
                player.sendMessage("§6[Paintball] §cComando no reconocido. Usa /pa help");
        }

        return true;
    }

    private void giveSelectionAxe(Player player) {
        ItemStack axe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.setDisplayName("§6[Paintball] §fHacha de selección");
        axe.setItemMeta(meta);
        player.getInventory().addItem(axe);
        player.sendMessage("§6[Paintball] §aHas recibido la hacha de selección para marcar la arena!");
    }

    private static class EditSession {
        private final String arenaName;
        private int numTeams = 2;
        private final Map<GameTeam, Location> spawns = new HashMap<>();

        public EditSession(String arenaName) {
            this.arenaName = arenaName;
        }

        public void setNumTeams(int numTeams) {
            this.numTeams = numTeams;
        }

        public String getArenaName() {
            return arenaName;
        }

        public void setSpawn(GameTeam team, Location loc) {
            spawns.put(team, loc);
        }

        public Map<GameTeam, Location> getSpawns() {
            return spawns;
        }

        public boolean allSpawnsSet() {
            return spawns.size() == numTeams;
        }
    }
}
