package pl.edu.agh.gg.transform;

import org.graphstream.graph.Node;
import org.junit.Test;
import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.model.InteriorNode;
import pl.edu.agh.gg.transform.utils.TestGraph;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;


public class P7TestSuite {

    private GraphNode assertGetNeighbour(TestGraph g, GraphNode n, String id) {
        GraphNode neighbour = g.getGraphNode(id).get();
        assertTrue(n.hasEdgeBetween(neighbour));
        return neighbour;
    }

    private void assertESquare(TestGraph g, Node root, List<String> clockwiseIds, List<Coordinates> clockwiseCoords) {
        GraphNode r = (GraphNode) root;
        List<ENode> es = Arrays.stream(r.getAdjacentENodes()).map(n -> (ENode) n).collect(Collectors.toList());
        assertEquals(new HashSet<>(clockwiseIds), es.stream().map(ENode::getId).collect(Collectors.toSet()));

        ENode current = es.stream().filter(e -> clockwiseIds.get(0).equals(e.getId())).toArray(ENode[]::new)[0];
        for (int i = 0; i < clockwiseIds.size(); i++) {
            ENode next = (ENode) assertGetNeighbour(g, current, clockwiseIds.get((i + 1) % clockwiseIds.size()));
            assertEquals(clockwiseCoords.get(i), current.getCoordinates());
            assertFalse(current.hasEdgeBetween(clockwiseIds.get((i + 2) % clockwiseIds.size())));
            current = next;
        }
    }

    private Coordinates c(double x, double y, double level) {
        return Coordinates.createCoordinatesWithOffset(x, y, level);
    }

    private void assertOkRight(TestGraph g, InteriorNode r) {
        assertEquals(TestGraph.ROOT_ID, r.getId());

        InteriorNode i1i1 = (InteriorNode) assertGetNeighbour(g, r, "i1i1");
        assertESquare(g, i1i1, asList("i1e1", "i1e12", "i1e14", "i1e13"),
                asList(c(-1, 1, 1), c(0, 1, 1), c(0, 0, 1), c(-1, 0, 1)));

        InteriorNode i1i2 = (InteriorNode) assertGetNeighbour(g, r, "i1i2");
        assertESquare(g, i1i2, asList("i1e12", "i1e2", "i1e24", "i1e14"),
                asList(c(0, 1, 1), c(1, 1, 1), c(1, 0, 1), c(0, 0, 1)));

        InteriorNode i1i3 = (InteriorNode) assertGetNeighbour(g, r, "i1i3");
        assertESquare(g, i1i3, asList("i1e13", "i1e14", "i1e34", "i1e3"),
                asList(c(-1, 0, 1), c(0, 0, 1), c(0, -1, 1), c(-1, -1, 1)));

        InteriorNode i1i4 = (InteriorNode) assertGetNeighbour(g, r, "i1i4");
        assertESquare(g, i1i4, asList("i1e14", "i1e24", "i1e4", "i1e34"),
                asList(c(0, 0, 1), c(1, 0, 1), c(1, -1, 1), c(0, -1, 1)));
    }

    private void assertNodeNotChanged(TestGraph pre, TestGraph post, String nodeId) {
        assertEquals(pre.getGraphNode(nodeId).get().getCoordinates(),
                post.getGraphNode(nodeId).get().getCoordinates());

        Set<String> preNeighbours = pre.getNeighbours(nodeId);
        Set<String> postNeighbours = post.getNeighbours(nodeId);
        assertEquals(preNeighbours, postNeighbours);
    }

    private void assertRootNotChanged(TestGraph pre, TestGraph post, String changeRootId, String root, Set<String> visited) {
        Deque<String> bfsQ = new ArrayDeque<>();
        bfsQ.add(root);
        while (!bfsQ.isEmpty()) {
            String n = bfsQ.remove();
            Set<String> neighbours = pre.getNeighbours(n);
            if (visited.contains(n) || n.startsWith(changeRootId) || neighbours.contains(changeRootId)) {
                continue;
            }
            visited.add(n);
            assertNodeNotChanged(pre, post, n);
            bfsQ.addAll(neighbours);
        }
    }

    private void assertRestNotChanged(TestGraph pre, TestGraph post, String changeRootId, String... restRootIds) {
        Set<String> visited = new HashSet<>();
        Arrays.stream(restRootIds).forEach(r -> assertRootNotChanged(pre, post, changeRootId, r, visited));
    }

    @Test
    public void testOkLeftApplicable() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        assertTrue(p.isApplicable(g, g.getRoot(), false));
        assertTrue(p.isApplicable(g, g.getRoot(), true));
    }

    @Test
    public void testOkLeftCorrect() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        if (p.isApplicable(g, g.getRoot(), true)) {
            p.transform(g, g.getRoot(), true);
        }

        assertOkRight(g, g.getRoot());
    }

    @Test
    public void testEmbeddedLeftApplicable() {
        TestGraph g = TestGraph.make();
        g.appendSquare("e1", -1, 1);
        g.appendSquare("e2", 0.5, 0.5);
        Transformation p = new P7();

        assertTrue(p.isApplicable(g, g.getRoot(), false));
        assertTrue(p.isApplicable(g, g.getRoot(), true));
    }

    @Test
    public void testEmbeddedLeftCorrect() {
        TestGraph pre = TestGraph.make();
        pre.appendSquare("e1", -1, 1);
        pre.appendSquare("e2", 0.5, 0.5);
        TestGraph post = TestGraph.make();
        InteriorNode i1 = post.appendSquare("e1", -1, 1);
        InteriorNode i2 = post.appendSquare("e2", 0.5, 0.5);
        Transformation p = new P7();

        if (p.isApplicable(post, post.getRoot(), true)) {
            p.transform(post, post.getRoot(), true);
        }

        assertOkRight(post, post.getRoot());
        assertRestNotChanged(pre, post, pre.getRoot().getId(), i1.getId(), i2.getId());
    }

    @Test
    public void testNoEdgeNotApplicable() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        g.deleteGraphEdge("e12e2");

        assertFalse(p.isApplicable(g, g.getRoot(), true));
    }

    @Test
    public void testNoVertexNotApplicable() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        g.removeGraphNode("e12");

        assertFalse(p.isApplicable(g, g.getRoot(), true));
    }

    @Test
    public void testBadIdNotApplicable() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        g.getRoot().symbolToLowerCase();

        assertFalse(p.isApplicable(g, g.getRoot(), true));
    }

    @Test
    public void testBadCoordNotApplicable() {
        TestGraph g = TestGraph.make();
        Transformation p = new P7();

        g.getGraphNode("e12").get().setCoordinates(c(0, 1.1, 0));

        assertFalse(p.isApplicable(g, g.getRoot(), true));
    }
}
