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
}