package me.adrian.paintball;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArenaSelectionManager {

    private final Map<Player, Location> pos1 = new HashMap<>();
    private final Map<Player, Location> pos2 = new HashMap<>();

    public void setPos1(Player player, Location location) {
        pos1.put(player, location);
    }

    public void setPos2(Player player, Location location) {
        pos2.put(player, location);
    }

    public Location getPos1(Player player) {
        return pos1.get(player);
    }

    public Location getPos2(Player player) {
        return pos2.get(player);
    }

    public boolean hasSelection(Player player) {
        return pos1.containsKey(player) && pos2.containsKey(player);
    }

    public void clearSelection(Player player) {
        pos1.remove(player);
        pos2.remove(player);
    }
}
