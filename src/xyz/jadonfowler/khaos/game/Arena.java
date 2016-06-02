package xyz.jadonfowler.khaos.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import lombok.Getter;
import lombok.Setter;
import xyz.jadonfowler.khaos.Khaos;

public class Arena {

    @Getter private ArenaState state;
    @Getter private ArrayList<Team> teams;
    @Getter private ArrayList<Map> maps;
    @Getter private ArrayList<UUID> spectators;

    @Getter @Setter int id = -1;
    @Getter @Setter GameRunnable gameRunnable;
    @Getter @Setter Location lobby;
    @Getter Map currentMap;

    @Getter private static Random random = new Random();

    public Arena(GameRunnable gameRunnable) {
        this.state = ArenaState.PRE_GAME;
        this.teams = new ArrayList<Team>();
        this.maps = new ArrayList<Map>();
        this.spectators = new ArrayList<UUID>();
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

    public ArrayList<UUID> getPlayers() {
        ArrayList<UUID> players = new ArrayList<UUID>();
        for (Team t : teams)
            players.addAll(t.getPlayers());
        return players;
    }

    public void addPlayer(Player p) {
        clearPlayer(p);

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
            currentMap = getRandomMap();
            currentMap.loadWorld();
            for (Team team : teams) {
                for (UUID u : team.getPlayers()) {
                    Player p = Bukkit.getPlayer(u);
                    p.teleport(currentMap.getSpawns().get(team));
                    p.setGameMode(GameMode.SURVIVAL);
                    clearPlayer(p);
                }
            }
            for (UUID u : spectators) {
                Player p = Bukkit.getPlayer(u);
                p.setGameMode(GameMode.CREATIVE);
                clearPlayer(p);
            }

            if (gameRunnable != null) gameRunnable.onStart(this, currentMap);
        }
    }

    public void stop(boolean forceStop) {
        if (state == ArenaState.IN_GAME) {
            gameRunnable.onStop(this, currentMap, null);
            currentMap.unloadWorld();
            if (forceStop) {
                for (Team t : teams) {
                    for (UUID u : t.getPlayers()) {
                        Player p = Bukkit.getPlayer(u);
                        p.teleport(Khaos.getInstance().getLobby());
                        // p.kickPlayer("Thanks for playing!");
                    }
                }
            }
            else {
                for (Team t : teams) {
                    for (UUID u : t.getPlayers()) {
                        Player p = Bukkit.getPlayer(u);
                        p.teleport(lobby);
                        clearPlayer(p);
                        p.setGameMode(GameMode.SURVIVAL);
                    }
                }
                for (UUID u : spectators) {
                    Player p = Bukkit.getPlayer(u);
                    p.teleport(lobby);
                    clearPlayer(p);
                    p.setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }

    private Map getRandomMap() {
        Map m = maps.get(random.nextInt(maps.size()));
        if (m.isLoaded() // Map is already in use
                || (m == currentMap && maps.size() != 1)) // We chose the
                                                          // current map
            return getRandomMap();
        return m;
    }

    private void clearPlayer(Player p) {
        p.setHealth(20d);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }

    @Override public String toString() {
        return "Arena(" + id + ")";
    }
}
