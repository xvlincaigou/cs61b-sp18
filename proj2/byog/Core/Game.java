package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private Random RANDOM;
    private int wallNumbers;
    private TETile[][] tiles;

    private static class Position {
        int x,y;
        public Position(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }
    private Position getNotWallPosRandomly() {
        Position pos = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        while (tiles[pos.y][pos.x] == Tileset.WALL) {
            pos = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        }
        return pos;
    }

    private Position getNotWallLegalPosRandomlyByP(Position p) {
        List<Position> potentialPositions = new ArrayList<>();
        // Check positions around p: above, below, left, right
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int[] d : directions) {
            int newX = p.x + d[0];
            int newY = p.y + d[1];
            // Ensure the new position is within bounds, not a wall, and surrounded by no more than 2 walls
            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && tiles[newY][newX] != Tileset.WALL && countSurroundingWalls(newX, newY) <= 2) {
                if (!(newX == p.x && p.x == 0) && !(newY == p.y && p.y == 0) && !(newX == p.x && p.x == WIDTH - 1) && !(newY == p.y && p.y == HEIGHT - 1)) {
                    potentialPositions.add(new Position(newX, newY));
                }
            }
        }
        // If there are valid positions, return one at random
        if (!potentialPositions.isEmpty()) {
            return potentialPositions.get(RANDOM.nextInt(potentialPositions.size()));
        }
        // If no valid position is found, return null
        return null;
    }

    private int countSurroundingWalls(int x, int y) {
        int count = 0;
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] d : directions) {
            int newX = x + d[0];
            int newY = y + d[1];
            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && tiles[newY][newX] == Tileset.WALL) {
                count++;
            }
        }
        return count;
    }
    
    private static long convertSubstringToNumber(String input) {
        int sIndex = input.indexOf('s');
        String numberPart = input.substring(1, sIndex);
        return Integer.parseInt(numberPart);
    }

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
        wallNumbers = 0;
        tiles = new TETile[HEIGHT][WIDTH];
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        long seed = convertSubstringToNumber(input);
        RANDOM = new Random(seed);
        while (wallNumbers < 500) {
            Position start = getNotWallPosRandomly();
            Position p = start;
            while (p != null) {
                tiles[p.y][p.x] = Tileset.WALL;
                wallNumbers += 1;
                p = getNotWallLegalPosRandomlyByP(p);
            }
        }
        for (int i = 0; i < HEIGHT; ++ i) {
            for (int j = 0;j < WIDTH; ++ j) {
                if (tiles[i][j] !=Tileset.WALL) {
                    tiles[i][j] = Tileset.FLOOR;
                } else if (i > 0 && i < HEIGHT - 1 && j > 0 && j < WIDTH - 1){
                    boolean horizontalLine = tiles[i][j - 1] == Tileset.WALL && tiles[i][j + 1] == Tileset.WALL;
                    boolean verticalLine = tiles[i - 1][j] == Tileset.WALL && tiles[i + 1][j] == Tileset.WALL;
                    if (horizontalLine || verticalLine) {
                        tiles[i][j] = RANDOM.nextInt(25) == 0 ? Tileset.LOCKED_DOOR : Tileset.WALL;
                    }
                }
            }
        }
        ter.renderFrame(tiles);

        TETile[][] finalWorldFrame = this.tiles;
        return finalWorldFrame;
    }
}
