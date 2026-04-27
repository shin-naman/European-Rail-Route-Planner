import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class extends the DijkstraGraph class to run submission checks on it.
 */
public class P210SubmissionChecker extends DijkstraGraph<Integer, Integer> {

    /**
     * Creates a graph with 4 nodes and 7 edges and checks the path that
     * returned between two of the nodes based on the shortestPathData
     * and shortestPathCost methods.
     */
    @Test
    public void submissionCheckerSmallGraph() {
        DijkstraGraph<Integer, Integer> graph = new DijkstraGraph<>();
        graph.insertNode(1);
        graph.insertNode(6);
        graph.insertNode(11);
        graph.insertNode(3);

        graph.insertEdge(11, 1, 3);
        graph.insertEdge(6, 11, 7);
        graph.insertEdge(1, 11, 5);
        graph.insertEdge(6, 6, 1);
        graph.insertEdge(1, 6, 4);
        graph.insertEdge(6, 1, 6);
        graph.insertEdge(6, 3, 9);

        Assertions.assertEquals(7, graph.shortestPathCost(6, 11));
        Assertions.assertEquals("[6, 11]", graph.shortestPathData(6, 11).toString());
    }

    /**
     * Check that a NoSuchElementException is thrown if a path from start to end
     * node does not exist.
     */
    @Test
    public void submissionCheckerNoPath() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 2);
        graph.insertEdge("B", "C", 3);

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("B", "A"));
    }

    /**
     * Check that a NoSuchElementException is thrown if start node does not exist
     * in graph.
     */
    @Test
    public void submissionCheckerNoNode() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 2);
        graph.insertEdge("B", "C", 3);

        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("X", "A"));
    }

    /**
     * Tests the lecture example graph and confirms that the shortest
     * path from A to F is A -> B -> D -> F with total cost 5.
     */
    @Test
    public void testLectureExampleAtoF() {
        DijkstraGraph<String, Integer> graph = createLectureGraph();

        //Shortest path should be ABDF with cost 5
        Assertions.assertEquals(5.0, graph.shortestPathCost("A", "F"));
        Assertions.assertEquals("[A, B, D, F]", graph.shortestPathData("A", "F").toString());
    }

    /**
     * Tests the lecture example graph and confirms that the shortest
     * path from A to E is A -> B -> D -> E with total cost 8.
     */
    @Test
    public void testLectureExampleAtoE() {
        DijkstraGraph<String, Integer> graph = createLectureGraph();

        //Shortest path should be ABDE with cost 8
        Assertions.assertEquals(8.0, graph.shortestPathCost("A", "E"));
        Assertions.assertEquals("[A, B, D, E]", graph.shortestPathData("A", "E").toString());
    }


    /**
     * Tests that a NoSuchElementException is thrown when no directed
     * path exists between the selected start and end nodes.
     */
    @Test
    public void testLectureExampleAtoG() {
        DijkstraGraph<String, Integer> graph = createLectureGraph();

        // No path from A to G (nothing leads to G)
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("A", "G");
        });
    }

    /**
     * Tests that a NoSuchElementException is thrown when either the
     * start node or end node does not exist in the graph.
     */
    @Test
    public void testLectureExampleMissingNodes() {
        DijkstraGraph<String, Integer> graph = createLectureGraph();

        // Should throw exception when beginning node doesn't exist
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("X", "A");
        });

        // Should throw exception when end node doesn't exist
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("A", "Z");
        });
    }


    /**
     * Private helper method to reconstruct the graph from lecture
     * @return the example graph from lecture
     */
    private DijkstraGraph<String, Integer> createLectureGraph() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("A", "E", 15);
        graph.insertEdge("B", "E", 10);
        graph.insertEdge("B", "D", 1);
        graph.insertEdge("C", "D", 5);
        graph.insertEdge("D", "E", 3);
        graph.insertEdge("D", "F", 0);
        graph.insertEdge("F", "D", 2);
        graph.insertEdge("F", "H", 4);
        graph.insertEdge("G", "H", 4);

        return graph;
    }

}
