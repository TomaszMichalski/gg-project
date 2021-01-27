package pl.edu.agh.gg.transform;

import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;

public interface TransformationMergeProposal {

    boolean isApplicable(GraphModel graph, GraphNode eNode);

    void transform(GraphModel graph, GraphNode eNode);
}
