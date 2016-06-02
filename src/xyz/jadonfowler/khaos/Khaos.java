package xyz.jadonfowler.khaos;

import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import xyz.jadonfowler.khaos.game.Game;

public class Khaos extends JavaPlugin {

    @Getter static Khaos instance;
    @Getter private ArrayList<Game> gameList;

    @Override public void onEnable() {
        instance = this;
        gameList = new ArrayList<Game>();
    }

    @Override public void onDisable() {

    }

}