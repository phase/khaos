package xyz.jadonfowler.khaos.game;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import lombok.Getter;
import xyz.jadonfowler.khaos.Khaos;

public class Game {

    @Getter private String name;
    @Getter private String description;
    @Getter private Location lobby;
    @Getter private ArrayList<Arena> arenas;

    public Game(String name, String description, Location lobby) {
        this.name = name;
        this.description = description;
        this.lobby = lobby;
        this.arenas = new ArrayList<Arena>();
        Khaos.getInstance().getLogger().info("Game '" + name + "' created.");
    }

    public void addPlayer(Player p, int arenaId) {
        Arena arena = arenas.get(arenaId - 1);
        arena.addPlayer(p);
    }

    public Game addArena(Arena a) {
        a.setLobby(lobby);
        a.setId(arenas.size() + 1);
        arenas.add(a);
        return this;
    }

    public Game setGameRunnable(GameRunnable r) {
        for (Arena a : arenas)
            a.setGameRunnable(r);
        return this;
    }

    public void shutdown() {
        for (Arena a : arenas)
            a.stop(true);
        Khaos.getInstance().getLogger().info("Game " + name + " has stopped.");
    }

    public Arena getArena(int id) {
        return arenas.get(id - 1);
    }
}
