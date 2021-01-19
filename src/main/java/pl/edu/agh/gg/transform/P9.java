package pl.edu.agh.gg.transform;

import org.graphstream.graph.Node;
import org.javatuples.Pair;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.model.InteriorNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class P9 implements TransformationMergeProposal {

    @Override
    public boolean isApplicable(GraphModel graph, GraphNode eNode) {
        if (!(eNode instanceof ENode)) return false;
        if (!hasTwoAdjacentInteriorNodes((ENode) eNode)) return false;

        List<InteriorNode> adjacentInteriorNodes = getAdjacentInteriorNodes((ENode) eNode);
        if (!adjacentInteriorNodes.stream().allMatch(this::hasAtLeastTwoChildInteriorNodes)) return false;

        List<InteriorNode> childInteriorNodes1 = getChildInteriorNodes(adjacentInteriorNodes.get(0));
        List<ENode> childInteriorNodesAdjacentENodes1 = getChildInteriorNodesAdjacentENodes(childInteriorNodes1);

        List<InteriorNode> childInteriorNodes2 = getChildInteriorNodes(adjacentInteriorNodes.get(1));
        List<ENode> childInteriorNodesAdjacentENodes2 = getChildInteriorNodesAdjacentENodes(childInteriorNodes2);

        List<ENode> commonENodes1 = getCommonENodes(childInteriorNodesAdjacentENodes2, childInteriorNodesAdjacentENodes1);
        List<ENode> commonENodes2 = getCommonENodes(childInteriorNodesAdjacentENodes1, childInteriorNodesAdjacentENodes2);

        List<InteriorNode> commonAdjacentInteriorNodes1 = getCommonAdjacentInteriorNodes(commonENodes1);
        List<InteriorNode> commonAdjacentInteriorNodes2 = getCommonAdjacentInteriorNodes(commonENodes2);

        return commonENodes1.size() == 3 && commonENodes2.size() == 3 && commonAdjacentInteriorNodes1.size() >= 2 && commonAdjacentInteriorNodes2.size() >= 2;
    }

    @Override
    public void transform(GraphModel graph, GraphNode eNode) {
        List<InteriorNode> adjacentInteriorNodes = getAdjacentInteriorNodes((ENode) eNode);

        List<InteriorNode> childInteriorNodes1 = getChildInteriorNodes(adjacentInteriorNodes.get(0));
        List<ENode> childInteriorNodesAdjacentENodes1 = getChildInteriorNodesAdjacentENodes(childInteriorNodes1);

        List<InteriorNode> childInteriorNodes2 = getChildInteriorNodes(adjacentInteriorNodes.get(1));
        List<ENode> childInteriorNodesAdjacentENodes2 = getChildInteriorNodesAdjacentENodes(childInteriorNodes2);

        List<ENode> commonENodes1 = getCommonENodes(childInteriorNodesAdjacentENodes2, childInteriorNodesAdjacentENodes1);
        List<ENode> commonENodes2 = getCommonENodes(childInteriorNodesAdjacentENodes1, childInteriorNodesAdjacentENodes2);

        replaceCommonENodes(graph, commonENodes1, commonENodes2);
    }

    private boolean hasTwoAdjacentInteriorNodes(ENode eNode) {
        return getAdjacentInteriorNodes(eNode).size() == 2;
    }

    private List<InteriorNode> getAdjacentInteriorNodes(ENode eNode) {
        return Arrays.stream(eNode.getAdjacentENodes())
                .filter(edges -> edges instanceof InteriorNode && edges.getLevel() == eNode.getLevel())
                .map(edges -> (InteriorNode) edges)
                .collect(Collectors.toList());
    }

    private boolean hasAtLeastTwoChildInteriorNodes(InteriorNode interiorNode) {
        return getChildInteriorNodes(interiorNode).size() >= 2;
    }

    private List<InteriorNode> getChildInteriorNodes(InteriorNode interiorNode) {
        Iterator<Node> neighborNodeIterator = interiorNode.getNeighborNodeIterator();
        List<InteriorNode> childInteriorNodes = new ArrayList<>();

        while (neighborNodeIterator.hasNext()) {
            Node neighbourNode = neighborNodeIterator.next();

            if (neighbourNode instanceof InteriorNode) {
                InteriorNode neighbourInteriorNode = (InteriorNode) neighbourNode;
                if (neighbourInteriorNode.getLevel() == interiorNode.getLevel() + 1.0) {
                    childInteriorNodes.add(neighbourInteriorNode);
                }
            }
        }

        return childInteriorNodes;
    }

    private List<ENode> getChildInteriorNodesAdjacentENodes(List<InteriorNode> interiorNodes) {
        List<ENode> childInteriorNodesAdjacentENodes = new ArrayList<>();

        for (InteriorNode interiorNode : interiorNodes) {
            List<ENode> adjacentENodes = getAdjacentENodes(interiorNode);

            for (ENode eNode : adjacentENodes) {
                if (!childInteriorNodesAdjacentENodes.contains(eNode)) {
                    childInteriorNodesAdjacentENodes.add(eNode);
                }
            }
        }

        return childInteriorNodesAdjacentENodes;
    }

    private List<ENode> getAdjacentENodes(InteriorNode interiorNode) {
        return Arrays.stream(interiorNode.getAdjacentENodes())
                .filter(edges -> edges instanceof ENode && edges.getLevel() == interiorNode.getLevel())
                .map(edges -> (ENode) edges)
                .collect(Collectors.toList());
    }

    private List<ENode> getCommonENodes(List<ENode> eNodesToCompare, List<ENode> eNodesToReturn) {
        List<ENode> commonNodes = new ArrayList<>();

        for (ENode eNode : eNodesToCompare) {
            List<ENode> common = eNodesToReturn.stream()
                    .filter(eNodeToReturn -> eNodeToReturn.equals(eNode))
                    .filter(eNodeToReturn -> !eNodeToReturn.getId().equals(eNode.getId()))
                    .collect(Collectors.toList());

            for (ENode commonENode : common) {
                if (!commonNodes.contains(commonENode)) {
                    commonNodes.add(commonENode);
                }
            }
        }

        return commonNodes;
    }

    private List<InteriorNode> getCommonAdjacentInteriorNodes(List<ENode> eNodes) {
        List<InteriorNode> commonAdjacentInteriorNodes = new ArrayList<>();

        for (ENode eNode : eNodes) {
            List<InteriorNode> adjacentInteriorNodes = getAdjacentInteriorNodes(eNode);

            for (InteriorNode interiorNode : adjacentInteriorNodes) {
                if (!commonAdjacentInteriorNodes.contains(interiorNode)) {
                    commonAdjacentInteriorNodes.add(interiorNode);
                }
            }
        }

        return commonAdjacentInteriorNodes;
    }

    private void replaceCommonENodes(GraphModel graph, List<ENode> commonENodes1, List<ENode> commonENodes2) {
        List<ENode> eNodesToReplace = new ArrayList<>();
        List<Pair<ENode, ENode>> replacementPairs = new ArrayList<>();

        for (ENode eNode1 : commonENodes1) {
            for (ENode eNode2 : commonENodes2) {
                if (eNode1.equals(eNode2)) {
                    if (eNode1.getCoordinates().getOriginalX() != 0 && eNode1.getCoordinates().getOriginalY() != 0) {
                        eNodesToReplace.add(eNode1);
                        replacementPairs.add(Pair.with(eNode1, eNode2));
                    } else {
                        eNodesToReplace.add(eNode2);
                        replacementPairs.add(Pair.with(eNode2, eNode1));
                    }
                }
            }
        }

        for (Pair<ENode, ENode> replacementPair : replacementPairs) {
            replacementPair.getValue0().replaceWith(graph, replacementPair.getValue1(), eNodesToReplace);
        }
    }
}
