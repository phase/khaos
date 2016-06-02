package xyz.jadonfowler.khaos.game;

import org.bukkit.entity.Player;
import lombok.Getter;

public class TeamException extends Exception {

    @Getter Player player;
    @Getter Team team;

    public TeamException(Player player, Team team, String message) {
        super(message);
        this.player = player;
        this.team = team;
    }

}
