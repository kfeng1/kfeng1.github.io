package byow.util.input;

import byow.Core.Command;
import byow.Core.CommandType;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReplayInputSource extends InputSource {
    private long lastTime = 0;
    private Queue<Command> cmds = new ConcurrentLinkedQueue<>();

    public ReplayInputSource(Queue<Command> cmds) {
        this.cmds.addAll(cmds);
    }

    public void run() {
        for (Command cmd : cmds) {
            if (lastTime != 0) {
                try {
                    Thread.sleep(cmd.time() - lastTime);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }

            lastTime = cmd.time();
            notify(cmd);
            cmds.remove();
        }

        notify(new Command(CommandType.REPLAY_END));
    }

    public List<Character> partial() {
        return new ArrayList<>();
    }

    public long duration() {
        long duration = 0;
        long durationLastTime = 0;

        for (Command cmd : cmds) {
            if (durationLastTime != 0)  {
                duration += cmd.time() - durationLastTime;
            }

            durationLastTime = cmd.time();
        }

        return duration;
    }
}
