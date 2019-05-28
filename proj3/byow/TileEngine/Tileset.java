package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.GRAY, "I stand alone in the darkness.");
    public static final TETile WALL = new TETile('#', new Color(60, 150, 50), Color.darkGray,
            "The mossy stone wall looms far above.");
    public static final TETile FLOOR = new TETile(' ', new Color(128, 192, 128), Color.GRAY,
                                                  "The worn cobblestone extends into the darkness.");
    public static final TETile STALACTITE = new TETile('▲', Color.darkGray, Color.GRAY,
                                                  "A stalactite protrudes from the earth.");
    public static final TETile FLOWER = new TETile('❀', new Color(255, 179, 179), Color.GRAY, "A small flower pokes up from between the cracks.");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.GRAY, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile TEMP = new TETile('>', Color.blue, Color.black, "temp");
}


