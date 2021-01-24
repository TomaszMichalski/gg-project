package pl.edu.agh.gg.common;

import pl.edu.agh.gg.model.*;

public class Utils {

    public static String getEdgeName(GraphNode first, GraphNode second) {
        return first.getId() + second.getId();
    }

    public static void addEdge(GraphModel g, GraphNode a, GraphNode b) {
        g.insertGraphEdge(getEdgeName(a, b), a, b);
        if (a instanceof ENode) {
            b.addNeighbourENode(a);
        }
        if (b instanceof ENode) {
            a.addNeighbourENode(b);
        }
    }

    public static ENode addE(GraphModel g, String id, double x, double y, double level) {
        return (ENode) g.insertGraphNode(new ENode(g, id, new Coordinates(x, y, level)));
    }

    public static ENode addE(GraphModel g, String id, Coordinates c) {
        return (ENode) g.insertGraphNode(new ENode(g, id, c));
    }

    public static InteriorNode addI(GraphModel g, String id, double x, double y, double level) {
        return (InteriorNode) g.insertGraphNode(new InteriorNode(g, id, new Coordinates(x, y, level)));
    }

    public static InteriorNode addI(GraphModel g, String id, Coordinates c) {
        return (InteriorNode) g.insertGraphNode(new InteriorNode(g, id, c));
    }

    public static Coordinates mid(GraphNode a, GraphNode b) {
        return a.getCoordinates().middlePoint(b.getCoordinates());
    }
}
