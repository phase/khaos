package xyz.jadonfowler.khaos.game;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor public class Map {

    @Getter String name;
    @Getter String worldName;
    @Getter String creator;
    @Getter Location spectatorLobby;
    @Getter boolean isLoaded = false;
    @Getter HashMap<Team, Location> spawns = new HashMap<Team, Location>();

    public void loadWorld() {
        if (isLoaded) unloadWorld();
        Bukkit.createWorld(new WorldCreator(worldName));
        isLoaded = true;
    }

    /**
     * Unloads world, but doesn't save any changes. This allows for players to
     * destroy terrain and it won't be saved.
     */
    public void unloadWorld() {
        if (!isLoaded) return;
        Bukkit.unloadWorld(worldName, false);
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
