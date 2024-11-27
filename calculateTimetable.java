import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class calculateTimetable {
  public static void main(String[] args) {
    
    //Get the data from the InstanzX folders
    String folderPath = args[0]; 
    int[][][] data = readFiles(folderPath);
    /*for(int i = 0; i < 3; i++) {
      System.out.println(Arrays.deepToString(data[i]));
    }*/

    ArrayList<Node> nodes = getNodes(data);
    /*for(Node node : nodes) {
      System.out.println(node);
    }*/ 
    ArrayList<Edge> edges = getEdges(nodes);
    /*for(Edge edge : edges) {
      System.out.println(edge);
    }*/
    Graph graph = new Graph(nodes, edges);
    System.out.println(graph);

  }

  //This method reads the datafiles D.txt, R.txt, S.txt from a folderPath and returns a array containing the data
  public static int[][][] readFiles(String folderPath) {
    String[] fileNames = {"D.txt", "R.txt", "S.txt"};
    int[][][] data = new int[3][][];
    for(int i = 0; i < data.length; i++) {
      try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + "\\" + fileNames[i]))) {
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
          lines.add(line);
        }
        data[i] = new int[lines.size()][];
        for(int j = 0; j < lines.size(); j++) {
          data[i][j] = Arrays.stream(lines.get(j).split(",")).mapToInt(Integer::parseInt).toArray();
        }
      } catch(IOException e) {
        System.err.println("Error while reading the files: " + e.getMessage());
      }
    }
    return data;
  }

  public static ArrayList<Node> getNodes(int[][][] data) {
    //0 - Dozent
    //1 - Raum
    //2 - Semester
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


  public static ArrayList<Edge> getEdges(ArrayList<Node> nodes) {
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < nodes.size()-1; i++) {
      for (int j = i + 1; j < nodes.size(); j++) {
        Node v = nodes.get(i);
        Node u = nodes.get(j);
        if(v.getLecturer() == u.getLecturer() || (v.getRoom() != null && v.getRoom() == u.getRoom()) || v.getSemester() == u.getSemester()) {
          edges.add(new Edge(v, u));
        }
      }
    }
    return edges;
  }
}
