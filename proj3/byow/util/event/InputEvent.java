package byow.util.event;

public class InputEvent extends Event {
    protected char c;

    public InputEvent(char ch) {
        c = ch;
    }

    public char key() {
        return c;
    }
}
