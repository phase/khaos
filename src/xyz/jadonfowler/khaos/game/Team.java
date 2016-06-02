package xyz.jadonfowler.khaos.game;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import lombok.Getter;

public class Team {

    @Getter private ArrayList<UUID> players;

    @Getter private String name;
    @Getter private ChatColor color;
    @Getter private int maxPlayers;

    public Team(String name, ChatColor color, int maxPlayers) {
        this.name = name;
        this.color = color;
        this.maxPlayers = maxPlayers;
    }

    public Team addPlayer(Player p) throws TeamException {
        if (maxPlayers == -1 || players.size() < maxPlayers) players.add(p.getUniqueId());
        else throw new TeamException(p, this, name + " has " + players.size() + "/" + maxPlayers + " players.");
        return this;
    }

    public Team removePlayer(Player p) throws TeamException {
        if (players.contains(p.getUniqueId())) players.remove(p.getUniqueId());
        else throw new TeamException(p, this, p.getName() + " is not in " + name);
        return this;
    }
}
