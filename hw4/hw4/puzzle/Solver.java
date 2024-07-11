package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;

public class Solver {
    private LinkedList<WorldState> solution;

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        SearchNode current = null;
        while (!pq.isEmpty()) {
            current = pq.delMin();
            if (current.ws.isGoal())
                break;
            for (WorldState neighbor : current.ws.neighbors()) {
                if (current.prev == null || !neighbor.equals(current.prev.ws)) {
                    pq.insert(new SearchNode(neighbor, current.moves + 1, current));
                }
            }
        }
        solution = new LinkedList<>();
        while (current != null) {
            solution.addFirst(current.ws);
            current = current.prev;
        }
    }

    public int moves() {
        return solution.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}