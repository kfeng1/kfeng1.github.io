package byow.util.input;

import byow.util.Observable;
import byow.util.Observer;
import byow.util.event.InputEvent;
import byow.Core.Command;
import byow.Core.CommandProcessor;
import java.util.List;
import java.util.stream.Collectors;

public class StringInputSource extends InputSource implements Observer<Command> {
    private final String str;
    private final CommandProcessor proc = new CommandProcessor();

    public StringInputSource(String s) {
        str = s;
        proc.register(this);
    }

    public void notified(Command c) {
        notify(c);
    }

    public void run() {
        str.chars().forEach(c -> proc.notified(new InputEvent((char) c)));
    }

    public List<Character> partial() {
        return str.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }
}
