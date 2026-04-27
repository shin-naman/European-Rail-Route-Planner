import java.io.File;
import java.io.IOException;
import java.util.*;

public class Backend implements  BackendInterface {

    private GraphADT<String, Double> graph;
    private List<String> locations;

    /**
     * Implementing classes should support the constructor below.
     * @param graph object to store the backend's graph data
     */
    public Backend(GraphADT<String,Double> graph) {
        this.graph = graph;
        this.locations = new ArrayList<>();
    }

    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    public void loadGraphData(String filename) throws IOException {
        for (String location : new ArrayList<>(locations)) {
            graph.removeNode(location);
        }
        locations.clear();

        Set<String> locationSet = new HashSet<>();

        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.contains("->")) {
                    continue;
                }

                try {
                    String[] parts = line.split("->");
                    if (parts.length < 2) {
                        throw new IOException("Malformed line in dot file: " + line);
                    }

                    String start = parts[0].trim().replace("\"", "");
                    String rest = parts[1].trim();

                    int openBracket = rest.indexOf("[");
                    int equals = rest.indexOf("=");
                    int closeBracket = rest.indexOf("]");

                    if (openBracket < 0 || equals < 0 || closeBracket < 0 || equals > closeBracket) {
                        throw new IOException("Malformed line in dot file: " + line);
                    }

                    String end = rest.substring(0, openBracket).trim().replace("\"", "");
                    String weightStr = rest.substring(equals + 1, closeBracket).trim();
                    Double weight = Double.valueOf(weightStr);

                    if (locationSet.add(start)) {
                        locations.add(start);
                        graph.insertNode(start);
                    }

                    if (locationSet.add(end)) {
                        locations.add(end);
                        graph.insertNode(end);
                    }

                    graph.insertEdge(start, end, weight);

                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    throw new IOException("Malformed line in dot file: " + line, e);
                }
            }
        }
    }

    /**
     * Returns a list of all locations in the graph.
     * @return list of all location names
     */
    public List<String> getListOfAll() {
        return new ArrayList<>(locations);
    }

    /**
     * Return the sequence of locations along the shortest path from start to
     * end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the nodes along the shortest path from start to end,
     *         or an empty list if no such path exists
     */
    public List<String> findLocationsOnShortestPath(String start, String end) {
        try {
            return graph.shortestPathData(start, end);
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Return the times in minutes between each two nodes on the shortest path
     * from start to end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the times in minutes between two nodes along the
     * shortest path from start to end, or an empty list if no such path exists
     */
    public List<Double> findTimesOnShortestPath(String start, String end) {
        List<Double> times = new ArrayList<>();

        try {
            List<String> path = graph.shortestPathData(start, end);
            for (int i = 0; i < path.size() - 1; i++) {
                double weight = graph.getEdge(path.get(i), path.get(i+1));
                times.add(weight);
            }
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }

        return times;
    }

    /**
     * Returns the list of locations furthest along any shortest path from
     * start to any of the reachable locations in the graph.
     * @param start the location from which to start search paths
     * @return the list of locations furthest on any shortest path from start
     * @throws NoSuchElementException if start does not exist, or if there are
     *         no other locations that can be reached from there
     */
    public List<String> getFurthestFromList(String start) throws NoSuchElementException {
        if (!graph.containsNode(start)) {
            throw new NoSuchElementException("Start does not exist");
        }

        List<String> result = new ArrayList<>();
        double maxDistance = -1;

        for (String location: locations) {
            if (location.equals(start)) {
                continue;
            }

            try {
                double dist = graph.shortestPathCost(start, location);

                if (dist > maxDistance) {
                    maxDistance = dist;
                    result.clear();
                    result.add(location);
                } else if (dist == maxDistance) {
                    result.add(location);
                }
            } catch (NoSuchElementException e) {
                //Expected
            }
        }

        if (result.isEmpty()) {
            throw new NoSuchElementException("There are no other locations that can be reached");
        }

        return result;
    }

    /**
     * Helper method for backend testing, lets you add locations manually
     */
    public void addLocation(String location) {
        if (!locations.contains(location)) {
            locations.add(location);
        }
    }
}
