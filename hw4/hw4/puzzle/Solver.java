package hw4.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Solver {
    private ArrayList<WorldState> solution = new ArrayList<>();
    private int moves = 0;

    public Solver(WorldState initial) {
        HashMap<WorldState, WorldState> edgeTo = new HashMap<>();
        HashMap<WorldState, Integer> distTo = new HashMap<>();
        PriorityQueue<WorldState> pq = new PriorityQueue<>((a, b) -> {
            return distTo.get(a) + a.estimatedDistanceToGoal() - distTo.get(b) - b.estimatedDistanceToGoal();
        });

        pq.add(initial);
        distTo.put(initial, 0);

        while (!pq.isEmpty()) {
            WorldState current = pq.poll();

            if (current.isGoal()) {
                for (WorldState state = current; state != null; state = edgeTo.get(state)) {
                    solution.add(0, state);
                }
                moves = solution.size() - 1;
                return;
            }

            for (WorldState neighbor : current.neighbors()) {
                int tentativeDist = distTo.get(current) + 1;
                if (!distTo.containsKey(neighbor) || tentativeDist < distTo.get(neighbor)) {
                    distTo.put(neighbor, tentativeDist);
                    edgeTo.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}