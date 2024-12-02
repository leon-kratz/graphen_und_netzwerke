package graphennetzwerke;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
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
            colors.removeAll(node.getNeighbors().stream().map(Node::getColor).collect(Collectors.toSet())); //Entfernt alle Farben, die bereits vom Nachbarn blockiert werden
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
        ArrayList<Node> nodesW = new ArrayList<Node>(nodes); //Kopie von Nodes für äußere Schleife
        HashMap<Node, Integer> updateDegreesW = new HashMap<Node, Integer>(); // Hashmap mit Knotengraden der äußeren Schleife
        for (Node node : nodesW) {
            updateDegreesW.put(node, node.getDegree());
        }
        while (nodesW.size() > 0) {
            setMinColor(getMinColor() + 1);
            ArrayList<Node> nodesU = new ArrayList<Node>(nodesW); // Kopie von Nodes für innere Schleife
            HashMap<Node, Integer> updateDegreesU = new HashMap<Node, Integer>(updateDegreesW); // Hashmap mit Knotengraden der inneren Schleife
            while (nodesU.size() > 0) {
                /*System.out.println("New Iteration:");
                for (Node nodeU : nodesU) {
                    System.out.println(nodeU + "Min-Degree: " + updateDegreesU.get(nodeU));
                }*/
                Node minDegree = null;
                for (Node nodeU : nodesU) { // Hole den Knoten mit dem minimalen Grad
                    if (minDegree == null || updateDegreesU.get(nodeU) < updateDegreesU.get(minDegree)) {
                        minDegree = nodeU;
                    }
                }
                minDegree.setColor(getMinColor());
                ArrayList<Node> neighbors = minDegree.getNeighbors();
                for (Node neighbor : neighbors) { // Degree der Nachbarn in W und U runtersetzen, weil u aus U und W rausfliegt
                    updateDegreesW.put(neighbor, updateDegreesW.get(neighbor) - 1); // In W immer runtersetzen, da u aus W entfernt wird
                    updateDegreesU.put(neighbor, updateDegreesU.get(neighbor) - 1);
                    if (nodesU.contains(neighbor)) { // Nur umsetzen, wenn Nachbar in U noch existiert
                        updateDegreesU.put(neighbor, updateDegreesU.get(neighbor) - 1); 
                        for (Node secondNeighbor : neighbor.getNeighbors()) {
                            if (nodesU.contains(secondNeighbor)) {
                                updateDegreesU.put(secondNeighbor, updateDegreesU.get(secondNeighbor) - 1); // Degree, der Nachbars Nachbarn von u in U runtersetzen, da die Nachbarn von u auch aus U rausfliegen
                            } 
                        }
                    }
                }
                nodesU.remove(minDegree); 
                nodesU.removeAll(neighbors);
                nodesW.remove(minDegree);
            }
        }
        return getMinColor();
    }

    // Hier noch verstehen!
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
