package me.adrian.paintball.gui;

import me.adrian.paintball.PaintballPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PaintballPanel {

    private final PaintballPlugin plugin;

    public PaintballPanel(PaintballPlugin plugin) {
        this.plugin = plugin;
    }

    public void openPanel(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§6Paintball Admin Panel");
        // Aquí puedes agregar items del panel
        player.openInventory(inv);
    }
}
