package pl.edu.agh.gg.examples;

import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.transform.P1;
import pl.edu.agh.gg.transform.P3;
import pl.edu.agh.gg.transform.Transformation;
import pl.edu.agh.gg.visualization.Visualizer;

public class MainP3 {

    public static void main(String[] args) {
        GraphModel graphModel = generateGraphModel();

        Transformation p1 = new P1();
        Transformation p3 = new P3();

        if (p1.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false)) {
            p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        }

        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false)) {
            p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);
        }

        Visualizer visualizer = new Visualizer(graphModel);
        visualizer.visualize();

    }

    public static GraphModel generateGraphModel() {
        GraphModel graphModel = new GraphModel("Main");
        graphModel.insertGraphNode(new ENode(graphModel, "e1", new Coordinates(0.0, 0.0, 1)));
        return graphModel;
    }
}
