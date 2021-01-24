package pl.edu.agh.gg.examples;

import pl.edu.agh.gg.model.*;
import pl.edu.agh.gg.transform.P7;
import pl.edu.agh.gg.transform.Transformation;
import pl.edu.agh.gg.visualization.Visualizer;

import static pl.edu.agh.gg.common.Utils.*;

public class MainP7 {
    public static void main(String[] args) {
        GraphModel graphModel = generateGraphModel();

        Transformation p7 = new P7();

        if (p7.isApplicable(graphModel, graphModel.getGraphNode("i1").get(), false)) {
            p7.transform(graphModel, graphModel.getGraphNode("i1").get(), false);
        }

        Visualizer visualizer = new Visualizer(graphModel);
        visualizer.visualize();
    }

    private static GraphModel generateGraphModel() {
        GraphModel g = new GraphModel("P7");

        InteriorNode i1 = addI(g, "i1", 0, 0, 0);
        GraphNode e1 = addE(g, "e1", -1, 1, 0);
        GraphNode e12 = addE(g, "e12", 0, 1, 0);
        GraphNode e2 = addE(g, "e2", 1, 1, 0);
        GraphNode e13 = addE(g, "e13", -1, 0, 0);
        GraphNode e24 = addE(g, "e24", 1, 0, 0);
        GraphNode e3 = addE(g, "e3", -1, -1, 0);
        GraphNode e4 = addE(g, "e4", 1, -1, 0);

        addEdge(g, i1, e1);
        addEdge(g, i1, e2);
        addEdge(g, i1, e3);
        addEdge(g, i1, e4);

        addEdge(g, e1, e12);
        addEdge(g, e12, e2);

        addEdge(g, e1, e13);
        addEdge(g, e2, e24);

        addEdge(g, e13, e3);
        addEdge(g, e24, e4);

        addEdge(g, e3, e4);

        return g;
    }
}
