package pl.edu.agh.gg.transform;

import pl.edu.agh.gg.model.*;

import java.util.*;

public class P3 extends Transformation {

    @Override
    public boolean isApplicable(GraphModel graphModel, GraphNode interiorNode, boolean isHorizontal) {
        return interiorNode.isSymbolUpperCase() && orientate(graphModel, interiorNode.getAdjacentENodes(), isHorizontal).isPresent();
    }

    @Override
    public void transform(GraphModel graphModel, GraphNode interiorNode, boolean isHorizontal) {

        // Change symbol to lowercase
        interiorNode.symbolToLowerCase();

        int currentNodeLevel = (int) interiorNode.getLevel();
        int nextLevel = currentNodeLevel + 1;

        GraphNode[] oldNodes = orientate(graphModel, interiorNode.getAdjacentENodes(), isHorizontal).get();

        List<GraphNode> nodes = new ArrayList<>();

        GraphNode leftUpper  = oldNodes[0];
        GraphNode rightUpper = oldNodes[1];
        GraphNode lowerRight = oldNodes[2];
        GraphNode lowerLeft  = oldNodes[3];

        // Create nodes one level below
        GraphNode n1 = new ENode(graphModel, getNodeName(interiorNode, "n1"), Coordinates.createCoordinatesWithOffset(leftUpper.getXCoordinate(), leftUpper.getYCoordinate(), nextLevel));

        double n2XCoordinate = (leftUpper.getXCoordinate() + rightUpper.getXCoordinate()) / 2.;
        double n2YCoordinate = (leftUpper.getYCoordinate() + rightUpper.getYCoordinate()) / 2.;
        GraphNode n2 = new ENode(graphModel, getNodeName(interiorNode, "n2"), Coordinates.createCoordinatesWithOffset(n2XCoordinate, n2YCoordinate, nextLevel));

        GraphNode n3 = new ENode(graphModel, getNodeName(interiorNode, "n3"), Coordinates.createCoordinatesWithOffset(rightUpper.getXCoordinate(), rightUpper.getYCoordinate(), nextLevel));

        double n4XCoordinate = (leftUpper.getXCoordinate() + lowerLeft.getXCoordinate()) / 2.;
        double n4YCoordinate = (leftUpper.getYCoordinate() + lowerLeft.getYCoordinate()) / 2.;
        GraphNode n4 = new ENode(graphModel, getNodeName(interiorNode, "n4"), Coordinates.createCoordinatesWithOffset(n4XCoordinate, n4YCoordinate, nextLevel));

        double n5XCoordinate = (leftUpper.getXCoordinate() + lowerRight.getXCoordinate()) / 2.;
        double n5YCoordinate = (leftUpper.getYCoordinate() + lowerRight.getYCoordinate()) / 2.;
        GraphNode n5 = new ENode(graphModel, getNodeName(interiorNode, "n5"), Coordinates.createCoordinatesWithOffset(n5XCoordinate, n5YCoordinate, nextLevel));

        double n6XCoordinate = (rightUpper.getXCoordinate() + lowerRight.getXCoordinate()) / 2.;
        double n6YCoordinate = (rightUpper.getYCoordinate() + lowerRight.getYCoordinate()) / 2.;
        GraphNode n6 = new ENode(graphModel, getNodeName(interiorNode, "n6"), Coordinates.createCoordinatesWithOffset(n6XCoordinate, n6YCoordinate, nextLevel));

        GraphNode n7 = new ENode(graphModel, getNodeName(interiorNode, "n7"), Coordinates.createCoordinatesWithOffset(lowerLeft.getXCoordinate(), lowerLeft.getYCoordinate(), nextLevel));

        double n8XCoordinate = (lowerLeft.getXCoordinate() + lowerRight.getXCoordinate()) / 2.;
        double n8YCoordinate = (lowerLeft.getYCoordinate() + lowerRight.getYCoordinate()) / 2.;
        GraphNode n8 = new ENode(graphModel, getNodeName(interiorNode, "n8"), Coordinates.createCoordinatesWithOffset(n8XCoordinate, n8YCoordinate, nextLevel));

        GraphNode n9 = new ENode(graphModel, getNodeName(interiorNode, "n9"), Coordinates.createCoordinatesWithOffset(lowerRight.getXCoordinate(), lowerRight.getYCoordinate(), nextLevel));

        // Interior nodes
        double I1XCoordinate = (n1.getXCoordinate() + n5.getXCoordinate()) / 2.;
        double I1YCoordinate = (n1.getYCoordinate() + n5.getYCoordinate()) / 2.;
        GraphNode newInterior1 = new InteriorNode(graphModel, getNodeName(interiorNode, "i1"), Coordinates.createCoordinatesWithoutOffset(I1XCoordinate, I1YCoordinate, nextLevel));

        double I2XCoordinate = (n2.getXCoordinate() + n6.getXCoordinate()) / 2.;
        double I2YCoordinate = (n2.getYCoordinate() + n6.getYCoordinate()) / 2.;
        GraphNode newInterior2 = new InteriorNode(graphModel, getNodeName(interiorNode, "i2"), Coordinates.createCoordinatesWithoutOffset(I2XCoordinate, I2YCoordinate, nextLevel));

        double I3XCoordinate = (n4.getXCoordinate() + n8.getXCoordinate()) / 2.;
        double I3YCoordinate = (n4.getYCoordinate() + n8.getYCoordinate()) / 2.;
        GraphNode newInterior3 = new InteriorNode(graphModel, getNodeName(interiorNode, "i3"), Coordinates.createCoordinatesWithoutOffset(I3XCoordinate, I3YCoordinate, nextLevel));

        double I4XCoordinate = (n5.getXCoordinate() + n9.getXCoordinate()) / 2.;
        double I4YCoordinate = (n5.getYCoordinate() + n9.getYCoordinate()) / 2.;
        GraphNode newInterior4 = new InteriorNode(graphModel, getNodeName(interiorNode, "i4"), Coordinates.createCoordinatesWithoutOffset(I4XCoordinate, I4YCoordinate, nextLevel));


        // Add nodes for visualizing edges
        nodes.add(graphModel.insertGraphNode(n1)); //0
        nodes.add(graphModel.insertGraphNode(n2)); //1
        nodes.add(graphModel.insertGraphNode(n3)); //2
        nodes.add(graphModel.insertGraphNode(n4)); //3
        nodes.add(graphModel.insertGraphNode(n5)); //4
        nodes.add(graphModel.insertGraphNode(n6)); //5
        nodes.add(graphModel.insertGraphNode(n7)); //6
        nodes.add(graphModel.insertGraphNode(n8)); //7
        nodes.add(graphModel.insertGraphNode(n9)); //8
        nodes.add(graphModel.insertGraphNode(newInterior1)); //9
        nodes.add(graphModel.insertGraphNode(newInterior2)); //10
        nodes.add(graphModel.insertGraphNode(newInterior3)); //11
        nodes.add(graphModel.insertGraphNode(newInterior4)); //12


        // Insert edges for visualization
        graphModel.insertGraphEdge(getEdgeName(interiorNode, nodes.get(9)), interiorNode, nodes.get(9));
        graphModel.insertGraphEdge(getEdgeName(interiorNode, nodes.get(10)), interiorNode, nodes.get(10));

        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(9)), nodes.get(0),nodes.get(9));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(9)), nodes.get(1),nodes.get(9));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(3), nodes.get(9)), nodes.get(3),nodes.get(9));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(9)), nodes.get(4),nodes.get(9));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(1)), nodes.get(0),nodes.get(1));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(1), nodes.get(4)), nodes.get(1),nodes.get(4));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(3)), nodes.get(4),nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(0), nodes.get(3)), nodes.get(0),nodes.get(3));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(3), nodes.get(6)), nodes.get(3),nodes.get(6));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(3), nodes.get(11)), nodes.get(3),nodes.get(11));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(6), nodes.get(11)), nodes.get(6),nodes.get(11));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(7), nodes.get(11)), nodes.get(7),nodes.get(11));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(7), nodes.get(12)), nodes.get(7),nodes.get(12));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(7), nodes.get(4)), nodes.get(7),nodes.get(4));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(7), nodes.get(8)), nodes.get(7),nodes.get(8));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(7), nodes.get(6)), nodes.get(7),nodes.get(6));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(8), nodes.get(12)), nodes.get(8),nodes.get(12));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(8), nodes.get(5)), nodes.get(8),nodes.get(5));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(5), nodes.get(4)), nodes.get(5),nodes.get(4));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(5), nodes.get(10)), nodes.get(5),nodes.get(10));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(5), nodes.get(12)), nodes.get(5),nodes.get(12));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(2), nodes.get(1)), nodes.get(2),nodes.get(1));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(2), nodes.get(10)), nodes.get(2),nodes.get(10));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(2), nodes.get(5)), nodes.get(2),nodes.get(5));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(11)), nodes.get(4),nodes.get(11));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(12)), nodes.get(4),nodes.get(12));
        graphModel.insertGraphEdge(getEdgeName(nodes.get(4), nodes.get(10)), nodes.get(4),nodes.get(10));

        // Insert edges for transformations
        n1.addNeighbourENode(n2);
        n1.addNeighbourENode(newInterior1);
        n1.addNeighbourENode(n4);

        n2.addNeighbourENode(n1);
        n2.addNeighbourENode(n3);
        n2.addNeighbourENode(newInterior1);
        n2.addNeighbourENode(newInterior2);

        n3.addNeighbourENode(n2);
        n3.addNeighbourENode(newInterior2);
        n3.addNeighbourENode(n6);

        n4.addNeighbourENode(n1);
        n4.addNeighbourENode(newInterior1);
        n4.addNeighbourENode(n5);
        n4.addNeighbourENode(n7);

        n5.addNeighbourENode(n4);
        n5.addNeighbourENode(n2);
        n5.addNeighbourENode(n6);
        n5.addNeighbourENode(n8);
        n5.addNeighbourENode(newInterior1);
        n5.addNeighbourENode(newInterior2);
        n5.addNeighbourENode(newInterior3);
        n5.addNeighbourENode(newInterior4);

        n6.addNeighbourENode(n1);
        n6.addNeighbourENode(n5);
        n6.addNeighbourENode(n9);
        n6.addNeighbourENode(newInterior2);
        n6.addNeighbourENode(newInterior4);

        n7.addNeighbourENode(n4);
        n7.addNeighbourENode(n8);
        n7.addNeighbourENode(newInterior3);

        n8.addNeighbourENode(n7);
        n8.addNeighbourENode(n5);
        n8.addNeighbourENode(n9);
        n8.addNeighbourENode(newInterior3);
        n8.addNeighbourENode(newInterior4);

        n9.addNeighbourENode(n8);
        n9.addNeighbourENode(n6);
        n9.addNeighbourENode(newInterior4);

        newInterior1.addNeighbourENode(n1);
        newInterior1.addNeighbourENode(n2);
        newInterior1.addNeighbourENode(n4);
        newInterior1.addNeighbourENode(n5);

        newInterior2.addNeighbourENode(n2);
        newInterior2.addNeighbourENode(n3);
        newInterior2.addNeighbourENode(n5);
        newInterior2.addNeighbourENode(n6);

        newInterior3.addNeighbourENode(n4);
        newInterior3.addNeighbourENode(n5);
        newInterior3.addNeighbourENode(n7);
        newInterior3.addNeighbourENode(n8);

        newInterior4.addNeighbourENode(n5);
        newInterior4.addNeighbourENode(n6);
        newInterior4.addNeighbourENode(n8);
        newInterior4.addNeighbourENode(n9);
    }

    private Optional<GraphNode[]> orientate(GraphModel graphModel, GraphNode[] nodes, boolean isHorizontal) {

        Optional<GraphNode[]> result = Optional.empty();

        if (nodes.length != 4) return result;

        Set<GraphNode> nodesSet = new HashSet<>(Arrays.asList(nodes));

        GraphNode leftUpper = nodes[0];

        for (GraphNode node : nodes) {
            if (node.getXCoordinate() < leftUpper.getXCoordinate() &&
                    node.getYCoordinate() < leftUpper.getYCoordinate()) {
                leftUpper = node;
            }
        }

        nodesSet.remove(leftUpper);

        Optional<GraphNode> rightUpper = Optional.empty();

        for (GraphNode node : nodesSet) {
            if (!rightUpper.isPresent() && graphModel.areNodesConnected(leftUpper, node)) {
                // It's the first element that could be rightUpper
                rightUpper = Optional.of(node);
            } else if (rightUpper.isPresent() && node.getYCoordinate() > rightUpper.get().getYCoordinate()) {
                // Here we orientate our graph to be sure we get 1->2->3->4 instead of 1->4->3->2
                rightUpper = Optional.of(node);
            }
        }

        if (!rightUpper.isPresent()) {
            return result;
        } else {
            nodesSet.remove(rightUpper.get());
        }

        Optional<GraphNode> rightLower = Optional.empty();

        for (GraphNode node : nodesSet) {
            if (graphModel.areNodesConnected(node, rightUpper.get())) {
                rightLower = Optional.of(node);
            }
        }

        if (!rightLower.isPresent()) {
            return result;
        } else {
            nodesSet.remove(rightLower.get());
        }

        Optional<GraphNode> leftLower = Optional.empty();

        for (GraphNode node : nodesSet) {
            if (graphModel.areNodesConnected(node, rightLower.get()) && graphModel.areNodesConnected(node, leftUpper)) {
                leftLower = Optional.of(node);
            }
        }

        if (!leftLower.isPresent()) {
            return result;
        }

        if (!isHorizontal) {
            return Optional.of(new GraphNode[]{leftUpper, rightUpper.get(), rightLower.get(), leftLower.get()});
        } else {
            return Optional.of(new GraphNode[]{leftUpper, leftLower.get(), rightLower.get(), rightUpper.get()});
        }
    }
}
