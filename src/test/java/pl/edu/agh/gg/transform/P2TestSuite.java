package pl.edu.agh.gg.transform;

import static org.junit.Assert.*;
import static pl.edu.agh.gg.examples.Main.generateGraphModel;

import org.javatuples.Pair;
import org.junit.Test;

import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.transform.utils.MockGraphs;

import java.util.List;

public class P2TestSuite {

    @Test
    public void isApplicableP2TransformationTest() {

        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        assertTrue(p2.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void transformP2TransformationTest() {

        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p2.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);

        List<Pair<Character, Coordinates>> mockGraphP2 = MockGraphs.generateMockP2();

        for (Pair<Character, Coordinates> pair : mockGraphP2) {

            long count = graphModel.getGraphNodes().stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1())).count();

            assertEquals(pair.toString(), 1L, count);
        }

    }

    @Test
    public void transformP2HorizontalTransformationTest() {

        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p2.transform(graphModel, graphModel.getGraphNode("e1i1").get(), true);

        List<Pair<Character, Coordinates>> mockGraphP2 = MockGraphs.generateMockP2Horizontal();

        for (Pair<Character, Coordinates> pair : mockGraphP2) {

            long count = graphModel.getGraphNodes().stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1())).count();

            assertEquals(pair.toString(), 1L, count);
        }
    }

    @Test
    public void missingEdgeP2TransformationTest() {

        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        graphModel.deleteGraphEdge("e1e3e1e4");

        assertFalse(p2.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

    @Test
    public void missingNodeP2TransformationTest() {

        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p2 = new P2();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);

        graphModel.removeGraphNode("e1e2");

        assertFalse(p2.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false));
    }

}
