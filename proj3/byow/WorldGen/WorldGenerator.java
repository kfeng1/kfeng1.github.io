package byow.WorldGen;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.util.Grid;
import byow.util.Rect;

import java.util.List;
import java.util.Random;

public class WorldGenerator {
    public static Grid<TETile> generate(Rect bounds, Random rand) {
        var grid = new Grid<TETile>(bounds.w(), bounds.h());
        grid.fill(Tileset.NOTHING);

        List<Rect> rooms = RectGenerator.generate(grid, rand);

        grid.splice(MazeGenerator.generate(grid, rooms, rand));

        WallGen.generate(grid, grid);

        ClutterGenerator.generate(grid, rooms, rand);

        return grid;
    }
}
