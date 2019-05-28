package byow.Core;

import java.io.Serializable;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Command implements Serializable {
    private CommandType type;
    private long args = 0;
    private long time;

    public Command(CommandType type) {
        time = System.currentTimeMillis();
        this.type = type;
    }

    public Command(CommandType type, long args) {
        time = System.currentTimeMillis();
        this.type = type;
        this.args = args;
    }

    public CommandType type() {
        return this.type;
    }

    public long args() {
        return this.args;
    }

    public long time() {
        return this.time;
    }

    public static Queue<Command> load(String file) {
        Queue<Command> cmds = new LinkedList<>();

        try {
            var fstream = new FileInputStream(file);
            var ostream = new ObjectInputStream(fstream);

            int length = ostream.readInt();

            for (int i = 0; i < length; i++) {
                try {
                    Command cmd = (Command) ostream.readObject();
                    cmds.add(cmd);
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                }
            }

            fstream.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        return cmds;
    }

    public static void store(String file, Queue<Command> cmds) {
        try {
            var fstream = new FileOutputStream(file);
            var ostream = new ObjectOutputStream(fstream);

            ostream.writeInt(cmds.size());

            for (Command cmd : cmds) {
                try {
                    ostream.writeObject(cmd);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            ostream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
