package me.adrian.paintball.game;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;

public class Arena {
    private String name;
    private int maxTeams = 2;
    private Map<GameTeam, Location> spawns = new HashMap<>();

    public Arena(String name) {
        this.name = name;
    }

    public void setTeams(int teams) {
        this.maxTeams = teams;
    }

    public int getTeams() {
        return maxTeams;
    }

    public void setSpawn(GameTeam team, Location loc) {
        spawns.put(team, loc);
    }

    public Location getSpawn(GameTeam team) {
        return spawns.get(team);
    }

    public String getName() {
        return name;
    }
}
