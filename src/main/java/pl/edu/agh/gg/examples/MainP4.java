package pl.edu.agh.gg.examples;

import pl.edu.agh.gg.model.*;
import pl.edu.agh.gg.transform.P4;
import pl.edu.agh.gg.transform.Transformation;
import pl.edu.agh.gg.visualization.Visualizer;

public class MainP4 {


    public static void main(String[] args) {
        GraphModel graphModel = generateGraphModel();

        Transformation p4 = new P4();

        if (p4.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false)) {
            p4.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        }

        Visualizer visualizer = new Visualizer(graphModel);
        visualizer.visualize();
    }

    public static GraphModel generateGraphModel() {
        GraphModel graphModel = new GraphModel("Main");

        // Create nodes one level below
        GraphNode interiorNode = new InteriorNode(graphModel, "e1", Coordinates.createCoordinatesWithOffset(0.0, 0.0, 1));
        GraphNode e1 = new ENode(graphModel, "e1e1", Coordinates.createCoordinatesWithOffset(- 2.0, 2.0, 1));
        GraphNode e2 = new ENode(graphModel, "e1e2", Coordinates.createCoordinatesWithOffset(0.0, 2.5, 1));
        GraphNode e3 = new ENode(graphModel, "e1e3", Coordinates.createCoordinatesWithOffset(2.0, 2.0, 1));
        GraphNode e4 = new ENode(graphModel, "e1e4", Coordinates.createCoordinatesWithOffset(2.0, - 2.0, 1));
        GraphNode e5 = new ENode(graphModel, "e1e5", Coordinates.createCoordinatesWithOffset(- 2.0, - 2.0, 1));


        // Add nodes for visualizing edges
        graphModel.insertGraphNode(interiorNode);
        graphModel.insertGraphNode(e1);
        graphModel.insertGraphNode(e2);
        graphModel.insertGraphNode(e3);
        graphModel.insertGraphNode(e4);
        graphModel.insertGraphNode(e5);

        // Insert edges for visualization
        graphModel.insertGraphEdge("edge1", interiorNode, e1);
        graphModel.insertGraphEdge("edge2", interiorNode, e3);
        graphModel.insertGraphEdge("edge3", interiorNode, e4);
        graphModel.insertGraphEdge("edge4", interiorNode, e5);
        graphModel.insertGraphEdge("edge5", e1, e2);
        graphModel.insertGraphEdge("edge6", e2, e3);
        graphModel.insertGraphEdge("edge7", e3, e4);
        graphModel.insertGraphEdge("edge8", e4, e5);
        graphModel.insertGraphEdge("edge9", e5, e1);

        // Insert edges for transformations
        interiorNode.addNeighbourENode(e1);
        interiorNode.addNeighbourENode(e3);
        interiorNode.addNeighbourENode(e4);
        interiorNode.addNeighbourENode(e5);

        e1.addNeighbourENode(e2);
        e1.addNeighbourENode(e5);

        e2.addNeighbourENode(e1);
        e2.addNeighbourENode(e3);

        e3.addNeighbourENode(e2);
        e3.addNeighbourENode(e4);

        e4.addNeighbourENode(e3);
        e4.addNeighbourENode(e5);

        e5.addNeighbourENode(e4);
        e5.addNeighbourENode(e1);

        return graphModel;
    }
}
