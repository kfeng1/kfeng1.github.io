package byow.Core;

import byow.util.event.InputEvent;
import byow.util.Observer;
import byow.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class CommandProcessor extends Observable<Command> implements Observer<InputEvent> {
    private CopyOnWriteArrayList<Character> partial = new CopyOnWriteArrayList<>();

    private long accumulator;

    @Override
    public void notified(InputEvent e) {
        char c = e.key();

        if (!partial.isEmpty()) {
            if (partial.get(0) == 'n') {
                if (Character.isDigit(c)) {
                    accumulator = accumulator * 10 + Character.getNumericValue(c);
                    partial.add(c);
                } else if (c == 's') {
                    notify(new Command(CommandType.SEED, accumulator));
                    accumulator = 0;
                    partial.clear();
                } else {
                    accumulator = 0;
                    partial.clear();
                }
            } else if (partial.get(0) == ':') {
                if (c == 'q') {
                    notify(new Command(CommandType.QUIT));
                    partial.clear();
                }
            }
        } else {
            if (c == 'n') {
                notify(new Command(CommandType.NEW));
                partial.add(c);
            } else if (c == ':') {
                partial.add(c);
            } else if (c == 'l') {
                notify(new Command(CommandType.LOAD));
            } else if (c == 'r') {
                notify(new Command(CommandType.REPLAY_START));
            } else if (c == 'v') {
                notify(new Command(CommandType.TOGGLE_VIS));
            } else if (c == ']') {
                notify(new Command(CommandType.INC_VIS));
            } else if (c == '[') {
                notify(new Command(CommandType.DEC_VIS));
            } else if (c == 'w') {
                notify(new Command(CommandType.UP));
            } else if (c == 'a') {
                notify(new Command(CommandType.LEFT));
            } else if (c == 's') {
                notify(new Command(CommandType.DOWN));
            } else if (c == 'd') {
                notify(new Command(CommandType.RIGHT));
            }
        }
    }

    public void clear() {
        partial.clear();
    }

    public List<Character> partial() {
        return partial;
    }
}
