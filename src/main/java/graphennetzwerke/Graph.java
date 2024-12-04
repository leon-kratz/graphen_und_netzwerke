package graphennetzwerke;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int[][] getAdjacencyMatrix() {
        int[][] matrix = new int[nodes.size()][nodes.size()];
        
        for (int i = 0; i < nodes.size(); i++) {
            Node u = nodes.get(i);
            for (int j = 0; j < nodes.size(); j++) {
                Node v = nodes.get(j);

                if (u.hasNeighbor(v)) {
                    matrix[i][j] = 1;
                }
            }
        }

        return matrix;
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

    /**
     * Requires: einfacher ungerichteter Graph G = (V, E)
     * Knoten haben die Werte von 0 bis n-1.
     * coloring speichert die aktuelle Färbung der Teillösung
     * maximum speichert die maximale Anzahl an zu verwendenden Teilmengen.
     * limit beschreibt die kleinste gefundene Lösung
     * 
     * @return The number of colors needed.
     */
    private int[] coloring;
    private int[] maximum;
    private int limit;

    public int backtracking() {
        coloring = new int[nodes.size()];
        maximum = new int[nodes.size()];
        limit = nodes.size() - 1;

        Arrays.fill(coloring, 0);
        Arrays.fill(maximum, limit);
        coloring[0] = 0;
        maximum[0] = 0;

        partition(0);
        this.minColor = limit + 1;
        return this.minColor;
    }

    private void partition(int k) {
        int n = nodes.size();
        if (increaseSubsolution(k)) {
            do {
                if (correctSubsolution(k + 1)) {
                    maximum[k + 1] = Math.max(coloring[k + 1], maximum[k]);
                    if (k + 1 == n - 1) {
                        // kleinste Lösung gefunden, limit setzen und knoten färben
                        limit = maximum[k + 1];
                        for (int i = 0; i < nodes.size(); i++) {
                            nodes.get(i).setColor(coloring[i]);
                        }
                    } else {
                        partition(k + 1);
                    }
                }
            } while (varySubsolution(k + 1));
        }
    }

    // true, wenn der Knoten in Rekursion geprüft werden kann (nicht der letzte Knoten)
    private boolean increaseSubsolution(int k) {
        int n = nodes.size();
        // letzter Knoten kann nicht betrachtet werden, weil keine größeren mehr existent.
        if (k == n - 1) {
            return false;
        }

        // zurücksetzen der Färbung des nächsten Knotens.
        coloring[k + 1] = 0;
        return true;
    }

    /**
     * Determines if current subsolution is valid.
     * A subsolution is valid, if all smaller neighbors of current vertex does not have the same color as the vertex. 
     * 
     * @param k Vertex to check 
     * @return {@code true} if the current subsolution is valid, {@code false} otherwise
     */
    private boolean correctSubsolution(int k) {
        for (int j = 0; j < k; j++) {
            Node u = nodes.get(j);
            Node v = nodes.get(k);

            if (coloring[j] == coloring[k] && u.hasNeighbor(v)) {
                return false;
            }
        }

        return true;
    }

    private boolean varySubsolution(int k) {
        // aktuelle Farbe muss kleiner sein als 
        //   max vom vorgänger + 1 (alle Knoten können mit i Farben gefärbt werden)
        //   ODER eigenes maximum
        // ansonsten nicht weiter variieren
        if (coloring[k] >= Math.min(maximum[k - 1] + 1, maximum[k])) {
            return false;
        }

        // nächste Farbe testen
        coloring[k]++;
        return true;
    }
}
