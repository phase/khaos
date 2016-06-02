package xyz.jadonfowler.khaos;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import xyz.jadonfowler.khaos.game.Arena;
import xyz.jadonfowler.khaos.game.Game;
import xyz.jadonfowler.khaos.game.Team;
import xyz.jadonfowler.khaos.listener.GameChooser;
import xyz.jadonfowler.khaos.test.ExampleGame;

public class Khaos extends JavaPlugin {

    @Getter static Khaos instance;
    @Getter private ArrayList<Game> gameList;
    @Getter private Location lobby;

    @Override public void onEnable() {
        instance = this;
        gameList = new ArrayList<Game>();
        Bukkit.getPluginManager().registerEvents(new GameChooser(), this);
        this.getCommand("khaos").setExecutor(new KhaosCommandExecutor());
        
        // TODO Remove test game
        ExampleGame exGame = new ExampleGame();
        exGame.onEnable();
    }

    @Override public void onDisable() {
        for (Game g : gameList) {
            g.shutdown();
        }
        getLogger().info("All games have been emptied.");
    }

    public boolean gameExists(String name) {
        for (Game g : gameList)
            if (g.getName().equals(name)) return true;
        return false;
    }

    public boolean isInGame(Player p) {
        // TODO This is ugly
        for (Game g : gameList) {
            for (Arena a : g.getArenas()) {
                for (Team t : a.getTeams()) {
                    for (UUID u : t.getPlayers()) {
                        if (p.getUniqueId().equals(u)) return true;
                    }
                }
                for (UUID u : a.getSpectators()) {
                    if (p.getUniqueId().equals(u)) return true;
                }
            }
        }
        return false;
    }

    public Game getGame(String name) {
        for (Game g : gameList)
            if (g.getName().equals(name)) return g;
        return null;
    }
}