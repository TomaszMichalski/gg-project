package pl.edu.agh.gg.model;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractGraph;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ENode extends GraphNode {
    public static final Character INIT_SYMBOL = 'E';

    public ENode(AbstractGraph graph, String id, Coordinates coordinates) {
        super(graph, id, INIT_SYMBOL, coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ENode eNode = (ENode) obj;

        return this.getCoordinates().equals(eNode.getCoordinates());
    }

    public void replaceWith(GraphModel graph, ENode replacement, List<ENode> otherENodesToReplace) {
        this.getCoordinates().setOriginalX(0.0);
        this.getCoordinates().setOriginalY(0.0);

        for (GraphNode graphNode : this.getAdjacentENodes()) {
            if (!otherENodesToReplace.contains(graphNode)) {
                replacement.addNeighbourENode(graphNode);
            }
        }

        List<Pair<ENode, GraphNode>> nodesForEdges = new ArrayList<>();
        Iterator<Node> neighborNodeIterator = this.getNeighborNodeIterator();

        while (neighborNodeIterator.hasNext()) {
            GraphNode graphNode = (GraphNode) neighborNodeIterator.next();
            if (!otherENodesToReplace.contains(graphNode)) {
                nodesForEdges.add(Pair.with(replacement, graphNode));
            }
        }
        graph.removeGraphNode(this.getId());

        for (Pair<ENode, GraphNode> pair : nodesForEdges) {
            graph.insertGraphEdge(getEdgeName(pair.getValue0(), pair.getValue1()), pair.getValue0(), pair.getValue1());
        }
    }
}
