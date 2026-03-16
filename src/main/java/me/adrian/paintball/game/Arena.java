package me.adrian.paintball.game;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

public class Arena {

    private final String name;
    private final Map<String, Location> spawns = new HashMap<>();
    private int maxTeams = 2; // default

    public Arena(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSpawn(String team, Location loc) {
        spawns.put(team, loc);
    }

    public Location getSpawn(String team) {
        return spawns.get(team);
    }

    public void setMaxTeams(int max) {
        this.maxTeams = max;
    }

    public int getMaxTeams() {
        return maxTeams;
    }
}
