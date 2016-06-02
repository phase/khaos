package xyz.jadonfowler.khaos.game;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import lombok.Getter;
import xyz.jadonfowler.khaos.Khaos;

public class Map {

    @Getter String name;
    @Getter String creator;
    @Getter String worldName;
    @Getter Location spectatorLobby;
    @Getter boolean isLoaded;
    @Getter HashMap<Team, Location> spawns;

    public Map(String name, String creator, String worldName, Location spectatorLobby) {
        this.name = name;
        this.creator = creator;
        this.worldName = worldName;
        this.spectatorLobby = spectatorLobby;
        this.isLoaded = false;
        this.spawns = new HashMap<Team, Location>();
    }

    public void loadWorld() {
        if (isLoaded) unloadWorld();
        Bukkit.createWorld(new WorldCreator(worldName));
        Khaos.getInstance().getLogger().info("World '" + worldName + "' was loaded.");
        isLoaded = true;
    }

    /**
     * Unloads world, but doesn't save any changes. This allows for players to
     * destroy terrain and it won't be saved.
     */
    public void unloadWorld() {
        if (!isLoaded) return;
        Bukkit.unloadWorld(worldName, false);
        Khaos.getInstance().getLogger().info("World '" + worldName + "' was unloaded.");
        isLoaded = false;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Map addSpawn(Team team, Location location) {
        spawns.put(team, location);
        return this;
    }

}
