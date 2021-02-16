package pl.edu.agh.gg.transform.utils;

import org.javatuples.Pair;
import pl.edu.agh.gg.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class MockGraphs {

    public static List<Pair<Character, Coordinates>> generateMockP2() {

        List<Pair<Character, Coordinates>> mockGraph = new ArrayList<>();

        mockGraph.add(new Pair<>('I', new Coordinates(3.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(5.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('e', new Coordinates(0.0,0.0,1.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-1.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-1.0,2.0)));
        mockGraph.add(new Pair<>('i', new Coordinates(1.6,-3.0,2.0)));

        return mockGraph;
    }

    public static List<Pair<Character, Coordinates>> generateMockP2Horizontal() {

        List<Pair<Character, Coordinates>> mockGraph = new ArrayList<>();

        mockGraph.add(new Pair<>('I', new Coordinates(4.0,-6.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(4.0,-8.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('e', new Coordinates(0.0,0.0,1.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-1.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-1.0,2.0)));
        mockGraph.add(new Pair<>('i', new Coordinates(1.6,-3.0,2.0)));

        return mockGraph;
    }

    public static List<Pair<Character, Coordinates>> generateMockP3() {

        List<Pair<Character, Coordinates>> mockGraph = new ArrayList<>();

        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(3.0,-6.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(5.0,-6.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(3.0,-8.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(5.0,-8.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('e', new Coordinates(0.0,0.0,1.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.4,-1.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-1.0,2.0)));
        mockGraph.add(new Pair<>('i', new Coordinates(1.6,-3.0,2.0)));

        return mockGraph;
    }

    public static List<Pair<Character, Coordinates>> generateMockP3Horizontal() {

        List<Pair<Character, Coordinates>> mockGraph = new ArrayList<>();

        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(3.0,-6.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(3.0,-8.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(5.0,-6.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(6.0,-9.5,3.0)));
        mockGraph.add(new Pair<>('I', new Coordinates(5.0,-8.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0000000000000004,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0000000000000004,-9.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-5.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(4.0,-7.5,3.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(2.0000000000000004,-5.5,3.0)));
        mockGraph.add(new Pair<>('e', new Coordinates(0.0,0.0,1.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.3999999999999999,-5.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(-0.3999999999999999,-1.0,2.0)));
        mockGraph.add(new Pair<>('E', new Coordinates(3.6,-1.0,2.0)));
        mockGraph.add(new Pair<>('i', new Coordinates(1.6,-3.0,2.0)));

        return mockGraph;
    }
}
