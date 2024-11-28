import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private Integer minColor;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.minColor = null;
    }

    public Graph(List<Node> nodes, List<Edge> edges) {
      this.nodes = nodes;
      this.edges = edges;
      this.minColor = null;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Integer getMinColor() {
        return minColor;
    }

    public void setMinColor(Integer minColor) {
        this.minColor = minColor;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph:\n");
        sb.append("Nodes:\n");
        for (Node node : nodes) {
            sb.append(node).append("\n");
        }
        sb.append("Edges:\n");
        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }
        sb.append("MinColor: ").append(minColor).append("\n");
        return sb.toString();
    }

    private void resetColoring() {
        this.setMinColor(0);
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setColor(null);
        }
    }

    public int minColorSequential() {
        resetColoring();
        ArrayList<Integer> allColors = new ArrayList<Integer>();
        for (Node node : nodes) {
            ArrayList<Integer> colors = new ArrayList<Integer>(allColors);
            colors.removeAll(getNeighbors(node).stream().map(Node::getColor).collect(Collectors.toSet()));
            if (colors.size() == 0) {
                setMinColor(getMinColor() + 1);
                allColors.add(getMinColor());
                node.setColor(getMinColor());
            } else {
                node.setColor(Collections.min(colors));
            }
        }
        return getMinColor();
    }

    public int minColorJohnson() {
        resetColoring();
        ArrayList<Node> nodesW = new ArrayList<Node>(nodes);
        while (nodesW.size() > 0) {
            setMinColor(getMinColor() + 1);;
            ArrayList<Node> nodesU = new ArrayList<Node>(nodesW);
            while (nodesU.size() > 0) {
               Node minDegree = getMinDegree(nodesU); 
               minDegree.setColor(getMinColor());
               nodesU.remove(minDegree);
               nodesU.removeAll(getNeighbors(minDegree));
               nodesW.remove(minDegree);
            }
        } 
        return getMinColor();
    }

    private ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (Edge edge : this.edges) {
            if (node.equals(edge.getV())) {
                neighbors.add(edge.getU());
            } else if (node.equals(edge.getU())) {
                neighbors.add(edge.getV());
            }
        }
        return neighbors;
    }

    private Node getMinDegree(ArrayList<Node> nodes) {
        Node minDegree = null;
        for(Node node : nodes) {
           if(minDegree == null || node.getDegree() < minDegree.getDegree()) {
                minDegree = node;
           } 
        } 
        return minDegree;
    }


    //Hier noch verstehen!
    public int minColorBacktracking() {
        int numNodes = nodes.size();
        int[] colors = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            colors[i] = -1; // -1 means uncolored
        }

        // Start coloring with a maximum possible number of colors
        int maxColors = numNodes; // Worst case
        if (colorGraph(0, colors, maxColors)) {
            minColor = findMaxColor(colors) + 1;
        }
        setMinColor(minColor);
        return minColor;
    }

    private boolean colorGraph(int nodeIndex, int[] colors, int maxColors) {
        if (nodeIndex == nodes.size()) {
            // All nodes have been assigned a color
            return true;
        }

        // Try different colors for the node
        for (int color = 0; color < maxColors; color++) {
            if (isSafeToColor(nodeIndex, color, colors)) {
                colors[nodeIndex] = color; // Assign color
                if (colorGraph(nodeIndex + 1, colors, maxColors)) {
                    return true;
                }
                // Backtrack
                colors[nodeIndex] = -1;
            }
        }

        return false; // No solution found
    }

    private boolean isSafeToColor(int nodeIndex, int color, int[] colors) {
        Node currentNode = nodes.get(nodeIndex);

        for (Edge edge : getEdges()) {
            // Check if the current node is part of the edge
            if (edge.getV().equals(currentNode)) {
                // Get the index of the neighbor (connected via 'u')
                int neighborIndex = nodes.indexOf(edge.getU());
                // If the neighbor is already colored with the same color, return false
                if (neighborIndex != -1 && colors[neighborIndex] == color) {
                    return false;
                }
            } else if (edge.getU().equals(currentNode)) {
                // Get the index of the neighbor (connected via 'v')
                int neighborIndex = nodes.indexOf(edge.getV());
                // If the neighbor is already colored with the same color, return false
                if (neighborIndex != -1 && colors[neighborIndex] == color) {
                    return false;
                }
            }
        }

        return true; // No conflicts with adjacent nodes
    }

    private int findMaxColor(int[] colors) {
        int maxColor = 0;
        for (int color : colors) {
            if (color > maxColor) {
                maxColor = color;
            }
        }
        return maxColor;
    }
}
    

