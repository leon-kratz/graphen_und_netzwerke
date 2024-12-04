package graphennetzwerke;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateTimetable {
  public static void main(String[] args) {
    // Check if the path is set
    // if (args.length == 0) {
    //   System.out.println("Please add the folder path!");
    //   System.exit(0);
    // }

    // Get the data from the InstanzX folders
    // String folderPath = args[0];
    String folderPath = "Testinstanzen\\Instanz4";
    int[][][] data = readFiles(folderPath);
    /*
     * for(int i = 0; i < 3; i++) {
     * System.out.println(Arrays.deepToString(data[i]));
     * }
     */

    ArrayList<Node> nodes = getNodes(data);
    /*
     * for(Node node : nodes) {
     * System.out.println(node);
     * }
     */
    ArrayList<Edge> edges = getEdges(nodes);
    /*
     * for(Edge edge : edges) {
     * System.out.println(edge);
     * }
     */
    Graph graph = new Graph(nodes, edges);
    // System.out.println(graph);

    int minColor = graph.minColorBacktracking();
    System.out.println(minColor);
    minColor = graph.minColorJohnson();
    System.out.println(minColor);
    minColor = graph.minColorSequential();
    System.out.println(minColor);

    // checkBipartite();

    // Visualize Graph
    GraphVisualization graphVisualization = new GraphVisualization();
    graphVisualization.visualizeGraph(graph);
  }

  // This method reads the datafiles D.txt, R.txt, S.txt from a folderPath and
  // returns a array containing the data
  public static int[][][] readFiles(String folderPath) {
    String[] fileNames = { "D.txt", "R.txt", "S.txt" };
    int[][][] data = new int[3][][];
    for (int i = 0; i < data.length; i++) {
      Path filePath = Paths.get(folderPath, fileNames[i]);
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
          lines.add(line);
        }
        data[i] = new int[lines.size()][];
        for (int j = 0; j < lines.size(); j++) {
          data[i][j] = Arrays.stream(lines.get(j).split(",")).mapToInt(Integer::parseInt).toArray();
        }
      } catch (IOException e) {
        System.err.println("Error while reading the files: " + e.getMessage());
      }
    }
    return data;
  }

  // This methodes converts the data into the nodes, first for reads the
  // lecturers, second the room, third the semester
  public static ArrayList<Node> getNodes(int[][][] data) {
    ArrayList<Node> nodes = new ArrayList<>();

    for (int i = 0; i < data[0].length; i++) {
      for (int j = 0; j < data[0][i].length; j++) {
        nodes.add(new Node(data[0][i][j], null, i, null, null));
      }
    }

    for (int i = 0; i < data[1].length; i++) {
      for (int j = 0; j < data[1][i].length; j++) {
        int name = data[1][i][j];
        Node node = nodes.stream().filter(n -> n.getName() == name).findFirst().orElse(null);
        node.setRoom(i);
      }
    }

    for (int i = 0; i < data[2].length; i++) {
      for (int j = 0; j < data[2][i].length; j++) {
        int name = data[2][i][j];
        Node node = nodes.stream().filter(n -> n.getName() == name).findFirst().orElse(null);
        node.setSemester(i);
      }
    }

    return nodes;
  }

  // This method creates the edges for the graph
  public static ArrayList<Edge> getEdges(ArrayList<Node> nodes) {
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < nodes.size() - 1; i++) {
      for (int j = i + 1; j < nodes.size(); j++) {
        Node v = nodes.get(i);
        Node u = nodes.get(j);
        if (v.getLecturer() == u.getLecturer() || (v.getRoom() != null && v.getRoom() == u.getRoom())
            || v.getSemester() == u.getSemester()) {
          edges.add(new Edge(v, u));
        }
      }
    }
    return edges;
  }

  // This method checks if the algorithm works with a bipartite graph
  public static void checkBipartite() {
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
