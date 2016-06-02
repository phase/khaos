package xyz.jadonfowler.khaos.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor public enum ArenaState {
    PRE_GAME(true, true), IN_GAME(false, true), POST_GAME(false, true), DISABLED(false, false);

    @Getter private boolean canJoin;
    @Getter private boolean canSpectate;

}
