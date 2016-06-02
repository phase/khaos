package xyz.jadonfowler.khaos.game;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import lombok.Getter;
import lombok.Setter;

public class Arena {

    @Getter private ArenaState state;
    @Getter private ArrayList<Team> teams;
    @Getter private ArrayList<Map> maps;
    @Getter private ArrayList<UUID> spectators;

    @Getter int id;
    @Getter @Setter GameRunnable gameRunnable;
    @Getter Map currentMap;
    @Getter Location lobby;

    public Arena(int id, GameRunnable gameRunnable, Location lobby) {
        this.state = ArenaState.PRE_GAME;
        this.teams = new ArrayList<Team>();
        this.maps = new ArrayList<Map>();
        this.id = id;
        this.gameRunnable = gameRunnable;
    }

    public boolean isInArena(UUID u) {
        for (Team t : teams)
            if (t.getPlayers().contains(u)) return true;
        return false;
    }

    public boolean isSpectator(UUID u) {
        return spectators.contains(u);
    }

    public void addPlayer(Player p) {
        p.setHealth(20d);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        if (state.equals(ArenaState.PRE_GAME)) {
            p.teleport(lobby);
            p.setGameMode(GameMode.SURVIVAL);
            for (Player o : Bukkit.getOnlinePlayers())
                if (!isInArena(o.getUniqueId())) {
                    p.hidePlayer(o);
                    o.hidePlayer(p);
                }
                else {
                    p.showPlayer(o);
                    o.showPlayer(p);
                }
            giveRandomTeam(p);
            // checkStart();
        }
        else if (state.equals(ArenaState.IN_GAME)) {
            p.teleport(lobby); // TODO Change to Map Spawn
            p.setGameMode(GameMode.CREATIVE);
            for (Player o : Bukkit.getOnlinePlayers()) {
                if (!isInArena(o.getUniqueId()) && !isSpectator(o.getUniqueId())) {
                    p.hidePlayer(o);
                    o.hidePlayer(p);
                }
                else {
                    p.showPlayer(o);
                    o.showPlayer(p);
                }
            }
            spectators.add(p.getUniqueId());
        }
    }

    private void giveRandomTeam(Player p) {
        Team t = null;
        for (Team te : teams)
            if (t == null || te.getPlayers().size() < t.getPlayers().size()) t = te;
        try {
            t.addPlayer(p);
        }
        catch (TeamException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (state == ArenaState.PRE_GAME) {
            state = ArenaState.IN_GAME;
            gameRunnable.onStart(this, currentMap);
        }
    }

    public void stop() {
        gameRunnable.onStop(this, currentMap, null);
    }
}
