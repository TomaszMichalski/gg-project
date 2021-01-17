package pl.edu.agh.gg.examples;

import pl.edu.agh.gg.model.Coordinates;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.transform.*;
import pl.edu.agh.gg.visualization.Visualizer;

public class MainP9 {

    public static void main(String[] args) {
        GraphModel graphModel = generateGraphModel();

        Transformation p1 = new P1();
        Transformation p3 = new P3();
        TransformationMergeProposal p9 = new P9();

        if (p1.isApplicable(graphModel, graphModel.getGraphNode("e1").get(), false)) {
            p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        }

        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1").get(), false)) {
            p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);
        }

//        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1i1").get(), false)) {
//            p3.transform(graphModel, graphModel.getGraphNode("e1i1i1").get(), false);
//        }
//
//        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1i2").get(), false)) {
//            p3.transform(graphModel, graphModel.getGraphNode("e1i1i2").get(), false);
//        }

        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1i3").get(), false)) {
            p3.transform(graphModel, graphModel.getGraphNode("e1i1i3").get(), false);
        }

        if (p3.isApplicable(graphModel, graphModel.getGraphNode("e1i1i4").get(), false)) {
            p3.transform(graphModel, graphModel.getGraphNode("e1i1i4").get(), false);
        }

//        if (p9.isApplicable(graphModel, graphModel.getGraphNode("e1i1n2").get())) {
//            p9.transform(graphModel, graphModel.getGraphNode("e1i1n2").get());
//        }

//        if (p9.isApplicable(graphModel, graphModel.getGraphNode("e1i1n4").get())) {
//            p9.transform(graphModel, graphModel.getGraphNode("e1i1n4").get());
//        }
//
//        if (p9.isApplicable(graphModel, graphModel.getGraphNode("e1i1n6").get())) {
//            p9.transform(graphModel, graphModel.getGraphNode("e1i1n6").get());
//        }
//
        if (p9.isApplicable(graphModel, graphModel.getGraphNode("e1i1n8").get())) {
            p9.transform(graphModel, graphModel.getGraphNode("e1i1n8").get());
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
