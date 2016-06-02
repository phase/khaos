package xyz.jadonfowler.khaos.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArenaState {
    PRE_GAME(true, true, "Game has not started yet"),
    IN_GAME(false, true, "Game is in progress"),
    POST_GAME(false, true, "Game just ended"),
    DISABLED(false, false, "Game is disabled");

    @Getter private boolean canJoin;
    @Getter private boolean canSpectate;
    @Getter private String message;

    @Override public String toString() {
        return message;
    }

}
