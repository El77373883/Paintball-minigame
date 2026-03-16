package me.adrian.paintball.game;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Arena {

    private final String name; // Nombre del mapa/arena
    private int teamCount = 2; // Número de equipos: 2 o 4

    // Spawns de cada equipo
    private final Map<GameTeam, Location> spawns = new HashMap<>();

    // Posiciones de selección para crear el área (como cubo)
    private Location pos1;
    private Location pos2;

    public Arena(String name) {
        this.name = name;
    }

    // =======================
    // NOMBRE
    // =======================
    public String getName() {
        return name;
    }

    // =======================
    // SELECCIÓN DE ÁREA
    // =======================
    public void setPos1(Location loc) {
        this.pos1 = loc;
    }

    public void setPos2(Location loc) {
        this.pos2 = loc;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    // =======================
    // EQUIPOS
    // =======================
    public void setTeamCount(int count) {
        if (count == 2 || count == 4) {
            this.teamCount = count;
        } else {
            this.teamCount = 2; // default
        }
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setSpawn(GameTeam team, Location loc) {
        spawns.put(team, loc);
    }

    public Location getSpawn(GameTeam team) {
        return spawns.get(team);
    }

    // =======================
    // UTILS PARA PANEL / EDICIÓN
    // =======================
    public boolean isSpawnSet(GameTeam team) {
        return spawns.containsKey(team);
    }

    public Map<GameTeam, Location> getAllSpawns() {
        return new HashMap<>(spawns);
    }
}
