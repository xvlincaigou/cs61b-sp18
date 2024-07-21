import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    private static Long findClosestNode(GraphDB g, double lon, double lat) {
        Long closestNode = null;
        double closestDist = Double.MAX_VALUE;
        for (long node : g.vertices()) {
            double dist = GraphDB.distance(g.nodes.get(node).lon, g.nodes.get(node).lat, lon, lat);
            if (dist < closestDist) {
                closestDist = dist;
                closestNode = node;
            }
        }
        return closestNode;
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        Long startNode = findClosestNode(g, stlon, stlat);
        Long endNode = findClosestNode(g, destlon, destlat);
        HashMap<Long, Double> distToStart = new HashMap<>();
        HashMap<Long, Double> distToDest = new HashMap<>();
        HashMap<Long, Long> nodeTo = new HashMap<>();
        PriorityQueue<Long> fringe = new PriorityQueue<>(
                Comparator.comparingDouble(v -> distToStart.get(v) + distToDest.get(v))
        );
        distToStart.put(startNode, GraphDB.distance(g.lon(startNode), g.lat(startNode), stlon, stlat));
        distToDest.put(startNode, GraphDB.distance(g.lon(startNode), g.lat(startNode), destlon, destlat));
        fringe.add(startNode);
        while (!fringe.isEmpty()) {
            Long v = fringe.poll();
            if (v.equals(endNode)) {
                break;
            }
            for (long w: g.adjacent(v)) {
                double newDistToStart = distToStart.get(v) + g.distance(v, w);
                if (!distToStart.containsKey(w) || newDistToStart < distToStart.get(w)) {
                    distToStart.put(w, newDistToStart);
                    nodeTo.put(w, v);
                    if (distToDest.containsKey(w)) {
                        fringe.remove(w);
                    } else {
                        distToDest.put(w, g.distance(w, endNode));
                    }
                    fringe.add(w);
                }
            }
        }
        Long currentNode = endNode;
        LinkedList<Long> path = new LinkedList<>();
        while (nodeTo.containsKey(currentNode)) {
            path.addFirst(currentNode);
            currentNode = nodeTo.get(currentNode);
        }
        path.addFirst(currentNode);
        return new ArrayList<>(path);
    }

    private static int findChangeWayIndex(GraphDB g, List<Long> route, int currNodeIndex, String prevWay) {
        if (currNodeIndex == route.size() - 1)
            return currNodeIndex;
        int nextNodeIndex = currNodeIndex + 1;
        long currNode = route.get(currNodeIndex), nextNode = route.get(nextNodeIndex);
        // 得到edges里面的元素中to的值等于nextNode的元素
        String currWay = g.nodes.get(currNode).edges.stream().filter(e -> e.to == nextNode).findFirst().get().name;
        if (!currWay.equals(prevWay))
            return currNodeIndex;
        return findChangeWayIndex(g, route, nextNodeIndex, currWay);
    }

    private static double distanceBetweenNodes(GraphDB g, int index1, int index2, List<Long> route) {
        if (index1 == index2)
            return 0.0;
        long node1 = route.get(index1), node2 = route.get(index1 + 1);
        return g.distance(node1, node2) + distanceBetweenNodes(g, index1 + 1, index2, route);
    }

    private static final double cos15 = Math.cos(Math.toRadians(15));
    private static final double cos30 = Math.cos(Math.toRadians(30));
    private static final double cos100 = Math.cos(Math.toRadians(100));

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        // Initialize the directions list
        LinkedList<NavigationDirection> directions = new LinkedList<>();

        // If the route is empty or only has one node, return an empty list
        if (route.size() < 2)
            return directions;

        // Initialize the variables
        int prevNodeIndex = - 1, currNodeIndex = 0, changeWayIndex = 0;
        String way = g.nodes.get(route.get(currNodeIndex)).edges.stream().filter(e -> e.to == route.get(1)).findFirst().get().name;

        // Iterate through the route
        while (currNodeIndex != route.size() - 1) {
            // Find the index of the node where the way changes
            changeWayIndex = findChangeWayIndex(g, route, currNodeIndex, way);
            if (prevNodeIndex == -1) {
                directions.add(new NavigationDirection(NavigationDirection.START, way, distanceBetweenNodes(g, currNodeIndex, changeWayIndex, route)));
            } else {
                long prevNode = route.get(prevNodeIndex), currNode = route.get(currNodeIndex), nextNode = route.get(changeWayIndex);
                double prevBearing = GraphDB.bearing(g.lon(prevNode), g.lat(prevNode), g.lon(currNode), g.lat(currNode)), currBearing = GraphDB.bearing(g.lon(currNode), g.lat(currNode), g.lon(nextNode), g.lat(nextNode));
                double cosBearingDiff = Math.cos(Math.toRadians(currBearing - prevBearing));
                double distance = g.distance(currNode, nextNode);
                if (cosBearingDiff >= cos15) {
                    directions.add(new NavigationDirection(NavigationDirection.STRAIGHT, way, distance));
                } else if (cosBearingDiff >= cos30) {
                    if (Math.sin(Math.toRadians(currBearing - prevBearing)) > 0) {
                        directions.add(new NavigationDirection(NavigationDirection.SLIGHT_LEFT, way, distance));
                    } else {
                        directions.add(new NavigationDirection(NavigationDirection.SLIGHT_RIGHT, way, distance));
                    }
                } else if (cosBearingDiff >= cos100) {
                    if (Math.sin(Math.toRadians(currBearing - prevBearing)) > 0) {
                        directions.add(new NavigationDirection(NavigationDirection.LEFT, way, distance));
                    } else {
                        directions.add(new NavigationDirection(NavigationDirection.RIGHT, way, distance));
                    }
                } else {
                    if (Math.sin(Math.toRadians(currBearing - prevBearing)) > 0) {
                        directions.add(new NavigationDirection(NavigationDirection.SHARP_LEFT, way, distance));
                    } else {
                        directions.add(new NavigationDirection(NavigationDirection.SHARP_RIGHT, way, distance));
                    }
                }
            }
            currNodeIndex = changeWayIndex;
            prevNodeIndex = currNodeIndex - 1;
            if (currNodeIndex == route.size() - 1)
                break;
            int finalCurrNodeIndex = currNodeIndex;
            way = g.nodes.get(route.get(currNodeIndex)).edges.stream().filter(e -> e.to == route.get(finalCurrNodeIndex + 1)).findFirst().get().name;
        }
        return directions;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public NavigationDirection(int direction, String way, double distance) {
            this.direction = direction;
            this.way = way;
            this.distance = distance;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
