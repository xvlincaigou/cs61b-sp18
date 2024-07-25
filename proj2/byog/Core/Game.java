package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private Random RANDOM;
    private static final int wallNumbersMax = 500;
    private final TETile[][] tiles;
    private final UnionFind unionFind;

    private static class Position {
        int x,y;
        public Position(int x,int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return 50 * x + y;
        }
    }

    private static class UnionFind {
        private final Map<Position, Position> parent;
        private final Map<Position, Integer> rank;
        public int maxRank;

        public UnionFind() {
            this.parent = new HashMap<>();
            this.rank = new HashMap<>();
        }

        public void makeSet(Position p) {
            parent.put(p, p);
            rank.put(p, 1);
        }

        public Position find(Position p) {
            if (!parent.get(p).equals(p)) {
                parent.put(p, find(parent.get(p)));
            }
            return parent.get(p);
        }

        public int getRank(Position p) {
            return rank.get(find(p));
        }

        public void union(Position p1, Position p2) {
            Position root1 = find(p1);
            Position root2 = find(p2);

            if (!root1.equals(root2)) {
                int rank1 = rank.get(root1);
                int rank2 = rank.get(root2);

                if (rank1 >= rank2) {
                    parent.put(root2, root1);
                    rank.put(root1, rank1 + rank2);
                } else {
                    parent.put(root1, root2);
                    rank.put(root2, rank1 + rank2);
                }

                maxRank = Math.max(rank1 + rank2, maxRank);
            }
        }
    }

    private Position getStartWall() {
        Position pos = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        while (tiles[pos.y][pos.x] == Tileset.WALL) {
            pos = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        }
        return pos;
    }

    private Position getNotWallLegalPosRandomlyByP(Position p) {
        List<Position> potentialPositions = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int[] d : directions) {
            int newX = p.x + d[0];
            int newY = p.y + d[1];
            // Ensure the new position is within bounds, not a wall, and surrounded by no more than 2 walls
            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT &&
                    tiles[newY][newX] != Tileset.WALL &&
                    getSurroundingPositions(newX, newY, Tileset.WALL).size() < 2 &&
                    !(newX == p.x && p.x == 0) && !(newY == p.y && p.y == 0) &&
                    !(newX == p.x && p.x == WIDTH - 1) && !(newY == p.y && p.y == HEIGHT - 1)
            ) {
                    potentialPositions.add(new Position(newX, newY));
            }
        }
        if (!potentialPositions.isEmpty()) {
            return potentialPositions.get(RANDOM.nextInt(potentialPositions.size()));
        }
        return null;
    }

    private ArrayList<Position> getSurroundingPositions(int x, int y, TETile tile) {
        ArrayList<Position> surroundingFloors = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int[] d : directions) {
            int newX = x + d[0];
            int newY = y + d[1];
            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT &&
                    Objects.equals(tiles[newY][newX], tile)) {
                surroundingFloors.add(new Position(newX, newY));
            }
        }
        return surroundingFloors;
    }

    private static long convertSubstringToNumber(String input) {
        int sIndex = input.indexOf('s');
        String numberPart = input.substring(1, sIndex);
        return Long.parseLong(numberPart);
    }

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
        tiles = new TETile[HEIGHT][WIDTH];
        unionFind = new UnionFind();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    // 不断地造墙
    private void buildWall() {
        int wallNumbers = 0;
        while (wallNumbers < wallNumbersMax) {
            Position p = getStartWall();
            while (p != null) {
                tiles[p.y][p.x] = Tileset.WALL;
                wallNumbers += 1;
                p = getNotWallLegalPosRandomlyByP(p);
            }
        }
    }

    // 不断地造地板
    private void buildFloor(int i ,int j) {
        if (tiles[i][j] !=Tileset.WALL) {
            tiles[i][j] = Tileset.FLOOR;
            unionFind.makeSet(new Position(j, i));
            if (0 <= j - 1 && tiles[i][j - 1] == Tileset.FLOOR) {
                unionFind.union(new Position(j, i), new Position(j - 1, i));
            }
            if (0 <= i - 1 && tiles[i - 1][j] == Tileset.FLOOR) {
                unionFind.union(new Position(j, i), new Position(j, i - 1));
            }
        }
    }

    private void unifyFloor(int i, int j) {
        ArrayList<Position> surroundingFloors = getSurroundingPositions(j, i, Tileset.FLOOR);
        List<Position> floorsToUnify = surroundingFloors.stream().filter(p -> unionFind.getRank(p) < unionFind.maxRank).toList();

        if (floorsToUnify.isEmpty())
            return;
        tiles[i][j] = Tileset.FLOOR;
        unionFind.makeSet(new Position(j, i));
        for (Position p : floorsToUnify)
            unionFind.union(new Position(j, i), p);
        surroundingFloors.removeAll(floorsToUnify);
        if (!surroundingFloors.isEmpty())
            unionFind.union(new Position(j, i), surroundingFloors.get(0));
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
        // 初始化随机数。
        long seed = convertSubstringToNumber(input);
        RANDOM = new Random(seed);
        // 建造墙壁。
        buildWall();
        // 建造地板。
        for (int i = 0; i < HEIGHT; ++ i) {
            for (int j = 0;j < WIDTH; ++ j) {
                buildFloor(i, j);
            }
        }
        // 把仍然分离的地板连接起来。
        // 具体的方法就是如果你现在不在最大的那个集合里面，你你就需要链接。
        for (int i = 0; i < HEIGHT; ++ i) {
            for (int j = 0; j < WIDTH; ++ j) {
                if (tiles[i][j] == Tileset.WALL) {
                    unifyFloor(i, j);
                }
            }
        }
        // 渲染
        ter.renderFrame(tiles);
        // 返回
        return this.tiles;
    }
}
