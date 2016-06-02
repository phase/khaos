package xyz.jadonfowler.khaos.test;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import xyz.jadonfowler.khaos.Khaos;
import xyz.jadonfowler.khaos.game.Arena;
import xyz.jadonfowler.khaos.game.Game;
import xyz.jadonfowler.khaos.game.GameRunnable;
import xyz.jadonfowler.khaos.game.Map;
import xyz.jadonfowler.khaos.game.Team;

/**
 * This is an example game. None of the code in this will work on its own.
 * 
 * Notes:
 * 
 * Game Lobby: -96, 78, 246 Red Team Spawn: -105, 72, 265 Blue Team Spawn: -129,
 * 72, 230 Spectator Lobby: -130, 76, 248
 * 
 * @author phase
 *
 */
public class ExampleGame {

    private Location redSpawn = new Location(Bukkit.getWorld("awesome"), -105, 72, 265);
    private Location blueSpawn = new Location(Bukkit.getWorld("awesome"), -129, 72, 230);
    private Location spectatorSpawn = new Location(Bukkit.getWorld("awesome"), -130, 76, 248);
    private Location lobby = new Location(Bukkit.getWorld("awesome"), -96, 78, 246);

    /**
     * Example onEnable()
     */
    public void onEnable() {
        Game game = new Game("Example", "An example game", lobby);

        Team redTeam = new Team("Red Team", ChatColor.RED);
        Team blueTeam = new Team("Blue Team", ChatColor.BLUE);

        Map map = new Map("Awesome Map", "phase", "awesome", spectatorSpawn);
        map.addSpawn(redTeam, redSpawn).addSpawn(blueTeam, blueSpawn);

        GameRunnable runnable = new GameRunnable() {

            @Override public void onStart(Arena arena, Map map) {
                Khaos.getInstance().getLogger().info("ExampleGame: Arena #" + arena.getId() + " started.");
            }

            @Override public void onStop(Arena arena, Map map, Team winner) {
                Khaos.getInstance().getLogger().info("ExampleGame: Arena #" + arena.getId() + " stopped.");
            }

        };

        Arena arena = new Arena(runnable);

        game.addArena(arena);

    }

}
