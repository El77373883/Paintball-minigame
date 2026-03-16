package me.adrian.paintball.game;

import org.bukkit.Location;

public class Arena {

    private String name;
    private Location spawnLocation;
    private int teamCount;

    // Constructor que acepta solo el nombre
    public Arena(String name) {
        this.name = name;
    }

    // Constructor que acepta nombre + spawn location
    public Arena(String name, Location spawnLocation) {
        this.name = name;
        this.spawnLocation = spawnLocation;
    }

    // Getter y setter para teamCount
    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    // Getter para el nombre
    public String getName() {
        return name;
    }

    // Getter y setter para spawnLocation
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
