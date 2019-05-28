package byow.WorldGen;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.util.*;
import byow.World.World;

import java.util.Random;

public class RectangleDemo {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 48;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        long seed = 889324;
        var rand = new Random(seed);

        World world = new World(WIDTH, HEIGHT);
        Grid<TETile> generated = WorldGenerator.generate(new Rect(0, 0, WIDTH, HEIGHT), rand);
        world.splice(generated);
        ter.renderFrame(world.data());

    }

}
