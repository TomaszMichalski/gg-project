package pl.edu.agh.gg.transform;

import org.junit.Test;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.model.InteriorNode;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.edu.agh.gg.examples.MainP9.generateGraphModel;

public class P9TestSuite {

    @Test
    public void isP9ApplicableForCorrectGraphsTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        assertTrue(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertTrue(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenChildInteriorNodeRemovedTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        smallGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(smallGraphModel.getGraphNode("e1i1i1i2").get()));
        smallGraphModel.removeGraphNode("e1i1i1i2");

        bigGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(bigGraphModel.getGraphNode("e1i1i1i2").get()));
        bigGraphModel.removeGraphNode("e1i1i1i2");

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenChildInteriorNodeAdjacentENodeRemovedTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        smallGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(smallGraphModel.getGraphNode("e1i1i1n3").get()));
        smallGraphModel.removeGraphNode("e1i1i1n3");

        bigGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(bigGraphModel.getGraphNode("e1i1i1n3").get()));
        bigGraphModel.removeGraphNode("e1i1i1n3");

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenChildInteriorNodeEdgeRemovedTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        smallGraphModel.deleteGraphEdge("e1i1i1n3e1i1i1i2");
        smallGraphModel.getGraphNodes().stream()
                .filter(graphNode -> graphNode.getId().equals("e1i1i1i2"))
                .forEach(graphNode -> graphNode.removeNeighbourENode(smallGraphModel.getGraphNode("e1i1i1n3").get()));

        bigGraphModel.deleteGraphEdge("e1i1i1n3e1i1i1i2");
        bigGraphModel.getGraphNodes().stream()
                .filter(graphNode -> graphNode.getId().equals("e1i1i1i2"))
                .forEach(graphNode -> graphNode.removeNeighbourENode(bigGraphModel.getGraphNode("e1i1i1n3").get()));

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenENodeReplacedWithInteriorNodeTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        ENode eNode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n3").get();
        GraphNode interiorNode1 = new InteriorNode(smallGraphModel, "e1i1i1i5", eNode1.getCoordinates());
        eNode1.replaceWith(smallGraphModel, interiorNode1, Collections.emptyList());

        ENode eNode2 = (ENode) bigGraphModel.getGraphNode("e1i1i1n3").get();
        GraphNode interiorNode2 = new InteriorNode(bigGraphModel, "e1i1i1i5", eNode2.getCoordinates());
        eNode2.replaceWith(bigGraphModel, interiorNode2, Collections.emptyList());

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenENodeCoordinatesAreWrongTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        ENode eNode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n3").get();
        eNode1.getCoordinates().setX(1312414.0);

        ENode eNode2 = (ENode) bigGraphModel.getGraphNode("e1i1i1n3").get();
        eNode2.getCoordinates().setX(1312414.0);

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9NotApplicableWhenENodeLevelIsWrongTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        ENode eNode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n3").get();
        eNode1.getCoordinates().setLevel(3.0);

        ENode eNode2 = (ENode) bigGraphModel.getGraphNode("e1i1i1n3").get();
        eNode2.getCoordinates().setLevel(3.0);

        assertFalse(p9.isApplicable(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get()));
        assertFalse(p9.isApplicable(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get()));
    }

    @Test
    public void isP9TransformSuccessfulForCorrectGraphsTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        p9.transform(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get());

        ENode upperENode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n3").get();
        ENode middleENode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n6").get();
        ENode bottomENode1 = (ENode) smallGraphModel.getGraphNode("e1i1i1n9").get();

        assertTrue(upperENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2n2").get()));
        assertTrue(upperENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2i1").get()));
        assertTrue(upperENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i1n6").get()));

        assertTrue(middleENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i1n3").get()));
        assertTrue(middleENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2i1").get()));
        assertTrue(middleENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2n5").get()));
        assertTrue(middleENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2i3").get()));
        assertTrue(middleENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i1n9").get()));

        assertTrue(bottomENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i1n6").get()));
        assertTrue(bottomENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2i3").get()));
        assertTrue(bottomENode1.getAdjacentENodesList().contains(smallGraphModel.getGraphNode("e1i1i2n8").get()));

        p9.transform(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get());

        ENode upperENode2 = (ENode) bigGraphModel.getGraphNode("e1i1i1n3").get();
        ENode middleENode2 = (ENode) bigGraphModel.getGraphNode("e1i1i1n6").get();
        ENode bottomENode3 = (ENode) bigGraphModel.getGraphNode("e1i1i1n9").get();

        assertTrue(upperENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2n2").get()));
        assertTrue(upperENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2i1").get()));
        assertTrue(upperENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i1n6").get()));

        assertTrue(middleENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i1n3").get()));
        assertTrue(middleENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2i1").get()));
        assertTrue(middleENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2n5").get()));
        assertTrue(middleENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2i3").get()));
        assertTrue(middleENode2.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i1n9").get()));

        assertTrue(bottomENode3.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i1n6").get()));
        assertTrue(bottomENode3.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2i3").get()));
        assertTrue(bottomENode3.getAdjacentENodesList().contains(bigGraphModel.getGraphNode("e1i1i2n8").get()));
    }

    @Test(expected = RuntimeException.class)
    public void isP9TransformUnsuccessfulForIncorrectSmallGraphTest() {
        GraphModel smallGraphModel = getSmallGraphModel();
        TransformationMergeProposal p9 = new P9();

        smallGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(smallGraphModel.getGraphNode("e1i1i1i2").get()));
        smallGraphModel.removeGraphNode("e1i1i1i2");

        p9.transform(smallGraphModel, smallGraphModel.getGraphNode("e1i1n2").get());
    }

    @Test(expected = RuntimeException.class)
    public void isP9TransformUnsuccessfulForIncorrectBigGraphTest() {
        GraphModel bigGraphModel = getBigGraphModel();
        TransformationMergeProposal p9 = new P9();

        bigGraphModel.getGraphNodes().forEach(graphNode -> graphNode.removeNeighbourENode(bigGraphModel.getGraphNode("e1i1i1i2").get()));
        bigGraphModel.removeGraphNode("e1i1i1i2");

        p9.transform(bigGraphModel, bigGraphModel.getGraphNode("e1i1n2").get());
    }

    private GraphModel getSmallGraphModel() {
        GraphModel graphModel = generateGraphModel();
        Transformation p1 = new P1();
        Transformation p3 = new P3();

        p1.transform(graphModel, graphModel.getGraphNode("e1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1i1").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1i2").get(), false);

        return graphModel;
    }

    private GraphModel getBigGraphModel() {
        GraphModel graphModel = getSmallGraphModel();
        Transformation p3 = new P3();

        p3.transform(graphModel, graphModel.getGraphNode("e1i1i3").get(), false);
        p3.transform(graphModel, graphModel.getGraphNode("e1i1i4").get(), false);

        return graphModel;
    }
}
