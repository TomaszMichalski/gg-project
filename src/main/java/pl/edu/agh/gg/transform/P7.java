package pl.edu.agh.gg.transform;

import pl.edu.agh.gg.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.edu.agh.gg.common.Utils.*;

public class P7 extends Transformation {

    private static final int REQUIRED_EDGE_COUNT = 11;

    protected Optional<LeftEmbedding> leftCache;
    protected Optional<RightEmbedding> rightCache;
    private GraphModel graphModel;
    private InteriorNode interiorNode;

    @Override
    public boolean isApplicable(GraphModel graphModel, GraphNode interiorNode, boolean ignored) {
        this.graphModel = graphModel;
        if (!(interiorNode instanceof InteriorNode)) {
            return false;
        }
        this.interiorNode = (InteriorNode) interiorNode;
        this.leftCache = getLeftEmbedding();
        return leftCache.isPresent();
    }

    @Override
    public void transform(GraphModel graphModel, GraphNode interiorNode, boolean ignored) {
        if (this.graphModel != graphModel || this.interiorNode != interiorNode) {
            throw new IllegalStateException("Inconsistent arguments of .isApplicable() and .transform()");
        }
        LeftEmbedding l = leftCache.orElseThrow(() ->
                new IllegalStateException(".isApplicable() must return true before calling .transform()"));

        // Change symbol to lowercase
        l.i.symbolToLowerCase();

        RightEmbedding r = new RightEmbedding();
        rightCache = Optional.of(r);

        // Push down nodes from the previous level
        r.x1 = pushDown(l.x1, "e1");
        r.x12 = pushDown(l.x12, "e12");
        r.x2 = pushDown(l.x2, "e2");
        r.x13 = pushDown(l.x13, "e13");
        r.x24 = pushDown(l.x24, "e24");
        r.x3 = pushDown(l.x3, "e3");
        r.x4 = pushDown(l.x4, "e4");

        // Create new edge nodes
        r.x14 = pushDown(l.i, "e14");
        r.x34 = addE(graphModel, getNodeName(l.i, "e34"), mid(r.x3, r.x4));

        // Create new interiors
        r.i1 = addI(graphModel, getNodeName(l.i, "i1"), mid(r.x1, r.x14));
        r.i2 = addI(graphModel, getNodeName(l.i, "i2"), mid(r.x2, r.x14));
        r.i3 = addI(graphModel, getNodeName(l.i, "i3"), mid(r.x3, r.x14));
        r.i4 = addI(graphModel, getNodeName(l.i, "i4"), mid(r.x4, r.x14));

        // Add interior-interior edges
        addEdge(graphModel, l.i, r.i1);
        addEdge(graphModel, l.i, r.i2);
        addEdge(graphModel, l.i, r.i3);
        addEdge(graphModel, l.i, r.i4);

        // Add interior-edge edges (clockwise)
        addEdge(graphModel, r.i1, r.x1);
        addEdge(graphModel, r.i1, r.x12);
        addEdge(graphModel, r.i1, r.x14);
        addEdge(graphModel, r.i1, r.x13);

        addEdge(graphModel, r.i2, r.x12);
        addEdge(graphModel, r.i2, r.x2);
        addEdge(graphModel, r.i2, r.x24);
        addEdge(graphModel, r.i2, r.x14);

        addEdge(graphModel, r.i3, r.x13);
        addEdge(graphModel, r.i3, r.x14);
        addEdge(graphModel, r.i3, r.x34);
        addEdge(graphModel, r.i3, r.x3);

        addEdge(graphModel, r.i4, r.x14);
        addEdge(graphModel, r.i4, r.x24);
        addEdge(graphModel, r.i4, r.x4);
        addEdge(graphModel, r.i4, r.x34);

        // Add edge-edge edges (clockwise starting from noon <=> by rows right to left)
        addEdge(graphModel, r.x2, r.x24);
        addEdge(graphModel, r.x12, r.x2);
        addEdge(graphModel, r.x12, r.x14);
        addEdge(graphModel, r.x1, r.x12);
        addEdge(graphModel, r.x1, r.x13);
        addEdge(graphModel, r.x24, r.x4);
        addEdge(graphModel, r.x14, r.x24);
        addEdge(graphModel, r.x14, r.x34);
        addEdge(graphModel, r.x13, r.x14);
        addEdge(graphModel, r.x13, r.x3);
        addEdge(graphModel, r.x34, r.x4);
        addEdge(graphModel, r.x3, r.x34);
    }

    private Optional<LeftEmbedding> getLeftEmbedding() {
        if (!interiorNode.isSymbolUpperCase()) {
            return Optional.empty();
        }

        LeftEmbedding e = new LeftEmbedding();
        e.i = interiorNode;

        List<ENode> es = Arrays.stream(interiorNode.getAdjacentENodes())
                .filter(n -> n instanceof ENode)
                .map(n -> (ENode) n)
                .collect(Collectors.toCollection(ArrayList::new));
        if (interiorNode.getAdjacentENodes().length != 4 || es.size() != 4) {
            return Optional.empty();
        }

        // Find x3 and x4 - the only connected E nodes, order doesn't matter due to P7 symmetry
        for (int i = 0; i < es.size(); ++i) {
            for (int j = i + 1; j < es.size(); ++j) {
                if (es.get(i).hasEdgeBetween(es.get(j))) {
                    // remove j first as it's always > i, so removal doesn't change i's index
                    e.x4 = es.remove(j);
                    e.x3 = es.remove(i);
                    break;
                }
            }
        }

        // Find midpoint of x1 and x2
        Optional<ENode> x12 = getMidpoint(es.get(0), es.get(1));
        if (!x12.isPresent()) {
            return Optional.empty();
        }
        e.x12 = x12.get();

        // Try to connect one e to x3, if it's not possible try the other
        for (int i = 0; i < es.size(); ++i) {
            Optional<ENode> midpoint = getMidpoint(es.get(i), e.x3);
            if (midpoint.isPresent()) {
                e.x13 = midpoint.get();
                e.x1 = es.remove(i);
                break;
            }
        }
        if (e.x1 == null) {
            return Optional.empty();
        }

        // The same for x4
        Optional<ENode> midpoint = getMidpoint(es.get(0), e.x4);
        if (!midpoint.isPresent()) {
            return Optional.empty();
        }
        e.x24 = midpoint.get();
        e.x2 = es.remove(0);

        // Make sure there are no additional edges. We know that the ones required are present, so count all edges
        // between points in the embedding and compare the counts.
        int edgeCount = 0;
        List<GraphNode> ns = Arrays.asList(e.i, e.x1, e.x12, e.x2, e.x13, e.x24, e.x3, e.x4);
        for (int i = 0; i < ns.size() - 1; ++i) {
            for (int j = i + 1; j < ns.size(); ++j) {
                if (ns.get(i).hasEdgeBetween(ns.get(j))) {
                    edgeCount++;
                }
            }
        }
        if (edgeCount != REQUIRED_EDGE_COUNT) {
            return Optional.empty();
        }

        return Optional.of(e);
    }

    private Optional<ENode> getMidpoint(GraphNode a, GraphNode b) {
        List<ENode> candidates = Arrays.stream(a.getAdjacentENodes())
                .filter(n -> n.getCoordinates().equals(mid(a, b)) && n.hasEdgeBetween(b))
                .filter(n -> n instanceof ENode)
                .map(n -> (ENode) n)
                .collect(Collectors.toList());
        if (candidates.size() < 1) {
            return Optional.empty();
        }
        // In case there's more than 1 possible node, pick the first one.
        return Optional.ofNullable(candidates.get(0));
    }

    private ENode pushDown(GraphNode n, String name) {
        Coordinates c = Coordinates.createCoordinatesWithOffset(n.getXCoordinate(), n.getYCoordinate(), n.getLevel() + 1);
        return addE(graphModel, getNodeName(interiorNode, name), c);
    }

    protected static final class LeftEmbedding {
        InteriorNode i;
        ENode x1;
        ENode x12;
        ENode x2;
        ENode x13;
        ENode x24;
        ENode x3;
        ENode x4;
    }

    protected static final class RightEmbedding {
        InteriorNode i1;
        InteriorNode i2;
        InteriorNode i3;
        InteriorNode i4;
        ENode x1;
        ENode x12;
        ENode x2;
        ENode x13;
        ENode x14;
        ENode x24;
        ENode x3;
        ENode x34;
        ENode x4;
    }
}
