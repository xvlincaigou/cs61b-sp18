import edu.princeton.cs.introcs.In;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Boggle {

    private static class State {
        private static class Coord {
            int x;
            int y;

            public Coord(int x, int y) {
                this.x = x;
                this.y = y;
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
            return new ArrayList<>(List.of(new State[]{
                    new State((x - 1) % n, (y - 1) % m),
                    new State((x - 1) % n, y),
                    new State((x - 1) % n, (y + 1) % m),
                    new State(x, (y - 1) % m),
                    new State(x, (y + 1) % m),
                    new State((x + 1) % n, (y - 1) % m),
                    new State((x + 1) % n, y),
                    new State((x + 1) % n, (y + 1) % m)
            })).stream().filter(o -> !previous.contains(o.coord)).toList();
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
        // 从文件中读取单词
        In in = new In(dictPath);
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
            for (int j = 0; j < numCols; j++) {
                board[i][j] = lines.get(i).charAt(j);
            }
        }

        // 用于存储结果
        PriorityQueue<String> result = new PriorityQueue<>((a, b) -> {
            if (a.length() != b.length()) {
                return b.length() - a.length();
            } else {
                return a.compareTo(b);
            }
        });

        for (int i = 0; i < numRows; ++ i) {
            for (int j = 0; j < numCols; ++ j) {
                State state = new State(i, j);
                state.prefix = String.valueOf(board[i][j]);
                state.previous.add(state.coord);
                Stack<State> stack = new Stack<>();
                stack.push(state);
                while (!stack.isEmpty()) {
                    State current = stack.pop();
                    if (trie.isValidPrefix(current.prefix)) {
                        if (trie.isValidWord(current.prefix) && current.prefix.length() >= 3) {
                            result.add(current.prefix);
                        }
                        for (State neighbor : current.lawyableNeighbors(numRows, numCols)) {
                            neighbor.prefix = current.prefix + board[neighbor.coord.x][neighbor.coord.y];
                            neighbor.previous.addAll(current.previous);
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
            res.add(result.poll());
        }

        return res;
    }
}
