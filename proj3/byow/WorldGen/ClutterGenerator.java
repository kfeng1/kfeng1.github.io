package byow.WorldGen;

import byow.util.Grid;
import byow.util.Rect;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.RandomUtils;
import java.util.List;
import java.util.Random;

public class ClutterGenerator {
    public static void generate(Grid<TETile> grid, List<Rect> rooms, Random rand) {
        grid.streamPoints().forEach(p -> {
                if (grid.get(p) == Tileset.FLOOR) {
                    if (RandomUtils.roll(rand, 0.005)) {
                        grid.set(p, Tileset.FLOWER);
                    }

                    if (RandomUtils.roll(rand, 0.01)) {
                        grid.set(p, Tileset.STALACTITE);
                    }
                }
            });
    }
}
