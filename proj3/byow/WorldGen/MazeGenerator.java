package byow.WorldGen;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.RandomUtils;
import byow.util.*;
import javax.swing.*;
import java.util.*;

public class MazeGenerator {
    //@source https://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/

    public static Grid<TETile> generate(Rect bounds, List<Rect> rooms, Random rand) {
        var grid = new Grid<TETile>(bounds.w(), bounds.h());
        grid.fill(Tileset.NOTHING);

        for (Rect r : rooms) {
            grid.fill(r, Tileset.UNLOCKED_DOOR);
        }

        double sameDirChance = RandomUtils.uniform(rand, 0.4, 1);
        double extraConnectionChance = RandomUtils.uniform(rand, 0.01, 0.08);

        for (int x = 1; x < grid.w() - 1; x += 2) {
            for (int y = 1; y < grid.h() - 1; y += 2) {
                Point currPos = new Point(x, y);
                if (!isNothing(currPos, grid)) {
                    continue;
                }
                genMazePortion(currPos, grid, sameDirChance, rand);
            }
        }

        var connectionsTracker = new RoomConnections(grid);

        List<Point> connectorTiles = new ArrayList<>();
        for (int x = 0; x < grid.w(); x += 1) {
            for (int y = 0; y < grid.h(); y += 1) {
                if (connectionsTracker.allConnected()) {
                    break;
                }
                if (grid.get(x, y).equals(Tileset.NOTHING)) {
                    connectorTiles.add(new Point(x, y));
                }
            }
        }

        while (!connectorTiles.isEmpty()) {
            int index = RandomUtils.uniform(rand, connectorTiles.size());
            addConnectors(connectorTiles.remove(index), grid,
                    extraConnectionChance, connectionsTracker, rand);
        }

        for (int x = 0; x < grid.w(); x++) {
            for (int y = 0; y < grid.h(); y++) {
                if (grid.get(x, y).equals(Tileset.FLOOR)) {
                    clearDeadEnds(new Point(x, y), grid);
                }
            }
        }

        for (Rect r : rooms) {
            grid.fill(r, Tileset.FLOOR);
        }

        return grid;
    }

    private static void genMazePortion(Point start, Grid<TETile> grid,
                                       double sameDirChance, Random rand) {
        LinkedList<Point> tiles = new LinkedList<>();
        Direction lastDir = null;

        placeMazePath(start, grid);
        tiles.add(start);
        while (!tiles.isEmpty()) {
            Point tile = tiles.getLast();
            ArrayList<Direction> tileableNeighbors = new ArrayList<>();

            for (Direction d : Direction.CARDINAL) {
                if (canPlaceMazeTile(tile, grid, d)) {
                    tileableNeighbors.add(d);
                }
            }

            if (!tileableNeighbors.isEmpty()) {
                Direction d;
                if (tileableNeighbors.contains(lastDir) && RandomUtils.roll(rand, sameDirChance)) {
                    d = lastDir;
                } else {
                    d = RandomUtils.element(rand, tileableNeighbors);
                }

                placeMazePath(tile.add(d.p()), grid);
                placeMazePath(tile.add(d.p()).add(d.p()), grid);

                tiles.add(tile.add(d.p()).add(d.p()));
                lastDir = d;

            } else {
                tiles.removeLast();
            }
        }
    }

    private static boolean canPlaceMazeTile(Point p, Grid<TETile> grid, Direction d) {
        if (!validateCoordinate(p.add(d.p()).add(d.p()).add(d.p()), grid)) {
            return false;
        }
        return isNothing(p.add(d.p()).add(d.p()), grid);

    }


    private static void placeMazePath(Point p, Grid<TETile> grid) {
        grid.set(p.x(), p.y(), Tileset.FLOOR);
    }

    private static boolean validateCoordinate(Point p, Grid<TETile> grid) {
        return grid.has(p);
    }

    private static Point singleNeighbor(Point p, Grid<TETile> grid) {
        List<Direction> nonEmpty = nonEmptyNeighbors(p, grid);
        int length = nonEmpty.size();
        switch (length) {
            case 0:
                return p;
            case 1:
                return p.addDirection(nonEmpty.get(0));
            default:
                return null;
        }
    }

    private static void clearDeadEnds(Point p, Grid<TETile> grid) {
        while (singleNeighbor(p, grid) != null) {
            Point temp = p;
            p = singleNeighbor(p, grid);
            grid.set(temp.x(), temp.y(), Tileset.NOTHING);
        }
    }

    private static boolean isNothing(Point p, Grid<TETile> grid) {
        return grid.get(p.x(), p.y()).equals(Tileset.NOTHING);
    }

    private static List<Direction> nonEmptyNeighbors(Point p, Grid<TETile> grid) {
        List<Direction> toReturn = new ArrayList<>();
        for (Direction d : Direction.CARDINAL) {
            Point adj = p.add(d.p());
            if (validateCoordinate(adj, grid)
                    && !grid.get(adj.x(), adj.y()).equals(Tileset.NOTHING)) {
                toReturn.add(d);
            }
        }
        return toReturn;
    }

    private static void addConnectors(Point p, Grid<TETile> grid, double extraConnectionChance,
                                      RoomConnections connectionsTracker, Random rand) {
        List<Direction> adj = nonEmptyNeighbors(p, grid);
        if (adj.size() == 2 && adj.get(0).opposite() == adj.get(1)) {
            Point p1 = p.addDirection(adj.get(0));
            Point p2 = p.addDirection(adj.get(1));
            if (differentTiles(p1, p2, grid)) {
                if (!connectionsTracker.pointsConnected(p1, p2)) {
                    connectionsTracker.open(p.x(), p.y());
                    placeMazePath(p, grid);
                } else {
                    if (RandomUtils.roll(rand, extraConnectionChance)) {
                        connectionsTracker.open(p.x(), p.y());
                        placeMazePath(p, grid);
                    }
                }
            }
        }
    }

    private static boolean differentTiles(Point p1, Point p2, Grid<TETile> grid) {
        return !grid.get(p1).equals(grid.get(p2));
    }
}

