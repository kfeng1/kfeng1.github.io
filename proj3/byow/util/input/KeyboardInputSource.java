package byow.util.input;

import edu.princeton.cs.introcs.StdDraw;
import byow.util.event.InputEvent;
import byow.Core.Command;
import byow.Core.CommandProcessor;
import byow.util.Observer;
import java.util.List;

public class KeyboardInputSource extends InputSource implements Observer<Command> {
    private final CommandProcessor proc = new CommandProcessor();

    public KeyboardInputSource() {
        proc.register(this);
    }

    public void notified(Command c) {
        notify(c);
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                proc.notified(new InputEvent(c));
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public List<Character> partial() {
        return proc.partial();
    }
}
