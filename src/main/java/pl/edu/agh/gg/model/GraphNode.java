package pl.edu.agh.gg.model;

import org.javatuples.Pair;
import pl.edu.agh.gg.common.ElementAttributes;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.SingleNode;

import java.util.*;

public abstract class GraphNode extends SingleNode {
    private Character symbol;
    private final Coordinates coordinates;

    private final List<GraphNode> adjacentENodes = new ArrayList<>();

    protected GraphNode(AbstractGraph graph, String id, Character symbol, double xCoordinate, double yCoordinate, double level) {
        this(graph, id, symbol, new Coordinates(xCoordinate, yCoordinate, level));
    }

    protected GraphNode(AbstractGraph graph, String id, Character symbol, Coordinates coordinates) {
        super(graph, id);
        super.setAttribute(ElementAttributes.FROZEN_LAYOUT);
        this.symbol = symbol;
        this.coordinates = coordinates;
    }

    public void addNeighbourENode(GraphNode node) {
        adjacentENodes.add(node);
    }

    public boolean removeNeighbourENode(GraphNode node) {
        return adjacentENodes.remove(node);
    }

    public GraphNode[] getAdjacentENodes() {
        return adjacentENodes.toArray(new GraphNode[0]);
    }

    public Character getSymbol() {
        return symbol;
    }

    public boolean isSymbolUpperCase() {
        return Character.isUpperCase(symbol);
    }

    public void symbolToLowerCase() {
        symbol = Character.toLowerCase(symbol);
    }

    public double getXCoordinate() {
        return coordinates.getX();
    }

    public double getYCoordinate() {
        return coordinates.getY();
    }

    public double getLevel() {
        return coordinates.getLevel();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(Node node) {
        for (AbstractNode e : this.neighborMap.keySet()) {
            if (Objects.equals(e.getId(), node.getId())) {
                return super.getEdgeBetween(e);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("mockGraph.add(new Pair<>('%s', %s));", symbol, coordinates.toString());
    }
}
