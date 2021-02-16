package pl.edu.agh.gg.transform;

import org.javatuples.Pair;
import org.junit.Test;
import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.transform.utils.MockGraphs;

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
}
