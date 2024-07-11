package lab11.graphs;

import edu.princeton.cs.algs4.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int s;
    private int t;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> q = new Queue<>();
        int s = maze.xyTo1D(1, 1);
        q.enqueue(s);
        marked[s] = true;
        announce();

        while (!q.isEmpty()) {
            int v = q.dequeue();
            marked[v] = true;
            announce();
            if (v == t)
                break;
            for (int neighbor : maze.adj(v)) {
                if (marked[neighbor])
                    continue;
                q.enqueue(neighbor);
                distTo[neighbor] = distTo[v] + 1;
                edgeTo[neighbor] = v;
            }
        }
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
    }


    @Override
    public void solve() {
        bfs();
    }
}

