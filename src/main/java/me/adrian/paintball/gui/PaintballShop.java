package me.adrian.paintball.gui;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaintballShop implements Listener {

    private final PaintballPlugin plugin;

    // Map con items y su precio en coins
    private final Map<Material, Integer> shopItems = new HashMap<>();

    public PaintballShop(PaintballPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Items predeterminados “chidos”
        shopItems.put(Material.DIAMOND_SWORD, 200);
        shopItems.put(Material.FIRE_CHARGE, 100); // Bola especial
        shopItems.put(Material.GLOWSTONE, 50); // Efecto brillante
        shopItems.put(Material.ELYTRA, 500); // Poder de vuelo
    }

    // =========================
    // Abrir tienda para jugadores
    // =========================
    public void openShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6Paintball Shop");

        int slot = 10;
        for (Map.Entry<Material, Integer> entry : shopItems.entrySet()) {
            ItemStack item = new ItemStack(entry.getKey());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a" + entry.getKey().name());
            meta.setLore(Arrays.asList("§fPrecio: §6" + entry.getValue() + " coins"));
            item.setItemMeta(meta);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    // =========================
    // Abrir editor de tienda para admin
    // =========================
    public void openEditShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6Paintball Shop Editor");

        int slot = 10;
        for (Map.Entry<Material, Integer> entry : shopItems.entrySet()) {
            ItemStack item = new ItemStack(entry.getKey());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + entry.getKey().name());
            meta.setLore(Arrays.asList("§fPrecio: §6" + entry.getValue() + " coins", "§7Clic para editar/eliminar"));
            item.setItemMeta(meta);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    // =========================
    // Manejo de clicks
    // =========================
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals("§6Paintball Shop") && !title.equals("§6Paintball Shop Editor")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String itemName = clicked.getType().name();

        if (title.equals("§6Paintball Shop")) {
            // Comprar item
            int price = shopItems.getOrDefault(clicked.getType(), 0);
            int coins = plugin.getGameManager().getCoins(player);

            if (coins >= price) {
                plugin.getGameManager().addCoins(player, -price);
                player.getInventory().addItem(new ItemStack(clicked.getType()));
                player.sendMessage("§6[Paintball] §aHas comprado " + itemName + " por " + price + " coins!");
            } else {
                player.sendMessage("§6[Paintball] §cNo tienes suficientes coins para comprar " + itemName);
            }
        }

        if (title.equals("§6Paintball Shop Editor")) {
            // Solo admin puede editar
            if (!player.hasPermission("paintball.admin")) return;

            player.sendMessage("§6[Paintball] §eFunción de editar tienda próximamente...");
            // Aquí se puede expandir para cambiar precio o eliminar item
        }
    }
}
