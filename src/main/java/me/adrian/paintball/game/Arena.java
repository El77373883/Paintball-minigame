package me.adrian.paintball.game;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Arena {

    private final String name;
    private int maxTeams = 2; // por defecto
    private final Map<String, Location> spawns = new HashMap<>(); // RED, BLUE, PINK, GREEN

    public Arena(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        if (maxTeams == 2 || maxTeams == 4) {
            this.maxTeams = maxTeams;
        }
    }

    public void setSpawn(String team, Location loc) {
        spawns.put(team.toUpperCase(), loc);
    }

    public Location getSpawn(String team) {
        return spawns.get(team.toUpperCase());
    }

    public Map<String, Location> getSpawns() {
        return spawns;
    }
}
