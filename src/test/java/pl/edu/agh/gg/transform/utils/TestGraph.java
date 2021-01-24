package pl.edu.agh.gg.transform.utils;

import org.graphstream.graph.Node;
import pl.edu.agh.gg.common.Utils;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.model.InteriorNode;
import pl.edu.agh.gg.transform.P7;
import pl.edu.agh.gg.transform.Transformation;
import pl.edu.agh.gg.visualization.Visualizer;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.edu.agh.gg.common.Utils.addE;
import static pl.edu.agh.gg.common.Utils.addI;


public class TestGraph extends GraphModel {

    public static final String ROOT_ID = "i1";

    public TestGraph(String id) {
        super(id);
    }

    public static void main(String[] args) {
        TestGraph g = TestGraph.make();
        g.appendSquare("e1", -1, 1);
        g.appendSquare("e2", 1, 1);
        Transformation p = new P7();

        if (p.isApplicable(g, g.getRoot(), true)) {
            p.transform(g, g.getRoot(), true);
        }

        new Visualizer(g).visualize();
    }

    public static TestGraph make() {
        TestGraph g = new TestGraph("G");
        InteriorNode i1 = addI(g, ROOT_ID, 0, 0, 0);

        GraphNode e1 = addE(g, "e1", -1, 1, 0);
        GraphNode e12 = addE(g, "e12", 0, 1, 0);
        GraphNode e2 = addE(g, "e2", 1, 1, 0);
        GraphNode e13 = addE(g, "e13", -1, 0, 0);
        GraphNode e24 = addE(g, "e24", 1, 0, 0);
        GraphNode e3 = addE(g, "e3", -1, -1, 0);
        GraphNode e4 = addE(g, "e4", 1, -1, 0);

        g.addEdge(i1, e1);
        g.addEdge(i1, e2);
        g.addEdge(i1, e3);
        g.addEdge(i1, e4);

        g.addEdge(e1, e12);
        g.addEdge(e12, e2);

        g.addEdge(e1, e13);
        g.addEdge(e2, e24);

        g.addEdge(e13, e3);
        g.addEdge(e24, e4);

        g.addEdge(e3, e4);

        return g;
    }

    public InteriorNode getRoot() {
        return (InteriorNode) getGraphNode(ROOT_ID).get();
    }

    public TestGraph addEdge(GraphNode a, GraphNode b) {
        Utils.addEdge(this, a, b);
        return this;
    }

    public InteriorNode appendSquare(String anchorId, double shiftX, double shiftY) {
        GraphNode n = getGraphNode(anchorId).get();
        String sqId = n.getId() + "(" + shiftX + "," + shiftY + ")" + "i1";

        InteriorNode i = addI(this, sqId, n.getXCoordinate() + shiftX, n.getYCoordinate() + shiftY, n.getLevel());
        ENode e1 = addE(this, sqId + "e1", n.getXCoordinate(), n.getYCoordinate() + 2 * shiftY, n.getLevel());
        ENode e2 = addE(this, sqId + "e2", n.getXCoordinate() + 2 * shiftX, n.getYCoordinate() + 2 * shiftY, n.getLevel());
        ENode e3 = addE(this, sqId + "e3", n.getXCoordinate() + 2 * shiftX, n.getYCoordinate(), n.getLevel());

        addEdge(n, e1).addEdge(e1, e2).addEdge(e2, e3).addEdge(e3, n);
        addEdge(i, e1).addEdge(i, e2).addEdge(i, e3).addEdge(i, n);
        return i;
    }

    public Set<String> getNeighbours(String nodeId) {
        GraphNode n = getGraphNode(nodeId).get();
        return n.getEdgeSet().stream()
                .flatMap(e -> Stream.of((GraphNode) e.getNode0(), e.getNode1()))
                .map(Node::getId)
                .filter(id -> !nodeId.equals(id))
                .collect(Collectors.toSet());
    }
}
