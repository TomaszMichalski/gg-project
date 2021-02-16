package pl.edu.agh.gg.transform;

import org.javatuples.Pair;
import org.junit.Test;
import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.transform.utils.MockGraphs;
import pl.edu.agh.gg.visualization.Visualizer;

import java.util.List;

import static org.junit.Assert.*;
import static pl.edu.agh.gg.examples.Main.generateGraphModel;

public class P3TestSuite {

    @Test
    public void isApplicableForCorrectGraphP3Test() {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        assertTrue(p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void isApplicableForWrongGraphP3Test() {
        GraphModel graphModel = generateGraphModel();
        Transformation p3 = new P3();

        assertFalse(p3.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false));
    }

    @Test
    public void isApplicableForGraphWithMissingVertexP3Test() throws InterruptedException {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        GraphNode nodeToRemove = graphModel.getGraphNode("e1e1").get();
        for(GraphNode node : graphModel.getGraphNodes()){
            node.removeNeighbourENode(nodeToRemove);
        }
//        show(graphModel);

        assertFalse(p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void isApplicableForGraphWithMissingEdgeP3Test() throws InterruptedException {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        graphModel.deleteGraphEdge("e1e1e1e2");
        GraphNode node1 = graphModel.getGraphNode("e1e1").get();
        GraphNode node2 = graphModel.getGraphNode("e1e2").get();
        node1.removeNeighbourENode(node2);
        node2.removeNeighbourENode(node1);
//        show(graphModel);

        assertFalse(p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void areNodeCoordinatesMappedCorrectly() throws InterruptedException {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);

        GraphNode oldLeftUpper = graphModel.getGraphNode("e1e1").get();
        GraphNode oldLeftLower = graphModel.getGraphNode("e1e2").get();
        GraphNode oldRightUpper = graphModel.getGraphNode("e1e4").get();
        GraphNode oldRightLower = graphModel.getGraphNode("e1e3").get();

        GraphNode newLeftUpper = graphModel.getGraphNode("e1i1n1").get();
        GraphNode newLeftLower = graphModel.getGraphNode("e1i1n3").get();
        GraphNode newRightUpper = graphModel.getGraphNode("e1i1n7").get();
        GraphNode newRightLower = graphModel.getGraphNode("e1i1n9").get();
//        show(graphModel);

        assertEquals(
                newLeftUpper.getCoordinates(),
                Coordinates.createCoordinatesWithOffset(
                        oldLeftUpper.getCoordinates().getX(),
                        oldLeftUpper.getCoordinates().getY(),
                        3)
        );
        assertEquals(
                newLeftLower.getCoordinates(),
                Coordinates.createCoordinatesWithOffset(
                        oldLeftLower.getCoordinates().getX(),
                        oldLeftLower.getCoordinates().getY(),
                        3)
        );
        assertEquals(
                newRightUpper.getCoordinates(),
                Coordinates.createCoordinatesWithOffset(
                        oldRightUpper.getCoordinates().getX(),
                        oldRightUpper.getCoordinates().getY(),
                        3)
        );
        assertEquals(
                newRightLower.getCoordinates(),
                Coordinates.createCoordinatesWithOffset(
                        oldRightLower.getCoordinates().getX(),
                        oldRightLower.getCoordinates().getY(),
                        3)
        );
    }

    @Test
    public void transformP3TransformationTest() {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);

        List<Pair<Character, Coordinates>> mockGraph = MockGraphs.generateMockP3();

        for (Pair<Character, Coordinates> pair : mockGraph) {
            long count = graphModel.getGraphNodes()
                    .stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1()))
                    .count();

            assertEquals(pair.toString(), 1L, count);
        }
    }

    @Test
    public void transformP3HorizontalTransformationTest() {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), true);

        List<Pair<Character, Coordinates>> mockGraph = MockGraphs.generateMockP3Horizontal();

        for (Pair<Character, Coordinates> pair : mockGraph) {

            long count = graphModel.getGraphNodes().stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1())).count();

            assertEquals(pair.toString(), 1L, count);
        }
    }

    private void show(GraphModel graphModel) throws InterruptedException {
        Visualizer visualizer = new Visualizer(graphModel);
        visualizer.visualize();
        Thread.sleep(100000);
    }
}
