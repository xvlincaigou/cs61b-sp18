package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {
    public WorldState ws;
    public int moves;
    public SearchNode prev;

    public SearchNode(WorldState ws, int moves, SearchNode prev) {
        this.ws = ws;
        this.moves = moves;
        this.prev = prev;
    }
    @Override
    public int compareTo(SearchNode o) {
        return (this.moves + this.ws.estimatedDistanceToGoal()) - (o.moves + o.ws.estimatedDistanceToGoal());
    }
}
