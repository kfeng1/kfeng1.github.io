package byow.Core;

import byow.util.Direction;

import java.util.Arrays;
import java.util.List;

public enum CommandType {
    QUIT, LOAD, REPLAY_START, REPLAY_END, NEW, SEED, UP, LEFT, DOWN, RIGHT, TOGGLE_VIS, INC_VIS, DEC_VIS;

    public static final List<CommandType> MOVEMENT = Arrays.asList(UP,
            LEFT,
            RIGHT,
            DOWN);

    private Direction dir;

    public Direction direction() {
        return dir;
    }

    static {
        UP.dir = Direction.TOP;
        DOWN.dir = Direction.BOT;
        LEFT.dir = Direction.LEFT;
        RIGHT.dir = Direction.RIGHT;
    }
};
