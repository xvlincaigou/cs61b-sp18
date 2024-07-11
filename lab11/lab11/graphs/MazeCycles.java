package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleDetected = false;
    public MazeCycles(Maze m) {
        super(m);
    }

    private void dfs(int v, int parent) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(w, v);
                if (cycleDetected) return;
            } else if (w != parent) {
                cycleDetected = true;
                announce();
                return;
            }
        }
    }

    @Override
    public void solve() {
        for (int v = 0; v < maze.V(); v++) {
            if (!marked[v]) {
                dfs(v, -1);
                if (cycleDetected) break;
            }
        }
    }
}

