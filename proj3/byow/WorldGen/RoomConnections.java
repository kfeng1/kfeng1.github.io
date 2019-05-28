package byow.WorldGen;

import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;
import byow.util.*;



public class RoomConnections {

    private int numOpen;
    private boolean[][] openings;
    private UnionFind paths;
    private int top;
    private int rows;
    private int cols;

    public RoomConnections(Grid world) {
        numOpen = 0;
        rows = world.w();
        cols = world.h();
        openings = new boolean[rows][cols];
        int totalSpaces = rows * cols;
        paths = new UnionFind(totalSpaces + 1);
        top = totalSpaces;
        Point start = null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                TETile tile = (TETile) world.get(i, j);
                if (tile != Tileset.NOTHING) {
                    open(i, j);
                    if (start == null) {
                        start = new Point(i, j);
                    }
                }

            }
        }
        paths.union(top, convert(start.x(), start.y()));
    }

    //returns if all paths - rooms and halls are connected to each other
    public boolean allConnected() {
        return paths.sizeOf(top) - 1 == numOpen;
    }


    //returns if these two specific rooms/rectangles are connected to each other.

    public boolean pointsConnected(Point p, Point p2) {
        return paths.connected(convert(p.x(), p.y()), convert(p2.x(), p2.y()));
    }


    private int convert(int row, int col) {
        return row * cols + col;
    }


    private void connectUp(int row, int col) {
        if (row == 0) {
            return;
        }
        if (openings[row - 1][col]) {
            int a = convert(row, col);
            int b = convert(row - 1, col);
            paths.union(a, b);
        }
    }

    private void connectDown(int row, int col) {
        if (row == rows - 1) {
            return;
        }
        if (openings[row + 1][col]) {
            int a = convert(row, col);
            int b = convert(row + 1, col);
            paths.union(a, b);
        }
    }

    private void connectRight(int row, int col) {
        if (col == cols - 1) {
            return;
        }
        if (openings[row][col + 1]) {
            int a = convert(row, col);
            int b = convert(row, col + 1);
            paths.union(a, b);
        }
    }

    private void connectLeft(int row, int col) {
        if (col == 0) {
            return;
        }
        if (openings[row][col - 1]) {
            int a = convert(row, col);
            int b = convert(row, col - 1);
            paths.union(a, b);
        }
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        numOpen += 1;
        connectUp(row, col);
        connectDown(row, col);
        connectLeft(row, col);
        connectRight(row, col);
        openings[row][col] = true;

    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index Out of Bounds");
        }
        return openings[row][col];
    }

}
