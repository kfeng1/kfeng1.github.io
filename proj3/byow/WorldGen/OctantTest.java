package byow.WorldGen;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.util.*;
import byow.World.World;
import byow.util.input.Entity;
import byow.TileEngine.Tileset;
import java.util.Random;
import byow.World.ShadowLine;

//Using this to test out shit, with http://journal.stuffwithstuff.com/2015/09/07/what-the-hero-sees/
//I kinda just wrote everything in main cuz I was too lazy to figure out how to make things work with static context and whatnot

public class OctantTest {
    private static final int WIDTH = 96;
    private static final int HEIGHT = 48;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        World w = new World(WIDTH, HEIGHT);

        /*public boolean[][]octant(){
            var grid = new boolean[x()][y()];
            for(int i = 0; i<x(); i++){
                for(int j = 0; j<y(); j++){
                    grid[i][j] = false;
                }
            }
            Entity player = getEntities().get(0);
            Point location = player.pos();
            for (var row = 1; row < x() - location.x() ; row++) {
                for (var col = 0; col <= row; col++) {
                    var x = location.x() + col;
                    var y = location.y() - row;

                    grid[x][y] = true;
                }
            }
            return grid;
        }
        */
        Point location = new Point (40, 20);
        for (var row = 1; row < HEIGHT - location.y(); row++) {
            for (var col = 0; col <= row; col++) {
                var x = location.x() + col;
                var y = location.y() + row;

                w.set(x, y, Tileset.WALL);
            }
        }
        ter.renderFrame(w.data());
        Point p = location;
        var line = new ShadowLine();
        boolean fullShadow = false;
        for (var row = 1;; row++) {
            // Stop once we go out of bounds.
            Point pos = p.add(new Point(row, 0));
            if (!w.has(pos)) break;

            for (var col = 0; col <= row; col++) {
                pos = p.add(new Point(row, col));

                // If we've traversed out of bounds, bail on this row.
                if (!w.has(pos)) break;

                if (line.isFullShadow()) {
                    w.set(pos, Tileset.NOTHING);
                    ter.renderFrame(w.data());
                } else {
                    var projection = line.projectTile(row, col);

                    // Set the visibility of this tile.
                    var visible = !line.isInShadow(projection);
                    if(!visible){
                        w.set(pos, Tileset.NOTHING);
                        ter.renderFrame(w.data());
                    }
                    // Add any opaque tiles to the shadow map.
                    if (!visible && w.get(pos)==Tileset.WALL) {
                        line.add(projection);
                    }
                }
            }
        }




        ter.renderFrame(w.data());





    }
    /*public void refreshVisibility(Point p, World w) {
        //for (var octant = 0; octant < 8; octant++) {
            refreshOctant(p, w);
        //}
    }*/

    public World refreshOctant(Point p, World w ) {
        var line = new ShadowLine();
        boolean fullShadow = false;
        for (var row = 1; ; row++) {
            // Stop once we go out of bounds.
            var pos = p.add(new Point(row, 0));
            if (!w.has(pos)) break;

            for (var col = 0; col <= row; col++) {
                pos = p.add(new Point(row, col));

                // If we've traversed out of bounds, bail on this row.
                if (!w.has(pos)) break;

                if (line.isFullShadow()) {
                    w.set(pos, Tileset.NOTHING);
                } else {
                    var projection = line.projectTile(row, col);

                    // Set the visibility of this tile.
                    var visible = !line.isInShadow(projection);
                    if (!visible) {
                        w.set(pos, Tileset.NOTHING);
                    }
                    // Add any opaque tiles to the shadow map.
                    if (!visible && w.get(pos) == Tileset.WALL) {
                        line.add(projection);
                    }
                }
            }
        }
        return w;
    }

    //public static Point transformOctant(int row, int col){ //use octant after though
        /*Point location = new Point (0, 0);
        for (var row = 1; row < HEIGHT - location.y(); row++) {
            for (var col = 0; col <= row; col++) {
                var x = location.x() + col;
                var y = location.y() + row;

                world.set(x, y, Tileset.WALL);
            }
        }

    }*/


}
