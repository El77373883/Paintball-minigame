package me.adrian.paintball.game;

import org.bukkit.Location;

public class Arena {

    private String name;
    private Location spawnLocation; // Si quieres mantener la ubicación
    private int teamCount = 2; // Default 2 equipos

    public Arena(String name) {
        this.name = name;
    }

    public Arena(String name, Location spawnLocation) {
        this.name = name;
        this.spawnLocation = spawnLocation;
    }

    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int count) {
        this.teamCount = count;
    }
}
