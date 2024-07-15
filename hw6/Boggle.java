import edu.princeton.cs.introcs.In;

import java.util.*;

public class Boggle {

    private static class State {
        private static class Coord {
            int x;
            int y;

            public Coord(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Coord coord = (Coord) obj;
                return x == coord.x && y == coord.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
        Coord coord;
        String prefix;
        ArrayList<Coord> previous;

        public State(int x, int y) {
            this.coord = new Coord(x, y);
            this.prefix = "";
            this.previous = new ArrayList<>();
        }

        public List<State> lawyableNeighbors(int m, int n) {
            int x = coord.x, y = coord.y;
            List<State> neighbors = new ArrayList<>();
            int[][] directions = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1},           {0, 1},
                    {1, -1}, {1, 0}, {1, 1}
            };

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newX < n && newY >= 0 && newY < m) { // 确保新坐标在边界内
                    if (!previous.contains(new Coord(newX, newY))) {
                        neighbors.add(new State(newX, newY));
                    }
                }
            }

            return neighbors;
        }
    }
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {

        if (k <= 0) {
            throw new IllegalArgumentException("k should be positive");
        }

        // 从文件中读取单词
        In in = new In(dictPath);
        if (in.isEmpty()) {
            throw new IllegalArgumentException("Dictionary file is empty");
        }
        List<String> wordList = new ArrayList<>();

        while (!in.isEmpty()) {
            String word = in.readString();
            wordList.add(word);
        }

        // 创建 Trie 并插入单词
        Trie trie = new Trie();
        trie.buildTrie(wordList);

        // read Boggle board of unknown dimensions
        in = new In(boardFilePath);
        List<String> lines = new ArrayList<>();
        while (!in.isEmpty()) {
            lines.add(in.readLine());
        }
        int numRows = lines.size();
        int numCols = lines.get(0).length();
        char[][] board = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            if (lines.get(i).length() != numCols) {
                throw new IllegalArgumentException("Board is not rectangular");
            }
            for (int j = 0; j < numCols; j++) {
                board[i][j] = lines.get(i).charAt(j);
            }
        }

        // 用于存储结果
        TreeSet<String> result = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (a.length() != b.length()) {
                    return b.length() - a.length();
                } else {
                    return a.compareTo(b);
                }
            }
        });

        for (int i = 0; i < numRows; ++ i) {
            for (int j = 0; j < numCols; ++ j) {
                State state = new State(j, i);
                state.prefix = String.valueOf(board[i][j]);
                state.previous.add(state.coord);
                Stack<State> stack = new Stack<>();
                stack.push(state);
                while (!stack.isEmpty()) {
                    State current = stack.pop();
                    if (trie.isValidPrefix(current.prefix)) {
                        if (trie.isValidWord(current.prefix) && current.prefix.length() >= 3 && !result.contains(current.prefix)) {
                            result.add(current.prefix);
                        }
                        for (State neighbor : current.lawyableNeighbors(numRows, numCols)) {
                            neighbor.prefix = current.prefix + board[neighbor.coord.y][neighbor.coord.x];
                            neighbor.previous.addAll(current.previous);
                            neighbor.previous.add(neighbor.coord);
                            stack.push(neighbor);
                        }
                    }
                }
            }
        }

        List<String> res = new ArrayList<>();
        for (int i = 0; i < k; ++ i) {
            if (result.isEmpty()) {
                break;
            }
            res.add(result.pollFirst());
        }

        return res;
    }

    public static void main(String[] args) {

        // read Boggle board of unknown dimensions
        In in = new In("smallBoard.txt");
        List<String> lines = new ArrayList<>();
        while (!in.isEmpty()) {
            lines.add(in.readLine());
        }
        int numRows = lines.size();
        int numCols = lines.get(0).length();
        char[][] board1 = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            if (lines.get(i).length() != numCols) {
                throw new IllegalArgumentException("Board is not rectangular");
            }
            for (int j = 0; j < numCols; j++) {
                board1[i][j] = lines.get(i).charAt(j);
            }
        }

        System.out.println("Size of board: " + board1.length + " x " + board1[0].length);
        long start1 = System.currentTimeMillis();
        List<String> res1 = solve(7, "smallBoard.txt");
        long end1 = System.currentTimeMillis();
        long timeSpan1 = end1 - start1;
        System.out.println(res1.toString());
        System.out.println("time to solve the boggle: " + timeSpan1);
        System.out.println();

        in = new In("smallBoard2.txt");
        List<String> _lines = new ArrayList<>();
        while (!in.isEmpty()) {
            _lines.add(in.readLine());
        }
        int _numRows = _lines.size();
        int _numCols = _lines.get(0).length();
        char[][] board2 = new char[_numRows][_numCols];
        for (int i = 0; i < _numRows; i++) {
            if (_lines.get(i).length() != _numCols) {
                throw new IllegalArgumentException("Board is not rectangular");
            }
            for (int j = 0; j < _numCols; j++) {
                board2[i][j] = _lines.get(i).charAt(j);
            }
        }

        System.out.println("Size of board: " + board2.length + " x " + board2[0].length);
        long start2 = System.currentTimeMillis();
        List<String> res2 = solve(7, "smallBoard2.txt");
        long end2 = System.currentTimeMillis();
        System.out.println(res2.toString());
        long timeSpan2 = end2 - start2;
        System.out.println("time to solve the boggle: " + timeSpan2);
        System.out.println();

        int sizeRatio = (board1.length / board2.length) * (board1[0].length / board2[0].length);
        System.out.println("board1 is about x" + sizeRatio + " larger(smaller) than board2");
        System.out.println("Linearly extrapolating from the board2 runtime, "
                + "we would expect a runtime of "
                + timeSpan2 + " x " + sizeRatio + " = " + (timeSpan2 * sizeRatio));
    }

}
