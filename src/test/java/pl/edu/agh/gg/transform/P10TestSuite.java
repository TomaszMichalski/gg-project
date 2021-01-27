package pl.edu.agh.gg.transform;

import org.junit.Test;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.edu.agh.gg.examples.MainP10.generateGraphModel;

public class P10TestSuite {
  private TransformationMergeProposal p10 = new P10();
  @Test
  public void isP10ApplicableForCorrectGraphTest() {
    var graphModel = getGraphModel();

    assertTrue(p10.isApplicable(graphModel, graphModel.getGraphNode("e1i1n5").get()));
  }

  @Test
  public void isP10NotApplicableWhenRemoveCommonENode() {
    var graphModel = getGraphModel();

    graphModel.getGraphNodes()
        .forEach(graphNode -> graphNode.removeNeighbourENode(graphModel.getGraphNode("e1i1i1n9").get()));
    graphModel.removeGraphNode("e1i1i1n9");

    assertFalse(p10.isApplicable(graphModel, graphModel.getGraphNode("e1i1n5").get()));
  }

  @Test
  public void isP10NotApplicableWhenENodeCoordinatesAreWrong() {
    var graphModel = getGraphModel();

    var eNode1 = (ENode) graphModel.getGraphNode("e1i1i1n9").get();
    eNode1.getCoordinates().setY(2137);

    assertFalse(p10.isApplicable(graphModel, graphModel.getGraphNode("e1i1n5").get()));
  }

  @Test(expected = IllegalStateException.class)
  public void isP10TransformThrowingExceptionIfNotApplicable() {
    var graphModel = getGraphModel();

    var eNode1 = (ENode) graphModel.getGraphNode("e1i1i1n9").get();
    eNode1.getCoordinates().setY(2137);

    p10.transform(graphModel, graphModel.getGraphNode("e1i1n5").get());
  }

  @Test
  public void isP10TransformTransformingProperly() {
    var graphModel = getGraphModel();


    assertTrue(graphModel.getGraphNode("e1i1i1").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i3").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n9").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n9").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n8").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n7").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i3n2").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i3n1").isPresent());
    assertFalse(graphModel.areNodesConnected("e1i1i1n8", "e1i1i3n5"));
    assertFalse(graphModel.areNodesConnected("e1i1i1n7", "e1i1i3n4"));

    p10.transform(graphModel, graphModel.getGraphNode("e1i1n5").get());

    assertTrue(graphModel.getGraphNode("e1i1i1").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i3").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n9").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n9").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n8").isPresent());
    assertTrue(graphModel.getGraphNode("e1i1i1n7").isPresent());
    assertFalse(graphModel.getGraphNode("e1i1i3n2").isPresent());
    assertFalse(graphModel.getGraphNode("e1i1i3n1").isPresent());
    assertTrue(graphModel.areNodesConnected("e1i1i1n7", "e1i1i3n4"));
  }

  private GraphModel getGraphModel() {
    GraphModel graphModel = generateGraphModel();
    Transformation p1 = new P1();
    Transformation p3 = new P3();
    TransformationMergeProposal p9 = new P9();

    p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
    p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);
    p3.transform(graphModel, graphModel.getGraphNode("e1i1i1").get(), false);
    p3.transform(graphModel, graphModel.getGraphNode("e1i1i2").get(), false);
    p3.transform(graphModel, graphModel.getGraphNode("e1i1i3").get(), false);
    p3.transform(graphModel, graphModel.getGraphNode("e1i1i4").get(), false);
    p9.transform(graphModel, graphModel.getGraphNode("e1i1n2").get());
    p9.transform(graphModel, graphModel.getGraphNode("e1i1n6").get());
    p9.transform(graphModel, graphModel.getGraphNode("e1i1n8").get());

    return graphModel;
  }
}
