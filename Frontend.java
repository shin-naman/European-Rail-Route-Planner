import java.util.List;
import java.util.NoSuchElementException;
 
/**
 * Frontend implementation for the European Rail shortest path webapp.
 * Generates HTML fragments for path-finding and furthest-location features.
 */
public class Frontend implements FrontendInterface {
 
    private BackendInterface backend;
 
    /**
     * Constructs a Frontend with the given backend for computation.
     * @param backend the backend used for shortest path computations
     */
    public Frontend(BackendInterface backend) {
        this.backend = backend;
    }
 
    /**
     * Returns an HTML fragment with input controls for requesting a shortest path.
     * Includes labelled text fields for start/end cities and a submit button.
     */
    @Override
    public String generateShortestPathPromptHTML() {
        return "<div class=\"form-section\">"
            + "<h2>Find Shortest Train Route</h2>"
            + "<label for=\"start\">Departure City:</label>"
            + "<input type=\"text\" id=\"start\" name=\"start\" placeholder=\"e.g. Paris\" />"
            + "<label for=\"end\">Destination City:</label>"
            + "<input type=\"text\" id=\"end\" name=\"end\" placeholder=\"e.g. Berlin\" />"
            + "<button type=\"button\" onclick=\"findShortestPath()\">Find Shortest Path</button>"
            + "</div>";
    }
 
    /**
     * Returns an HTML fragment showing the shortest path between start and end,
     * including all intermediate stops and total travel time.
     * If no path exists or an error occurs, returns an appropriate error message.
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        try {
            List<String> locations = backend.findLocationsOnShortestPath(start, end);
            List<Double> times = backend.findTimesOnShortestPath(start, end);
 
            if (locations == null || locations.isEmpty()) {
                return "<div class=\"result-section\">"
                    + "<p class=\"error\">No path found between <strong>" + start
                    + "</strong> and <strong>" + end + "</strong>.</p>"
                    + "</div>";
            }
 
            // Build the ordered list of stops
            StringBuilder stops = new StringBuilder("<ol>");
            for (String loc : locations) {
                stops.append("<li>").append(loc).append("</li>");
            }
            stops.append("</ol>");
 
            // Compute total time (sum of all segment times)
            double totalTime = 0.0;
            if (times != null) {
                for (Double t : times) {
                    totalTime += t;
                }
            }
 
            return "<div class=\"result-section\">"
                + "<p>Shortest path from <strong>" + start + "</strong> to <strong>" + end + "</strong>:</p>"
                + stops.toString()
                + "<p>Total travel time: <strong>" + totalTime + " minutes</strong></p>"
                + "</div>";
 
        } catch (NoSuchElementException e) {
            return "<div class=\"result-section\">"
                + "<p class=\"error\">Could not find path: one or both cities (<strong>"
                + start + "</strong>, <strong>" + end
                + "</strong>) were not found in the network.</p>"
                + "</div>";
        } catch (Exception e) {
            return "<div class=\"result-section\">"
                + "<p class=\"error\">An unexpected error occurred: " + e.getMessage() + "</p>"
                + "</div>";
        }
    }
 
    /**
     * Returns an HTML fragment with an input control for requesting the furthest
     * locations list from a starting city.
     */
    @Override
    public String generateFurthestLocationListFromPromptHTML() {
        return "<div class=\"form-section\">"
            + "<h2>Furthest Reachable Locations</h2>"
            + "<label for=\"from\">Starting City:</label>"
            + "<input type=\"text\" id=\"from\" name=\"from\" placeholder=\"e.g. London\" />"
            + "<button type=\"button\" onclick=\"findFurthestLocations()\">Furthest Location List</button>"
            + "</div>";
    }
 
    /**
     * Returns an HTML fragment listing the furthest locations reachable from start
     * along any shortest path, including total count.
     * If no locations are reachable or the city is not found, returns an error message.
     */
    @Override
    public String generateFurthestLocationListFromResponseHTML(String start) {
        try {
            List<String> furthest = backend.getFurthestFromList(start);
 
            if (furthest == null || furthest.isEmpty()) {
                return "<div class=\"result-section\">"
                    + "<p class=\"error\">No reachable locations found from <strong>"
                    + start + "</strong>.</p>"
                    + "</div>";
            }
 
            StringBuilder stops = new StringBuilder("<ol>");
            for (String loc : furthest) {
                stops.append("<li>").append(loc).append("</li>");
            }
            stops.append("</ol>");
 
            return "<div class=\"result-section\">"
                + "<p>Furthest locations reachable from <strong>" + start + "</strong>:</p>"
                + stops.toString()
                + "<p>Total number of locations: <strong>" + furthest.size() + "</strong></p>"
                + "</div>";
 
        } catch (NoSuchElementException e) {
            return "<div class=\"result-section\">"
                + "<p class=\"error\">Could not compute furthest locations: <strong>"
                + start + "</strong> was not found in the network, "
                + "or no other locations are reachable from there.</p>"
                + "</div>";
        } catch (Exception e) {
            return "<div class=\"result-section\">"
                + "<p class=\"error\">An unexpected error occurred: " + e.getMessage() + "</p>"
                + "</div>";
        }
    }
}
