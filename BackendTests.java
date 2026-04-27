import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendTests {

    /**
     * Test loading graph data and retrieving all locations.
     * Verifies that locations are added and returned correctly.
     */
    @Test
    public void roleTest1() throws IOException {
        Backend backend = new Backend(new Graph_Placeholder());

        backend.loadGraphData("europeanRail.dot");

        List<String> locations = backend.getListOfAll();

        assertNotNull(locations);
        assertTrue(locations.size() > 0);
    }

    /**
     * Test shortest path locations and times.
     * Verifies that path and edge weights are returned.
     */
    @Test
    public void roleTest2() {
        Backend backend = new Backend(new Graph_Placeholder());

        // using placeholder graph
        List<String> path = backend.findLocationsOnShortestPath(
                "Union South",
                "Weeks Hall for Geological Sciences"
        );

        List<Double> times = backend.findTimesOnShortestPath(
                "Union South",
                "Weeks Hall for Geological Sciences"
        );

        assertEquals("Union South", path.get(0));
        assertEquals("Weeks Hall for Geological Sciences", path.get(2));
    }

    /**
     * Test getFurthestFromList behavior.
     * Verifies correct furthest node and exception handling.
     */
    @Test
    public void roleTest3() {
        Backend backend = new Backend(new Graph_Placeholder());

        //Manually adding the locations to ArrayList<> locations in Backend b/c we're
        //not loading data using loadGraphData(), meaning ArrayList<> locations doesn't get
        //updated with Graph_Placeholder
        backend.addLocation("Union South");
        backend.addLocation("Computer Sciences and Statistics");
        backend.addLocation("Weeks Hall for Geological Sciences");

        // valid case
        List<String> furthest = backend.getFurthestFromList("Union South");
        assertNotNull(furthest);
        assertTrue(furthest.size() > 0);

        // invalid case (should throw exception)
        assertThrows(NoSuchElementException.class, () -> {
            backend.getFurthestFromList("Invalid Location");
        });
    }

    /**
     * Integration test that verifies the Frontend can generate a shortest path
     * response using the real Backend, DijkstraGraph, and HashtableMap.
     */
    @Test
    public void shortestPathResponseIntegrationTest() throws IOException {
        // Create the real graph and backend
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        backend.loadGraphData("europeanRail.dot");

        // Connect partner's frontend to my backend
        Frontend frontend = new Frontend(backend);

        // Use two real locations from the loaded dataset
        List<String> locations = backend.getListOfAll();
        String start = locations.get(0);
        String end = locations.get(1);

        // Generate frontend HTML response
        String html = frontend.generateShortestPathResponseHTML(start, end);

        // Verify response includes meaningful frontend/backend output
        assertNotNull(html);
        assertTrue(html.contains(start));
        assertTrue(html.contains(end));
        assertTrue(html.contains("Total travel time") || html.contains("No path found"));
    }

    /**
     * Integration test that verifies the Frontend shortest path prompt is
     * generated correctly while connected to the real Backend implementation.
     */
    @Test
    public void shortestPathPromptIntegrationTest() throws IOException {
        // Create the integrated backend/frontend objects
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        backend.loadGraphData("europeanRail.dot");
        Frontend frontend = new Frontend(backend);

        // Generate the shortest path prompt HTML
        String html = frontend.generateShortestPathPromptHTML();

        // Verify the form contains the expected input fields
        assertNotNull(html);
        assertTrue(html.contains("id=\"start\""));
        assertTrue(html.contains("id=\"end\""));
        assertTrue(html.contains("Find Shortest Path"));
    }

    /**
     * Integration test that verifies the Frontend can generate a furthest
     * location response using the real Backend and graph implementation.
     */
    @Test
    public void furthestLocationResponseIntegrationTest() throws IOException {
        // Create the real graph and backend
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        backend.loadGraphData("europeanRail.dot");

        // Connect partner's frontend to my backend
        Frontend frontend = new Frontend(backend);

        // Use a real starting location from the loaded dataset
        String start = backend.getListOfAll().get(0);

        // Generate frontend HTML response
        String html = frontend.generateFurthestLocationListFromResponseHTML(start);

        // Verify the response is meaningful
        assertNotNull(html);
        assertTrue(html.contains(start));
        assertTrue(html.contains("Furthest") || html.contains("error"));
    }

    /**
     * Integration test that verifies invalid input is handled through the
     * integrated Frontend and Backend without crashing the test.
     */
    @Test
    public void invalidLocationIntegrationTest() throws IOException {
        // Create the integrated backend/frontend objects
        GraphADT<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        backend.loadGraphData("europeanRail.dot");
        Frontend frontend = new Frontend(backend);

        // Ask for a path using invalid locations
        String html = frontend.generateShortestPathResponseHTML(
                "Not A Real City",
                "Also Not A Real City"
        );

        // Verify an error/no-path response is generated instead of crashing
        assertNotNull(html);
        assertTrue(html.contains("No path found") || html.contains("error") || html.contains("Could not"));
    }
}
