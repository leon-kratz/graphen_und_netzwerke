package graphennetzwerke;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphFactory {
    /**
     * Reads the files D.txt, R.txt, S.txt within the given folder and generates the
     * graph based on this files.
     * 
     * @param folderPath The folder where D.txt, R.txt, and S.txt is located.
     * @return The created {@link Graph} instance.
     */
    public static Graph createGraph(Path folderPath) {
        try {
            int[][][] data = readFiles(folderPath.toString());
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
            return graph;
        } catch (IOException e) {
            System.err.printf("Error while trying to read from '%s'! %s\n", folderPath, e.getMessage());
            return null;
        }
    }

    /**
     * Reads the files D.txt, R.txt, and S.txt from the given folder.
     * 
     * @param folderPath The path where D.txt, R.txt, and S.txt is located.
     * @return A three dimensional {@link int} array which contains the parsed
     *         content of all three files.
     * @throws IOException If one of the files cannot be read.
     */
    private static int[][][] readFiles(String folderPath) throws IOException {
        String[] fileNames = { "D.txt", "R.txt", "S.txt" };
        int[][][] data = new int[3][][];
        for (int i = 0; i < data.length; i++) {
            Path filePath = Paths.get(folderPath, fileNames[i]);

            try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                ArrayList<String> lines = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                data[i] = new int[lines.size()][];
                for (int j = 0; j < lines.size(); j++) {
                    data[i][j] = Arrays.stream(lines.get(j).split(",")).mapToInt(Integer::parseInt).toArray();
                }
            }
        }
        return data;
    }

    /**
     * Converts the given data into nodes.
     * First element has to be the lecturers, second the rooms, and third element
     * the semesters.
     * 
     * @param data The data to be converted.
     * @return A {@link ArrayList} containing the converted {@link Node} elements.
     */
    private static ArrayList<Node> getNodes(int[][][] data) {
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

    /**
     * Creates the edges based on the given nodes.
     * Every node will be processed with every other node.
     * An edge will be created, if the two nodes have the same lecturer, the same
     * room, or the same semester.
     * 
     * @param nodes The nodes within the graph.
     * @return A {@link ArrayList} containing the generated {@link Edge} elements.
     */
    private static ArrayList<Edge> getEdges(ArrayList<Node> nodes) {
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
}
