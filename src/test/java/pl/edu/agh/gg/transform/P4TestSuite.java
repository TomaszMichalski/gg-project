package pl.edu.agh.gg.transform;

import org.javatuples.Pair;
import org.junit.Test;
import pl.edu.agh.gg.examples.Main;
import pl.edu.agh.gg.examples.MainP4;
import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.transform.utils.MockGraphs;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Test(expected = NoSuchElementException.class)
    public void transformP2TransformationTest() {
        GraphModel graphModel = Main.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
    }

    @Test
    public void transformP4TransformationTest() {
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
    public void transformP4HorizontalTransformationTest() {
        GraphModel graphModel = MainP4.generateGraphModel();
        Transformation p4 = new P4();

        p4.transform(graphModel, graphModel.getGraphNode("e1").get(), true);

        graphModel.getGraphNodes().forEach(System.out::println);

        List<Pair<Character, Coordinates>> mockGraphP4 = MockGraphs.generateMockP4Horizontal();

        for (Pair<Character, Coordinates> pair : mockGraphP4) {

            long count = graphModel.getGraphNodes().stream()
                    .filter(n -> n.getSymbol() == pair.getValue0() && n.getCoordinates().equals(pair.getValue1())).count();

            assertEquals(pair.toString(), 1L, count);
        }
    }
}
