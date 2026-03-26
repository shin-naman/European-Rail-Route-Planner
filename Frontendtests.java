import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
 
/**
 * JUnit tests for the Frontend class.
 * Each test uses Backend_Placeholder and Graph_Placeholder to exercise
 * Frontend's HTML-generating methods without requiring a fully functional backend.
 */
public class FrontendTests {
 
    /**
     * roleTest1: Checks that generateShortestPathPromptHTML() and
     * generateFurthestLocationListFromPromptHTML() return HTML strings containing
     * the required input element IDs, button labels, and basic HTML structure.
     */
    @Test
    public void roleTest1() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
 
        String pathPrompt = frontend.generateShortestPathPromptHTML();
        assertNotNull(pathPrompt, "Shortest path prompt should not be null");
        assertTrue(pathPrompt.contains("id=\"start\""),
            "Prompt should contain input with id=\"start\"");
        assertTrue(pathPrompt.contains("id=\"end\""),
            "Prompt should contain input with id=\"end\"");
        assertTrue(pathPrompt.contains("Find Shortest Path"),
            "Prompt should contain a 'Find Shortest Path' button");
 
        String furthestPrompt = frontend.generateFurthestLocationListFromPromptHTML();
        assertNotNull(furthestPrompt, "Furthest location prompt should not be null");
        assertTrue(furthestPrompt.contains("id=\"from\""),
            "Prompt should contain input with id=\"from\"");
        assertTrue(furthestPrompt.contains("Furthest Location List"),
            "Prompt should contain a 'Furthest Location List' button");
    }
 
    /**
     * roleTest2: Checks that generateShortestPathResponseHTML() returns correct HTML
     * when a valid path exists between two known placeholder locations.
     */
    @Test
    public void roleTest2() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
 
        String start = "Union South";
        String end = "Weeks Hall for Geological Sciences";
        String response = frontend.generateShortestPathResponseHTML(start, end);
 
        assertNotNull(response, "Shortest path response should not be null");
        assertTrue(response.contains(start),
            "Response should mention the start city");
        assertTrue(response.contains(end),
            "Response should mention the end city");
        assertTrue(response.contains("<ol>"),
            "Response should contain an ordered list of stops");
        assertTrue(response.contains("minutes"),
            "Response should include total travel time in minutes");
        // Verify all stops are listed
        assertTrue(response.contains("Union South"),
            "Response should list Union South");
        assertTrue(response.contains("Computer Sciences and Statistics"),
            "Response should list intermediate stop");
        assertTrue(response.contains("Weeks Hall for Geological Sciences"),
            "Response should list final destination");
    }
 
    /**
     * roleTest3: Checks that generateFurthestLocationListFromResponseHTML() returns
     * correct HTML for a known starting location, and that the HTML error-handling
     * path is exercised when a NoSuchElementException would be thrown.
     */
    @Test
    public void roleTest3() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
 
        // Part A: valid start location
        String validStart = "Union South";
        String validResponse = frontend.generateFurthestLocationListFromResponseHTML(validStart);
        assertNotNull(validResponse, "Furthest list response should not be null");
        assertTrue(validResponse.contains("<ol>"),
            "Response should contain an ordered list of furthest locations");
        assertTrue(validResponse.contains("Total number of locations"),
            "Response should include the total count of locations");
        assertTrue(validResponse.contains(validStart),
            "Response should reference the starting city");
 
        // Part B: city not in graph should trigger error handling
        // Graph_Placeholder.shortestPathData returns an empty list for unknown cities,
        // so getFurthestFromList will return an empty path — frontend should show error.
        String unknownStart = "Atlantis";
        String errorResponse = frontend.generateFurthestLocationListFromResponseHTML(unknownStart);
        assertNotNull(errorResponse, "Error response should not be null");
        // The response should either show an error message or indicate no locations found
        assertTrue(
            errorResponse.contains("error") || errorResponse.contains("not found")
            || errorResponse.contains("No reachable") || errorResponse.contains("Could not"),
            "Response for an unknown city should contain an error or 'no results' message"
        );
    }
}
 
