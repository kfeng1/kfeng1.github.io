package byow.World;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.util.Grid;
import byow.util.input.Entity;
import byow.util.Point;
import byow.util.Direction;
import byow.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class World extends Grid<TETile> {
    private HashMap<Integer, Entity> entities;
    private int entityCount;

    //toggle to start with flood fill or not
    private boolean visibility = true;
    private int visRange = 5;

    public World(int width, int height) {
        super(width, height);
        fill(Tileset.NOTHING);

        entityCount = 0;
        entities = new HashMap();
    }

    private Grid<TETile> postProcess() {
        var grid = new Grid<TETile>(w, h);
        grid.splice(this);

        for (Entity e : entities.values()) {
            grid.set(e.pos().x(), e.pos().y(), e.tileType());
        }

        if (visibility) {
            grid.splice(floodVisibility(grid));
        }

        return grid;
    }

    public TETile[][] data() {
        TETile[][] array = new TETile[w][h];
        var grid = postProcess();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                array[i][j] = grid.get(i, j);
            }
        }

        return array;
    }

    public int addEntity(Entity e) {
        entities.put(entityCount, e);
        entityCount++;
        return entityCount;
    }

    public void toggleVisibility() {
        visibility = !visibility;
    }

    public void modVisibility(int i) {
        visRange += i;
        visRange = Math.max(0, Math.min(10, visRange));
    }

    public TETile getPost(int x, int y) {
        return postProcess().get(x, y);
    }

    public TETile getPost(Point p) {
        return postProcess().get(p);
    }

    private void flood(Point start, HashSet<Point> visible) {
        Queue<Pair<Point, Integer> > q = new LinkedList<>();
        q.add(Pair.of(start, 0));

        while (!q.isEmpty()) {
            var pair = q.remove();
            var p = pair.k();
            var r = pair.v();
            if (r == visRange)
                continue;

            if (visible.contains(p))
                continue;

            if (get(p) == Tileset.WALL) {
                visible.add(p);
                continue;
            }

            visible.add(p);
            for (Direction d : Direction.CARDINAL) {
                q.add(Pair.of(p.addDirection(d), r + 1));
            }
        }
    }

    private Grid<TETile> floodVisibility(Grid<TETile> grid) {
        Grid<TETile> blank = new Grid<TETile>(w, h);
        blank.fill(Tileset.NOTHING);

        HashSet<Point> visible = new HashSet<>();

        Point start = getEntity(0).pos();
        flood(start, visible);

        visible.stream().forEach(p -> blank.set(p, grid.get(p)));
        return blank;
    }

    public World shittyVisibility(int r) {
        var grid = data();
        Entity player = getEntity(0);
        World world = new World(grid.length, grid[0].length);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (player.pos().dist(i, j) <= r) {
                    world.set(i, j, get(i, j));
                }
            }
        }
        world.addEntity(player);
        return world;
    }

    public boolean[][]octant(){
        var grid = new boolean[x()][y()];
        for(int i = 0; i<x(); i++){
            for(int j = 0; j<y(); j++){
                grid[i][j] = false;
            }
        }
        Entity player = getEntity(0);
        Point location = player.pos();
        for (var row = 1; row < y() - location.y() ; row++) {
            for (var col = 0; col <= row; col++) {
                var x = location.x() + col;
                var y = location.y() + row;

                grid[x][y] = true;
            }
        }
        return grid;
    }

    public Entity getEntity(int i) {
        return entities.get(i);
    }
}
