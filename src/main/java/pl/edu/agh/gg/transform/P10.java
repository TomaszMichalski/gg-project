package pl.edu.agh.gg.transform;

import org.graphstream.graph.Node;
import org.javatuples.Pair;
import pl.edu.agh.gg.model.ENode;
import pl.edu.agh.gg.model.GraphModel;
import pl.edu.agh.gg.model.GraphNode;
import pl.edu.agh.gg.model.InteriorNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P10 implements TransformationMergeProposal {
  @Override
  public boolean isApplicable(GraphModel graph, GraphNode eNode) {
    if (!(eNode instanceof ENode))
      return false;

    if (getAdjacentInteriorNodes((ENode) eNode).size() < 2)
      return false;

    var extNode = (ENode) eNode;
    var adjacentInteriorNodes = getAdjacentInteriorNodes(extNode);

    var adjacentInteriorNodesPairs = createCartesianProduct(adjacentInteriorNodes);

    return findIfApplicable(adjacentInteriorNodesPairs).isPresent();
  }

  @Override
  public void transform(GraphModel graph, GraphNode eNode) {
    if (!isApplicable(graph, eNode)) {
      throw new IllegalStateException("Transformation not applicable");
    }

    var adjacentInteriorNodes = getAdjacentInteriorNodes((ENode) eNode);

    var adjacentInteriorNodesPairs = createCartesianProduct(adjacentInteriorNodes);

    var foundTriplets = findIfApplicable(adjacentInteriorNodesPairs).get();

    var leftTriplet = foundTriplets.get(0);
    var rightTriplet = foundTriplets.get(1);

    replaceCommonENodes(graph, leftTriplet, rightTriplet);
  }

  private Optional<List<List<ENode>>> findIfApplicable(List<List<InteriorNode>> adjacentInteriorNodesPairs) {
    for (var adjacentInteriorNodePair : adjacentInteriorNodesPairs) {
      if (!adjacentInteriorNodePair.stream().allMatch(node -> getChildInteriorNodes(node).size() >= 2))
        continue;

      var childInteriorNodes1Pairs = createCartesianProduct(getChildInteriorNodes(adjacentInteriorNodePair.get(0)));
      var childInteriorNodes2Pairs = createCartesianProduct(getChildInteriorNodes(adjacentInteriorNodePair.get(1)));

      for (var childInteriorNodes1Pair : childInteriorNodes1Pairs) {

        for (var childInteriorNodes2Pair : childInteriorNodes2Pairs) {
          var eNode1CommonENodeTriplets = getENodeTripletsForInteriorPair(childInteriorNodes1Pair);
          var eNode2CommonENodeTriplets = getENodeTripletsForInteriorPair(childInteriorNodes2Pair);

          for (var eNode1CommonENodeTriplet : eNode1CommonENodeTriplets) {

            if (verifyCoordinates(eNode1CommonENodeTriplet)) {
              for (var eNode2CommonENodeTriplet : eNode2CommonENodeTriplets) {
                if (verifyCoordinates(eNode2CommonENodeTriplet)) {
                  if (verifyBothCoordinatesForTriplet(eNode1CommonENodeTriplet, eNode2CommonENodeTriplet) && haveCommonENodeAndDifferentTwoOther(eNode1CommonENodeTriplet, eNode2CommonENodeTriplet)) {
                    return Optional.of(Arrays.asList(eNode1CommonENodeTriplet, eNode2CommonENodeTriplet));
                  }
                }
              }
            }
          }
        }
      }
    }

    return Optional.empty();
  }

  private List<InteriorNode> getAdjacentInteriorNodes(ENode eNode) {
    return Arrays.stream(eNode.getAdjacentENodes())
        .filter(edges -> edges instanceof InteriorNode && edges.getLevel() == eNode.getLevel())
        .map(edges -> (InteriorNode) edges)
        .collect(Collectors.toList());
  }

  private List<ENode> getAdjacentENodes(InteriorNode interiorNode) {
    return interiorNode.getAdjacentENodesList()
        .stream()
        .filter(edges -> edges instanceof ENode && edges.getLevel() == interiorNode.getLevel())
        .map(edges -> (ENode) edges)
        .collect(Collectors.toList());
  }

  private List<InteriorNode> getChildInteriorNodes(InteriorNode interiorNode) {
    Iterator<Node> neighborNodeIterator = interiorNode.getNeighborNodeIterator();

    return Stream.generate(() -> null)
        .takeWhile(x -> neighborNodeIterator.hasNext())
        .map(n -> neighborNodeIterator.next())
        .filter(node -> node instanceof InteriorNode)
        .map(node -> (InteriorNode) node)
        .filter(node -> Double.compare(node.getLevel(), interiorNode.getLevel() + 1.0) == 0)
        .distinct()
        .collect(Collectors.toList());
  }

  private List<List<ENode>> getENodeTripletsForInteriorPair(List<InteriorNode> interiorNodes) {
    var res = new ArrayList<List<ENode>>();

    var node1AdjacentENodes = getAdjacentENodes(interiorNodes.get(0));
    var node2AdjacentENodes = getAdjacentENodes(interiorNodes.get(1));

    var intersectedAdjacentENodes = new HashSet<>(node1AdjacentENodes);
    intersectedAdjacentENodes.retainAll(node2AdjacentENodes);

    var newNode1AdjacentENodes = node1AdjacentENodes.stream()
        .filter(eNode -> !intersectedAdjacentENodes.contains(eNode) && !node2AdjacentENodes.contains(eNode))
        .filter(eNode -> intersectedAdjacentENodes.stream().anyMatch(intersectedNode -> intersectedNode.getAdjacentENodesList().contains(eNode)))
        .collect(Collectors.toList());
    var newNode2AdjacentENodes = node2AdjacentENodes.stream()
        .filter(eNode -> !intersectedAdjacentENodes.contains(eNode) && !node1AdjacentENodes.contains(eNode))
        .filter(eNode -> intersectedAdjacentENodes.stream().anyMatch(intersectedNode -> intersectedNode.getAdjacentENodesList().contains(eNode)))
        .collect(Collectors.toList());

    for (var node1AdjacentENode : newNode1AdjacentENodes) {
      for (var node2AdjacentENode : newNode2AdjacentENodes) {
        for (var intersectedAdjacentENode : intersectedAdjacentENodes) {
          if (node1AdjacentENode.getAdjacentENodesList().contains(intersectedAdjacentENode) && node2AdjacentENode.getAdjacentENodesList().contains(intersectedAdjacentENode))
            res.add(Arrays.asList(node1AdjacentENode, node2AdjacentENode, intersectedAdjacentENode));
        }
      }
    }

    return res;
  }

  private boolean verifyCoordinates(List<ENode> commonENodes) {
    commonENodes.sort((eNode, t1) -> {
      var xComparison = Double.compare(eNode.getXOrOriginalXCoordinate(), t1.getXOrOriginalXCoordinate());

      if (xComparison != 0) {
        return xComparison;
      } else return Double.compare(eNode.getYOrOriginalYCoordinate(), t1.getYOrOriginalYCoordinate());
    });

    return (Math.abs((commonENodes.get(0).getXOrOriginalXCoordinate() + commonENodes.get(2).getXOrOriginalXCoordinate()) / 2 - commonENodes.get(1).getXOrOriginalXCoordinate()) < 0.2)
        && (Math.abs((commonENodes.get(0).getYOrOriginalYCoordinate() + commonENodes.get(2).getYOrOriginalYCoordinate()) / 2 - commonENodes.get(1).getYOrOriginalYCoordinate()) < 0.2);
  }

  private boolean verifyBothCoordinatesForPair(ENode leftNode, ENode rightNode) {
    var leftNodeCoords = leftNode.getCoordinates();
    var rightNodeCoords = rightNode.getCoordinates();

    var areXEqual = Math.abs(leftNodeCoords.getX() - rightNodeCoords.getX()) < 0.2;
    var areYEqual = Math.abs(leftNodeCoords.getY() - rightNodeCoords.getY()) < 0.2;

    return areXEqual && areYEqual;
  }

  private boolean verifyBothCoordinatesForTriplet(List<ENode> leftTriplet, List<ENode> rightTriplet) {
    var condition = false;
    for (int i = 0; i < leftTriplet.size(); i++) {
      condition = verifyBothCoordinatesForPair(leftTriplet.get(i), rightTriplet.get(i));

      if (!condition)
        break;
    }
    return condition;
  }

  private boolean haveCommonENodeAndDifferentTwoOther(List<ENode> leftENodes, List<ENode> rightENodes) {
    if (leftENodes.get(0) == rightENodes.get(0) || leftENodes.get(2) == rightENodes.get(2)) {
      if (leftENodes.get(2) == rightENodes.get(2)) {
        Collections.reverse(leftENodes);
        Collections.reverse(rightENodes);
      }
      for (int i = 1; i < leftENodes.size(); i++) {
        if (leftENodes.get(i) == rightENodes.get((i)))
          return false;
      }
      return true;
    } else return false;
  }

  private void replaceCommonENodes(GraphModel graph, List<ENode> commonENodes1, List<ENode> commonENodes2) {
    List<ENode> eNodesToReplace = new ArrayList<>();
    List<Pair<ENode, ENode>> replacementPairs = new ArrayList<>();

    commonENodes1.stream().skip(1L).forEach(eNode1 -> {
      commonENodes2.stream().skip(1L)
          .filter(eNode2 -> verifyBothCoordinatesForPair(eNode1, eNode2))
          .forEach(eNode2 -> {
            if (eNode1.getCoordinates().getOriginalX() != 0 && eNode1.getCoordinates().getOriginalY() != 0) {
              eNodesToReplace.add(eNode1);
              replacementPairs.add(Pair.with(eNode1, eNode2));
            } else {
              eNodesToReplace.add(eNode2);
              replacementPairs.add(Pair.with(eNode2, eNode1));
            }
          });
    });


    replacementPairs.forEach(replacementPair -> {
      replacementPair.getValue0().replaceWith(graph, replacementPair.getValue1(), eNodesToReplace);
    });
  }

  private <T> List<List<T>> createCartesianProduct(List<T> entities) {
    var returnList = new ArrayList<List<T>>();
    var length = entities.size();

    for (int i = 0; i < length - 1; i++) {
      for (int j = i + 1; j < length; j++) {
        returnList.add(Arrays.asList(entities.get(i), entities.get(j)));
      }
    }

    return returnList;
  }
}
