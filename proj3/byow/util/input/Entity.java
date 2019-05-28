package byow.util.input;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.util.Grid;
import byow.util.Point;
import byow.World.World;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Entity {
    private Point location;
    private TETile tileType;
    private TETile lastStep;
    private int id;

    public Entity(Grid<TETile> grid, TETile tile, Random rand) {
        List<Point> floorTiles = grid.streamPairs().filter(p ->
                p.b().equals(Tileset.FLOOR)).map(p ->
                p.a()).collect(Collectors.toList());
        int index = RandomUtils.uniform(rand, floorTiles.size());
        location = floorTiles.get(index);
        tileType = tile;
        lastStep = Tileset.FLOOR;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point pos() {
        return location;
    }

    public void setPos(Point p) {
        location = p;
    }

    public TETile tileType() {
        return tileType;
    }

    public TETile getLastStep() {
        return lastStep;
    }

    public void setLastStep(TETile tile) {
        lastStep = tile;
    }



}
