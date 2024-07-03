package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    public static class Position {
        int x,y;
        public void Position(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }
    private static void addHexagonHelper(TETile[][] world, int x, int y, int size, TETile t) {
        for (int i = 0; i < size; ++ i) {
            world[x + i][y] = t;
        }
    }
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        for (int i = 0; i < s; ++ i) {
            addHexagonHelper(world, p.x - i, p.y - i, s + 2 * i, t);
            addHexagonHelper(world, p.x - i, p.y - 2 * s + i + 1, s + 2 * i, t);
        }
    }
    public static void init(TETile[][] world) {
        for (int i = 0; i < WIDTH; ++ i) {
            for (int j = 0; j < HEIGHT; ++ j) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    @Test
    public static void main(String[] args) {
        TERenderer renderer = new TERenderer();
        renderer.initialize(WIDTH,HEIGHT);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        init(randomTiles);

        TETile unit = Tileset.TREE;
        Position pos = new Position();
        pos.Position(8,8);
        addHexagon(randomTiles, pos, 4, unit);

        renderer.renderFrame(randomTiles);
    }
}
