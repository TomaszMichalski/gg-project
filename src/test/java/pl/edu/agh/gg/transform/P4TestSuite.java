package pl.edu.agh.gg.transform;

import org.javatuples.Pair;
import org.junit.Test;
import pl.edu.agh.gg.examples.Main;
import pl.edu.agh.gg.examples.MainP4;
import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.transform.utils.MockGraphs;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class P4TestSuite {

    @Test
    public void isNotApplicableBaseGraphTest() {
        GraphModel graphModel = Main.generateGraphModel();
        Transformation p4 = new P4();

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableP1GraphTest() {
        GraphModel graphModel = Main.generateGraphModel();
        Transformation p1 = new P1();
        Transformation p4 = new P4();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void isNotApplicableP2GraphTest() {
        GraphModel graphModel = Main.generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p2.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);

        assertFalse(p2.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void isApplicableP4GraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        assertTrue(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableMissingEdgeGraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        graphModel.deleteGraphEdge("edge4");
        GraphNode node1 = graphModel.getGraphNode("e1").get();
        GraphNode node2 = graphModel.getGraphNode("e1e5").get();
        node1.removeNeighbourENode(node2);
        node2.removeNeighbourENode(node1);

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableMissingNodeGraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        GraphNode nodeToRemove = graphModel.removeGraphNode("e1e1").get();
        for(GraphNode node : graphModel.getGraphNodes()){
            node.removeNeighbourENode(nodeToRemove);
        }

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableWrongCoordinatesEdgeGraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel(new Coordinates(0.0, 2.5, 1));
        Transformation p4 = new P4();

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableWrongLevelEdgeGraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel(new Coordinates(0.0, 2.0, 2));
        Transformation p4 = new P4();

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isNotApplicableWrongLabelEdgeGraphTest() {
        GraphModel graphModel = MainP4.generateGraphModel(new Coordinates(0.0, 2.0, 2));
        graphModel.getGraphNode("e1").get().symbolToLowerCase();

        Transformation p4 = new P4();

        assertFalse(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test(expected = NoSuchElementException.class)
    public void transformP2TransformationTest() {
        GraphModel graphModel = Main.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
    }

    @Test
    public void transformP4TransformationCoordinatesTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        List<Pair<Character, Coordinates>> mockGraphP4 = MockGraphs.generateMockP4();

        for (Pair<Character, Coordinates> pair : mockGraphP4) {

            long count = graphModel.getGraphNodes().stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1())).count();

            assertEquals(pair.toString(), 1L, count);
        }
    }

    @Test
    public void transformP4TransformationLeftEdgesTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();
        GraphNode e1 = graphModel.getGraphNode("e1e1").get();
        GraphNode e2 = graphModel.getGraphNode("e1e2").get();
        GraphNode e3 = graphModel.getGraphNode("e1e3").get();
        GraphNode e4 = graphModel.getGraphNode("e1e4").get();
        GraphNode e5 = graphModel.getGraphNode("e1e5").get();

        assertTrue(getListFromIterator(interiorNode.getNeighborNodeIterator()).containsAll(Arrays.asList(e1, e3, e4, e5)));
        assertTrue(getListFromIterator(e1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e2, e5)));
        assertTrue(getListFromIterator(e2.getNeighborNodeIterator()).containsAll(Arrays.asList(e1, e3)));
        assertTrue(getListFromIterator(e3.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e2, e4)));
        assertTrue(getListFromIterator(e4.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e3, e5)));
        assertTrue(getListFromIterator(e5.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e4, e1)));
    }

    @Test
    public void transformP4TransformationRightEdgesTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();

        GraphNode interiorNode1 = graphModel.getGraphNode("e1i1").get();
        GraphNode interiorNode2 = graphModel.getGraphNode("e1i2").get();

        GraphNode n1 = graphModel.getGraphNode("e1n1").get();
        GraphNode n2 = graphModel.getGraphNode("e1n2").get();
        GraphNode n3 = graphModel.getGraphNode("e1n3").get();
        GraphNode n4 = graphModel.getGraphNode("e1n4").get();
        GraphNode n5 = graphModel.getGraphNode("e1n5").get();
        GraphNode n6 = graphModel.getGraphNode("e1n6").get();

        assertTrue(getListFromIterator(interiorNode.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2)));
        assertTrue(getListFromIterator(interiorNode1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, n1, n2, n5, n6)));
        assertTrue(getListFromIterator(interiorNode2.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, n2, n3, n4, n5)));
        assertTrue(getListFromIterator(n1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, n2, n6)));
        assertTrue(getListFromIterator(n2.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2, n1, n3)));
        assertTrue(getListFromIterator(n3.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode2, n2, n4)));
        assertTrue(getListFromIterator(n4.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode2, n3, n5)));
        assertTrue(getListFromIterator(n5.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2, n4, n6)));
        assertTrue(getListFromIterator(n6.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, n5, n1)));
    }

    @Test
    public void transformP4TransformationLevelTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();
        GraphNode interiorNode1 = graphModel.getGraphNode("e1i1").get();
        GraphNode n1 = graphModel.getGraphNode("e1n1").get();

        assertEquals(interiorNode.getLevel(), 1.);
        assertEquals(interiorNode1.getLevel(), 2.);
        assertEquals(n1.getLevel(), 2.);
    }

    @Test
    public void isApplicableP4ExtendedGraphTest() {
        GraphModel graphModel = MainP4.generateExtendedGraphModel();
        Transformation p4 = new P4();

        assertTrue(p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void transformP4ExtendedTransformationRightExtendedEdgesTest() {
        GraphModel graphModel = MainP4.generateExtendedGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();
        GraphNode e1 = graphModel.getGraphNode("e1e1").get();
        GraphNode e2 = graphModel.getGraphNode("e1e2").get();
        GraphNode e3 = graphModel.getGraphNode("e1e3").get();
        GraphNode e4 = graphModel.getGraphNode("e1e4").get();
        GraphNode e5 = graphModel.getGraphNode("e1e5").get();

        GraphNode a = graphModel.getGraphNode("a").get();
        GraphNode a1 = graphModel.getGraphNode("a1").get();
        GraphNode a2 = graphModel.getGraphNode("a2").get();
        GraphNode a3 = graphModel.getGraphNode("a3").get();
        GraphNode a4 = graphModel.getGraphNode("a4").get();
        GraphNode a5 = graphModel.getGraphNode("a5").get();

        assertTrue(getListFromIterator(a.getNeighborNodeIterator()).containsAll(Collections.singletonList(interiorNode)));
        assertTrue(getListFromIterator(a1.getNeighborNodeIterator()).containsAll(Arrays.asList(a2, a5)));
        assertTrue(getListFromIterator(a2.getNeighborNodeIterator()).containsAll(Arrays.asList(a1, a3)));
        assertTrue(getListFromIterator(a3.getNeighborNodeIterator()).containsAll(Arrays.asList(a2, a4)));
        assertTrue(getListFromIterator(a4.getNeighborNodeIterator()).containsAll(Arrays.asList(a3, a5)));
        assertTrue(getListFromIterator(a5.getNeighborNodeIterator()).containsAll(Arrays.asList(a4, a1)));
    }

    @Test
    public void transformP4ExtendedTransformationLeftEdgesTest() {
        GraphModel graphModel = MainP4.generateExtendedGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();
        GraphNode e1 = graphModel.getGraphNode("e1e1").get();
        GraphNode e2 = graphModel.getGraphNode("e1e2").get();
        GraphNode e3 = graphModel.getGraphNode("e1e3").get();
        GraphNode e4 = graphModel.getGraphNode("e1e4").get();
        GraphNode e5 = graphModel.getGraphNode("e1e5").get();

        assertTrue(getListFromIterator(interiorNode.getNeighborNodeIterator()).containsAll(Arrays.asList(e1, e3, e4, e5)));
        assertTrue(getListFromIterator(e1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e2, e5)));
        assertTrue(getListFromIterator(e2.getNeighborNodeIterator()).containsAll(Arrays.asList(e1, e3)));
        assertTrue(getListFromIterator(e3.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e2, e4)));
        assertTrue(getListFromIterator(e4.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e3, e5)));
        assertTrue(getListFromIterator(e5.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, e4, e1)));
    }

    @Test
    public void transformP4ExtendedTransformationRightEdgesTest() {
        GraphModel graphModel = MainP4.generateExtendedGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode interiorNode = graphModel.getGraphNode("e1").get();

        GraphNode interiorNode1 = graphModel.getGraphNode("e1i1").get();
        GraphNode interiorNode2 = graphModel.getGraphNode("e1i2").get();

        GraphNode n1 = graphModel.getGraphNode("e1n1").get();
        GraphNode n2 = graphModel.getGraphNode("e1n2").get();
        GraphNode n3 = graphModel.getGraphNode("e1n3").get();
        GraphNode n4 = graphModel.getGraphNode("e1n4").get();
        GraphNode n5 = graphModel.getGraphNode("e1n5").get();
        GraphNode n6 = graphModel.getGraphNode("e1n6").get();

        assertTrue(getListFromIterator(interiorNode.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2)));
        assertTrue(getListFromIterator(interiorNode1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, n1, n2, n5, n6)));
        assertTrue(getListFromIterator(interiorNode2.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode, n2, n3, n4, n5)));
        assertTrue(getListFromIterator(n1.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, n2, n6)));
        assertTrue(getListFromIterator(n2.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2, n1, n3)));
        assertTrue(getListFromIterator(n3.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode2, n2, n4)));
        assertTrue(getListFromIterator(n4.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode2, n3, n5)));
        assertTrue(getListFromIterator(n5.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, interiorNode2, n4, n6)));
        assertTrue(getListFromIterator(n6.getNeighborNodeIterator()).containsAll(Arrays.asList(interiorNode1, n5, n1)));
    }

    @Test
    public void transformP4TransformationExtendedLevelTest() {
        GraphModel graphModel = MainP4.generateExtendedGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        GraphNode a = graphModel.getGraphNode("a").get();
        GraphNode interiorNode = graphModel.getGraphNode("e1").get();
        GraphNode interiorNode1 = graphModel.getGraphNode("e1i1").get();
        GraphNode n1 = graphModel.getGraphNode("e1n1").get();

        assertEquals(a.getLevel(), 2.);
        assertEquals(interiorNode.getLevel(), 3.);
        assertEquals(interiorNode1.getLevel(), 4.);
        assertEquals(n1.getLevel(), 4.);
    }

    private <T> List<T> getListFromIterator(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
