package me.adrian.paintball.gui;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PaintballShop {

    private final PaintballPlugin plugin;

    public PaintballShop(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    // GUI para jugadores
    public void openShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lPaintball Shop");

        // Ejemplos de items “profesionales” con colores y lore
        inv.setItem(10, createItem(Material.DIAMOND_SWORD,
                "§b§lEspada Épica",
                "§7Un arma letal para dominar las partidas",
                "§eCosto: 50 coins"));
        inv.setItem(11, createItem(Material.BOW,
                "§a§lArco de Precisión",
                "§7Dispara con exactitud y rapidez",
                "§eCosto: 40 coins"));
        inv.setItem(12, createItem(Material.SNOWBALL,
                "§f§lBola de Paintball",
                "§7El arma principal de tu arsenal",
                "§eCosto: 5 coins"));
        inv.setItem(13, createItem(Material.GOLDEN_APPLE,
                "§6§lManzana Dorada",
                "§7Recupera tu vida instantáneamente",
                "§eCosto: 20 coins"));
        inv.setItem(14, createItem(Material.LEATHER_HELMET,
                "§d§lCasco de Equipo",
                "§7Protección básica del equipo",
                "§eCosto: 15 coins"));
        inv.setItem(15, createItem(Material.LEATHER_CHESTPLATE,
                "§d§lPeto del Equipo",
                "§7Más defensa para tus partidas",
                "§eCosto: 25 coins"));

        player.openInventory(inv);
    }

    // GUI para edición de tienda (solo admins)
    public void openEditShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 18, "§6§lPaintball Shop Editor");

        inv.setItem(0, createItem(Material.EMERALD,
                "§aAgregar Item",
                "§7Click para agregar un nuevo item a la tienda"));
        inv.setItem(1, createItem(Material.REDSTONE,
                "§cRemover Item",
                "§7Click para eliminar un item existente de la tienda"));
        inv.setItem(2, createItem(Material.PAPER,
                "§eCambiar Precios",
                "§7Click para modificar los precios en coins"));
        inv.setItem(3, createItem(Material.BOOK,
                "§bInformación de Items",
                "§7Click para editar descripción y lore de los items"));

        player.openInventory(inv);
    }

    // Método para crear items con nombre, lore y colores
    private ItemStack createItem(Material material, String displayName, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            List<String> lore = new ArrayList<>();
            for (String line : loreLines) {
                lore.add(line);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
