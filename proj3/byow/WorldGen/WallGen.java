package byow.WorldGen;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.util.Point;
import byow.util.Rect;
import byow.util.Grid;

public class WallGen {
    public static void generate(Rect bounds, Grid<TETile> grid) {
        for (int x = grid.x(); x < grid.w() - 1; x++) {
            for (int y = grid.y(); y < grid.h() - 1; y++) {
                if (grid.get(x, y).equals(Tileset.FLOOR)) {
                    placeWallsAround(new Point(x, y), grid);
                }
            }
        }
    }

    private static void placeWallsAround(Point p, Grid<TETile> grid) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (grid.get(p.x() + x, p.y() + y).equals(Tileset.NOTHING)) {
                    grid.set(p.x() + x, p.y() + y, Tileset.WALL);
                }
            }
        }
    }


}
