package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGen.WorldGenerator;
import byow.util.Observer;
import byow.World.World;
import byow.util.Point;
import byow.util.input.Entity;
import byow.util.input.InputSource;
import byow.util.input.KeyboardInputSource;
import byow.util.input.StringInputSource;
import byow.util.input.ReplayInputSource;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Engine implements Observer<Command> {
    public static final int WIDTH = 72;
    public static final int HEIGHT = 34;

    private boolean running = true;
    private boolean replay = false;

    private enum RenderState {
        DISABLED, INIT, ACTIVE, DIRTY
    };
    private RenderState renderState = RenderState.INIT;
    private TERenderer ter = new TERenderer();
    private World world = new World(WIDTH, HEIGHT);
    private Random rand;

    // Engine state
    private boolean worldGenerated = false;
    private int turn = 0;

    // Input state
    private InputSource inputSource;
    private long replayStart;
    private long replayDuration;
    private ReplayInputSource replaySource;
    private Queue<Command> cmdBuffer = new ConcurrentLinkedQueue<>();
    private Queue<Command> cmdHistory = new LinkedList<Command>();

    // HUD state
    private enum HudState {
        SPLASH, SEED, WORLD
    };
    private HudState hudState = HudState.SPLASH;
    private TETile hudHovered = Tileset.NOTHING;
    private ArrayList<Character> hudPartial = new ArrayList<>();
    private int hudTurn = 0;
    private String hudDate = "";
    private String hudReplayTime = "";
    private Font defaultFont = new Font("Monaco", Font.BOLD, 14);
    private Font titleFont = new Font("Arial", Font.BOLD, 24);

    @Override
    public void notified(Command c) {
        cmdBuffer.add(c);
    }

    public void simulateWorld(Command c) {
        //System.out.println(c.type());

        if (c.type() == CommandType.LOAD) {
            // Read from save and add commands to history
            Queue<Command> cmds = Command.load("save.txt");
            cmdHistory.clear();

            // Add the read commands to the front of the cmd queue
            cmds.addAll(cmdBuffer);
            cmdBuffer.clear();
            cmdBuffer.addAll(cmds);
        } else if (c.type() == CommandType.QUIT) {
            Command.store("save.txt", cmdHistory);
            running = false;
        } else if (c.type() == CommandType.NEW) {
            hudState = HudState.SEED;
        } else if (c.type() == CommandType.REPLAY_START) {
            Queue<Command> cmds = Command.load("save.txt");
            inputSource.deregister(this);
            cmdHistory.clear();
            cmdBuffer.clear();

            replaySource = new ReplayInputSource(cmds);
            replayDuration = replaySource.duration();
            replaySource.register(this);
            new Thread(replaySource).start();
            replayStart = System.currentTimeMillis();

            replay = true;
        } else if (c.type() == CommandType.REPLAY_END) {
            replaySource.deregister(this);
            inputSource.register(this);
            replay = false;
        } else {
            cmdHistory.add(c);

            if (c.type() == CommandType.SEED) {
                rand = new Random(c.args());
                world = new World(WIDTH, HEIGHT);
                world.splice(WorldGenerator.generate(world, rand));
                world.addEntity(new Entity(world, Tileset.AVATAR, rand));
                worldGenerated = true;
                renderState = RenderState.DIRTY;
                hudState = HudState.WORLD;
            } else if (c.type() == CommandType.TOGGLE_VIS) {
                world.toggleVisibility();
            } else if (c.type() == CommandType.INC_VIS) {
                world.modVisibility(1);
            } else if (c.type() == CommandType.DEC_VIS) {
                world.modVisibility(-1);
            } else if (CommandType.MOVEMENT.contains(c.type())) {
                Entity player = world.getEntity(0);
                Point target = player.pos().addDirection(c.type().direction());
                if (world.get(target) != Tileset.WALL) {
                    player.setPos(target);
                    renderState = RenderState.DIRTY;
                }
            }
        }

    }

    public void updateHud() {
        if (renderState == RenderState.INIT) {
            ter.initialize(WIDTH, HEIGHT + 2);
            renderState = RenderState.DIRTY;
        } else if (renderState == RenderState.DISABLED) {
            return;
        }

        var xCoord = (int) StdDraw.mouseX();
        var yCoord = (int) StdDraw.mouseY();

        List<Character> p = inputSource.partial();
        if (!hudPartial.equals(p)) {
            hudPartial = new ArrayList<>(p);
            renderState = RenderState.DIRTY;
        }


        if (hudState == HudState.WORLD) {
            if (world.has(xCoord, yCoord) && world.getPost(xCoord, yCoord) != hudHovered) {
                hudHovered = world.getPost(xCoord, yCoord);
                renderState = RenderState.DIRTY;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm");
            String currTime = formatter.format(new Date());
            if (!currTime.equals(hudDate)) {
                hudDate = currTime;
                renderState = RenderState.DIRTY;
            }

            if (turn != hudTurn) {
                hudTurn = turn;
                renderState = RenderState.DIRTY;
            }

            if (replay) {
                long current = replayDuration - (System.currentTimeMillis() - replayStart);

                String time = String.format("Time Left: %02d:%02d",
                                            TimeUnit.MILLISECONDS.toMinutes(current),
                                            TimeUnit.MILLISECONDS.toSeconds(current) -
                                                    TimeUnit.MILLISECONDS.toMinutes(current) * 60);
                if (!time.equals(hudReplayTime)) {
                    hudReplayTime = time;
                    renderState = RenderState.DIRTY;
                }
            }
        }
    }

    public void update() {
        updateHud();
    }

    private void renderHud() {
        StdDraw.setFont(defaultFont);
        StdDraw.setPenColor(StdDraw.WHITE);

        if (hudState == HudState.SPLASH) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setFont(titleFont);
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "WOOP-DEE-DOO");
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5, "(N) New Game");
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5 - 2, "(L) Load Game");
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5 - 4, "(R) Replay");
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5 - 6, "(:Q) Quit");

            String p = hudPartial.stream().map(Object::toString)
                .collect(Collectors.joining(""));

            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5 - 10, p);


        } else if (hudState == HudState.SEED) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setFont(titleFont);
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "New World");

            String p = hudPartial.stream().map(Object::toString)
                .collect(Collectors.joining(""));
            StdDraw.text(WIDTH / 2, HEIGHT * 3 / 5, "Enter Seed: " + p);
        } else {
            if (hudState == HudState.WORLD) {
                // Hovered tile
                StdDraw.textLeft(1, HEIGHT + 1, hudHovered.description());

                // Time
                StdDraw.textRight(WIDTH - 1, HEIGHT + 1, hudDate);

                // Turn number
                StdDraw.textRight(WIDTH - 1, HEIGHT + 0, "Turn " + hudTurn);
                String p = hudPartial.stream().map(Object::toString)
                    .collect(Collectors.joining(""));
                StdDraw.textRight(WIDTH - 1, HEIGHT + 0, p);
            }

            if (replay) {
                StdDraw.text(WIDTH / 2, HEIGHT + 1, "*REPLAY*");
                StdDraw.text(WIDTH / 2, HEIGHT + 0, hudReplayTime);
            }
        }
    }

    private void renderWorld() {
        if (worldGenerated) {
            StdDraw.setFont(defaultFont);
            ter.renderFrame(world.data());
        }
    }

    private void render() {
        if (renderState == RenderState.DIRTY) {
            StdDraw.clear();

            renderWorld();
            renderHud();

            StdDraw.show();

            renderState = renderState.ACTIVE;
        }
    }

    public void simulate() {
        while (!cmdBuffer.isEmpty()) {
            simulateWorld(cmdBuffer.remove());
            turn++;
        }
    }

    public void run() {
        while (running) {
            simulate();
            update();
            render();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        inputSource = new KeyboardInputSource();
        inputSource.register(this);
        new Thread(inputSource).start();

        run();

        System.exit(0);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        renderState = RenderState.DISABLED;

        inputSource = new StringInputSource(input);
        inputSource.register(this);
        inputSource.run();

        simulate();

        return world.data();
    }
}
