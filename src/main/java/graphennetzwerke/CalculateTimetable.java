package graphennetzwerke;

import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;

public class CalculateTimetable {
    public static void main(String[] args) {
        Path folderPath = null;

        // get folder path from program arguments
        if (args.length > 0) {
            try {
                folderPath = Paths.get(args[0]);
            } catch (InvalidPathException e) {
                System.err.printf("The given path '%s' is not a valid path!\n", args[0]);
                System.exit(1);
            }
        }

        if (folderPath == null) {
            // go through all test instances stored in the current folder
            for (int i = 1; i <= 5; i++) {
                folderPath = Paths.get("Testinstanzen", "Instanz" + i);
                processTestInstance(folderPath, false);
                if (i < 5) {
                    System.out.println();
                }
            }
        } else {
            processTestInstance(folderPath, true);
        }

        // checkBipartite();
    }

    /**
     * Reads the files D.txt, R.txt, S.txt within the given folder and generates the
     * graph based on this files.
     * Prints the minimum color for the implemented coloring algorithms.
     * If visualize is {@code true}, the graph will be visualized as graph and
     * timetable.
     * 
     * @param folderPath The folder where D.txt, R.txt, and S.txt is located.
     * @param visualize  Indicates if the generated graph should be visualized.
     */
    private static void processTestInstance(Path folderPath, boolean visualize) {
        System.out.printf("Using folder '%s'\n", folderPath.toAbsolutePath());
        Graph graph = GraphFactory.createGraph(folderPath);
        if (graph == null) {
            return;
        }
        // System.out.println(graph);

        int minColor = graph.minColorBacktracking();
        System.out.println("backtracking: " + minColor);
        minColor = graph.minColorJohnson();
        System.out.println("johnson: " + minColor);
        minColor = graph.minColorSequential();
        System.out.println("squential: " + minColor);

        System.out.println("backtracking2: " + graph.backtracking_pseudo());

        if (visualize == false) {
            return;
        }

        // Visualize Graph
        GraphVisualization graphVisualization = new GraphVisualization();
        graphVisualization.visualizeGraph(graph);

        // Visualize Timetable
        TimetableVisualization timetableVisualization = new TimetableVisualization();
        timetableVisualization.generateTimetable(graph);
    }

    /**
     * Checks if the algorithms work with a bipartite graph.
     */
    private static void checkBipartite() {
        ArrayList<Node> biNodes = new ArrayList<Node>();
        for (int i = 0; i < 8; i++) {
            biNodes.add(new Node(i, null, null, null, null));
        }
        ArrayList<Edge> biEdges = new ArrayList<Edge>();
        biEdges.add(new Edge(biNodes.get(0), biNodes.get(3)));
        biEdges.add(new Edge(biNodes.get(0), biNodes.get(5)));
        biEdges.add(new Edge(biNodes.get(0), biNodes.get(7)));
        biEdges.add(new Edge(biNodes.get(1), biNodes.get(2)));
        biEdges.add(new Edge(biNodes.get(1), biNodes.get(4)));
        biEdges.add(new Edge(biNodes.get(1), biNodes.get(6)));
        biEdges.add(new Edge(biNodes.get(2), biNodes.get(5)));
        biEdges.add(new Edge(biNodes.get(2), biNodes.get(7)));
        biEdges.add(new Edge(biNodes.get(3), biNodes.get(4)));
        biEdges.add(new Edge(biNodes.get(3), biNodes.get(6)));
        biEdges.add(new Edge(biNodes.get(4), biNodes.get(7)));
        biEdges.add(new Edge(biNodes.get(5), biNodes.get(6)));
        Graph biGraph = new Graph(biNodes, biEdges);
        System.out.println(biGraph.minColorSequential());
        System.out.println(biGraph.minColorJohnson());
        System.out.println(biGraph.minColorBacktracking());
    }
}
