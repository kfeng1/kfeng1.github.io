package byow.util.input;

import byow.util.Observable;
import byow.Core.Command;
import byow.util.event.InputEvent;
import java.util.List;

public abstract class InputSource extends Observable<Command> implements Runnable {
    public abstract void run();
    public abstract List<Character> partial();
}
