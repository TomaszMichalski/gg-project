package pl.edu.agh.gg.model;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.javatuples.Pair;
import pl.edu.agh.gg.common.ElementAttributes;

import java.util.*;

public class GraphModel extends MultiGraph {

    private static final double conflictMoveX = -0.1;
    private static final double conflictMoveY = -0.1;

    private Map<String, GraphNode> nodes = new HashMap<>();
    private Map<String, GraphEdge> edges = new HashMap<>();

    public GraphModel(String id) {
        super(id);
    }

    public GraphNode insertGraphNode(GraphNode graphNode) {
        if (isNodeHereAlready(graphNode)) {
            Coordinates coords = graphNode.getCoordinates();
            coords.setOriginalX(coords.getX());
            coords.setOriginalY(coords.getY());
            coords.setX(coords.getX() + conflictMoveX);
            coords.setY(coords.getY() + conflictMoveY);
        }
        Node node = this.addNode(graphNode.getId());
        node.setAttribute(ElementAttributes.FROZEN_LAYOUT);
        node.setAttribute(ElementAttributes.XY, graphNode.getXCoordinate(), graphNode.getYCoordinate());
        node.setAttribute(ElementAttributes.LEVEL, graphNode.getLevel());
        node.setAttribute(ElementAttributes.LABEL, graphNode.getId());
        nodes.put(graphNode.getId(), graphNode);
        return graphNode;
    }

    public Optional<GraphNode> removeGraphNode(String id) {
        GraphNode removedNode = nodes.remove(id);
        if (removedNode != null) {
            this.setStrict(false);
            List<String> edgeIdsToRemove = new ArrayList<>();
            edges.values().stream()
                    .filter(graphEdge -> graphEdge.getEdgeNodes().contains(removedNode))
                    .map(GraphEdge::getId)
                    .filter(edgeId -> edgeId.contains(removedNode.getId()))
                    .forEach(edgeId -> {
                        edgeIdsToRemove.add(edgeId);
                        removeEdge(edgeId);
                    });
            edgeIdsToRemove.forEach(edgeId -> edges.remove(edgeId));
            this.setStrict(true);
            return Optional.of(removedNode);
        }
        return Optional.empty();
    }

    public void deleteGraphEdge(String id) {
        edges.remove(id);
        this.removeEdge(id);
    }

    public GraphEdge insertGraphEdge(String id, GraphNode n1, GraphNode n2) {
        GraphEdge graphEdge = new GraphEdge(id, new Pair<>(n1, n2), (n1.getLevel() + n2.getLevel()) / 2);
        Edge edge = this.addEdge(graphEdge.getId(), n1, n2);
        edge.setAttribute(ElementAttributes.LEVEL, (n1.getLevel() + n2.getLevel()) / 2);
        if (n1 instanceof InteriorNode || n2 instanceof InteriorNode) {
            edge.setAttribute(ElementAttributes.STYLE, ElementAttributes.BLUE);
        }
        edges.put(graphEdge.getId(), graphEdge);
        return graphEdge;
    }

    public GraphEdge insertGraphEdge(GraphEdge graphEdge) {
        Edge edge = this.addEdge(graphEdge.getId(), graphEdge.getNode0().getId(), graphEdge.getNode1().getId());
        edge.setAttribute(ElementAttributes.LEVEL, graphEdge.getLevel());
        if (graphEdge.getNode0() instanceof InteriorNode || graphEdge.getNode1() instanceof InteriorNode) {
            edge.setAttribute(ElementAttributes.STYLE, ElementAttributes.BLUE);
        }
        edges.put(graphEdge.getId(), graphEdge);
        return graphEdge;
    }

    public boolean areNodesConnected(GraphNode n1, GraphNode n2) {
        return edges.values().stream()
                .anyMatch(egde -> checkEdgeEndNodes(egde, n1, n2));
    }

    public boolean checkEdgeEndNodes(GraphEdge edge, GraphNode n1, GraphNode n2) {
        final Pair<GraphNode, GraphNode> edgeNodes = edge.getEdgeNodes();
        return (edgeNodes.getValue0() == n1 && edgeNodes.getValue1() == n2)
                || (edgeNodes.getValue0() == n2 && edgeNodes.getValue1() == n1);
    }

    public Optional<GraphNode> getGraphNode(String id) {
        return Optional.ofNullable(nodes.get(id));
    }

    public Collection<GraphNode> getGraphNodes() {
        return nodes.values();
    }

    public Optional<GraphEdge> getEdge(GraphNode n1, GraphNode n2) {
        return Optional.ofNullable(edges.get(n1.getEdgeBetween(n2).getId()));
    }

    public Collection<GraphEdge> getEdges() {
        return edges.values();
    }

    public GraphModel getGraphModel(double level) {
        GraphModel graphModel = new GraphModel(this.id + "=" + level);

        nodes.values().stream()
                .filter(node -> node.getLevel() == level)
                .forEach(graphModel::insertGraphNode);

        edges.values().stream()
                .filter(edge -> edge.getLevel() == level)
                .forEach(graphModel::insertGraphEdge);

        return graphModel;
    }

    public GraphModel getGraphModelUpTo(double level) {
        GraphModel graphModel = new GraphModel(this.id + "<=" + level);

        nodes.values().stream()
                .filter(node -> node.getLevel() <= level)
                .forEach(graphModel::insertGraphNode);

        edges.values().stream()
                .filter(edge -> edge.getLevel() <= level)
                .forEach(graphModel::insertGraphEdge);

        return graphModel;
    }

    private boolean isNodeHereAlready(GraphNode node) {
        for (GraphNode n : nodes.values()) {
            if (n.getCoordinates().equals(node.getCoordinates())) return true;
        }
        return false;
    }
}
