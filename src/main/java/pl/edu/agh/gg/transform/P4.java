package pl.edu.agh.gg.transform;

import pl.edu.agh.gg.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class P4 extends Transformation {

    @Override
    public boolean isApplicable(GraphModel graphModel, GraphNode interiorNode, boolean isHorizontal) {
        return interiorNode.isSymbolUpperCase() && orientate(graphModel, interiorNode, isHorizontal).isPresent();
    }

    @Override
    public void transform(GraphModel graphModel, GraphNode interiorNode, boolean isHorizontal) {
        // Change symbol to lowercase
        interiorNode.symbolToLowerCase();

        int currentNodeLevel = (int) interiorNode.getLevel();
        int nextLevel = currentNodeLevel + 1;

        GraphNode[] oldNodes = orientate(graphModel, interiorNode, isHorizontal).get();

        List<GraphNode> nodes = new ArrayList<>();

        // Create nodes one level below
        GraphNode n1 = new ENode(graphModel, getNodeName(interiorNode, "n1"), Coordinates.createCoordinatesWithOffset(oldNodes[0].getXCoordinate(), oldNodes[0].getYCoordinate(), nextLevel));
        double n2XCoordinate = (oldNodes[0].getXCoordinate() + oldNodes[1].getXCoordinate()) / 2.;
        double n2YCoordinate = (oldNodes[0].getYCoordinate() + oldNodes[1].getYCoordinate()) / 2.;
        GraphNode n2 = new ENode(graphModel, getNodeName(interiorNode, "n2"), Coordinates.createCoordinatesWithOffset(n2XCoordinate, n2YCoordinate, nextLevel));
        GraphNode n3 = new ENode(graphModel, getNodeName(interiorNode, "n3"), Coordinates.createCoordinatesWithOffset(oldNodes[1].getXCoordinate(), oldNodes[1].getYCoordinate(), nextLevel));
        GraphNode n4 = new ENode(graphModel, getNodeName(interiorNode, "n4"), Coordinates.createCoordinatesWithOffset(oldNodes[2].getXCoordinate(), oldNodes[2].getYCoordinate(), nextLevel));
        double n5XCoordinate = (oldNodes[2].getXCoordinate() + oldNodes[3].getXCoordinate()) / 2.;
        double n5YCoordinate = (oldNodes[2].getYCoordinate() + oldNodes[3].getYCoordinate()) / 2.;
        GraphNode n5 = new ENode(graphModel, getNodeName(interiorNode, "n5"), Coordinates.createCoordinatesWithOffset(n5XCoordinate, n5YCoordinate, nextLevel));
        GraphNode n6 = new ENode(graphModel, getNodeName(interiorNode, "n6"), Coordinates.createCoordinatesWithOffset(oldNodes[3].getXCoordinate(), oldNodes[3].getYCoordinate(), nextLevel));


        double I1XCoordinate = !isHorizontal ? (n1.getXCoordinate() + n2.getXCoordinate()) / 2. : (n1.getXCoordinate() + n6.getXCoordinate()) / 2.;
        double I1YCoordinate = !isHorizontal ? (n1.getYCoordinate() + n6.getYCoordinate()) / 2. : (n1.getYCoordinate() + n2.getYCoordinate()) / 2.;
        GraphNode newInterior1 = new InteriorNode(graphModel, getNodeName(interiorNode, "i1"), Coordinates.createCoordinatesWithoutOffset(I1XCoordinate, I1YCoordinate, nextLevel));

        double I2XCoordinate = !isHorizontal ? (n2.getXCoordinate() + n3.getXCoordinate()) / 2. : (n2.getXCoordinate() + n5.getXCoordinate()) / 2.;
        double I2YCoordinate = !isHorizontal ? (n2.getYCoordinate() + n5.getYCoordinate()) / 2. : (n2.getYCoordinate() + n3.getYCoordinate()) / 2.;
        GraphNode newInterior2 = new InteriorNode(graphModel, getNodeName(interiorNode, "i2"), Coordinates.createCoordinatesWithoutOffset(I2XCoordinate, I2YCoordinate, nextLevel));

        // Add nodes for visualizing edges
        nodes.add(graphModel.insertGraphNode(newInterior1));
        nodes.add(graphModel.insertGraphNode(newInterior2));
        nodes.add(graphModel.insertGraphNode(n1));
        nodes.add(graphModel.insertGraphNode(n2));
        nodes.add(graphModel.insertGraphNode(n3));
        nodes.add(graphModel.insertGraphNode(n4));
        nodes.add(graphModel.insertGraphNode(n5));
        nodes.add(graphModel.insertGraphNode(n6));

        // Insert edges for visualization
        graphModel.insertGraphEdge(getEdgeName(interiorNode, nodes.get(0)), interiorNode, nodes.get(0));
        graphModel.insertGraphEdge(getEdgeName(interiorNode, nodes.get(1)), interiorNode, nodes.get(1));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(2)), nodes.get(0), nodes.get(2));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(3)), nodes.get(0), nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(6)), nodes.get(0), nodes.get(6));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(7)), nodes.get(0), nodes.get(7));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(3)), nodes.get(1), nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(4)), nodes.get(1), nodes.get(4));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(5)), nodes.get(1), nodes.get(5));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(6)), nodes.get(1), nodes.get(6));

        graphModel.insertGraphEdge(getEdgeName(nodes.get(2), nodes.get(3)), nodes.get(2), nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(2), nodes.get(7)), nodes.get(2), nodes.get(7));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(6), nodes.get(7)), nodes.get(6), nodes.get(7));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(6), nodes.get(3)), nodes.get(6), nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(6), nodes.get(5)), nodes.get(6), nodes.get(5));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(3)), nodes.get(4), nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(5)), nodes.get(4), nodes.get(5));


        // Insert edges for transformations
        newInterior1.addNeighbourENode(n1);
        newInterior1.addNeighbourENode(n2);
        newInterior1.addNeighbourENode(n5);
        newInterior1.addNeighbourENode(n6);

        newInterior2.addNeighbourENode(n2);
        newInterior2.addNeighbourENode(n3);
        newInterior2.addNeighbourENode(n4);
        newInterior2.addNeighbourENode(n5);

        n1.addNeighbourENode(n2);
        n1.addNeighbourENode(n6);

        n2.addNeighbourENode(n1);
        n2.addNeighbourENode(n3);
        n2.addNeighbourENode(n5);

        n3.addNeighbourENode(n4);
        n3.addNeighbourENode(n2);

        n4.addNeighbourENode(n5);
        n4.addNeighbourENode(n3);

        n5.addNeighbourENode(n6);
        n5.addNeighbourENode(n2);
        n5.addNeighbourENode(n4);

        n6.addNeighbourENode(n1);
        n6.addNeighbourENode(n5);
    }

    private Optional<GraphNode[]> orientate(GraphModel graphModel, GraphNode interiorNode, boolean isHorizontal) {

        Optional<GraphNode[]> result = Optional.empty();

        Set<GraphNode> cornerNodes = new HashSet<>(Arrays.asList(interiorNode.getAdjacentENodes()));

        if (cornerNodes.size() != 4) return result;

        Set<GraphNode> lowerNodes = cornerNodes.stream().filter(node ->
                Arrays.stream(node.getAdjacentENodes())
                        .filter(cornerNodes::contains)
                        .count() == 2
        ).collect(Collectors.toSet());

        Set<GraphNode> upperNodes = cornerNodes.stream().filter(node ->
                Arrays.stream(node.getAdjacentENodes())
                        .filter(cornerNodes::contains)
                        .count() == 1
        ).collect(Collectors.toSet());

        if (lowerNodes.size() != 2 && upperNodes.size() != 2) return result;


        GraphNode leftUpper = upperNodes.iterator().next();

        for (GraphNode node : upperNodes) {
            if (node.getXCoordinate() < leftUpper.getXCoordinate() &&
                    node.getYCoordinate() < leftUpper.getYCoordinate()) {
                leftUpper = node;
            }
        }

        upperNodes.remove(leftUpper);
        GraphNode rightUpper = upperNodes.iterator().next();
        
        if (graphModel.areNodesConnected(leftUpper, rightUpper))
            return result;

        List<GraphNode> rightLowers = lowerNodes.stream()
                .filter(node -> graphModel.areNodesConnected(rightUpper, node))
                .collect(Collectors.toList());
        
        if (rightLowers.size() != 1)
            return result;


        GraphNode rightLower = rightLowers.iterator().next();

        lowerNodes.remove(rightLower);

        GraphNode leftLower = lowerNodes.iterator().next();

        if (!graphModel.areNodesConnected(leftLower, rightLower))
            return result;

        if (!graphModel.areNodesConnected(leftLower, leftUpper))
            return result;

        Optional<GraphNode> between = Arrays.stream(rightUpper.getAdjacentENodes())
                .filter(Arrays.asList(leftUpper.getAdjacentENodes())::contains)
        .findAny();

        if (!between.isPresent()) {
            return result;
        }

        if (!isHorizontal) {
            return Optional.of(new GraphNode[]{leftUpper, rightUpper, rightLower, leftLower});
        } else {
            return Optional.of(new GraphNode[]{leftUpper, leftLower, rightLower, rightUpper});
        }
    }
}
