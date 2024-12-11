package graphennetzwerke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Collections;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
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
        return sb.toString();
    }

    private void resetColoring() {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setColor(null);
        }
    }

    /**
    * Requires: einfacher ungerichteter Graph G = (V, E)
    * Durchläuft Knoten und entfernt Farben der Nachbarn aus Farbmenge allColors
    * Bestimmt Färbung der Knoten mithilfe der übrigen Farbmenge
    *
    * @return  Anzahl an benötigten Farben berechnet mit Johnson Algorithmus
    */
    public int minColorSequential() {
        resetColoring();
        ArrayList<Integer> allColors = new ArrayList<Integer>();
        int minColor = 0;
        for (Node node : nodes) {
            ArrayList<Integer> colors = new ArrayList<Integer>(allColors);
            colors.removeAll(node.getNeighbors().stream().map(Node::getColor).collect(Collectors.toSet())); //Entfernt alle Farben, die bereits vom Nachbarn blockiert werden
            if (colors.size() == 0) {
                minColor += 1;
                allColors.add(minColor);
                node.setColor(minColor);
            } else {
                node.setColor(Collections.min(colors));
            }
        }
        return minColor;
    }
    
     /**
     * Requires: einfacher ungerichteter Graph G = (V, E)
     * Durchläuft Knoten und bestimmt Farben mithilfe des Knotengrades der Knotenmenge W und der Knotenmenge U basierend auf W
     * 
     * @return Anzahl an benötigten Farben berechnet mit sequenziellen Algorithmus
     */
    public int minColorJohnson() {
        resetColoring();
        ArrayList<Node> nodesW = new ArrayList<Node>(nodes); //Kopie von Nodes für äußere Schleife
        HashMap<Node, Integer> updateDegreesW = new HashMap<Node, Integer>(); // Hashmap mit Knotengraden der äußeren Schleife
        for (Node node : nodesW) {
            updateDegreesW.put(node, node.getDegree());
        }
        int minColor = 0;
        while (nodesW.size() > 0) {
            minColor += 1;
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
                minDegree.setColor(minColor);
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
        return minColor;
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
    public int minColorBacktracking() {
        int[] coloring = new int[nodes.size()];
        int[] maximum = new int[nodes.size()];
        AtomicInteger limit = new AtomicInteger(nodes.size() - 1);

        Arrays.fill(coloring, 0);
        Arrays.fill(maximum, limit.get());
        coloring[0] = 0;
        maximum[0] = 0;

        partition(0, coloring, maximum, limit);
        return limit.get() + 1;
    }

    private void partition(int k, int[] coloring, int[] maximum, AtomicInteger limit) {
        int n = nodes.size();
        if (increaseSubsolution(k, coloring)) {
            do {
                if (correctSubsolution(k + 1, coloring)) {
                    maximum[k + 1] = Math.max(coloring[k + 1], maximum[k]);
                    if (k + 1 == n - 1) {
                        // kleinste Lösung gefunden, limit setzen und knoten färben
                        limit.set(maximum[k + 1]);
                        for (int i = 0; i < nodes.size(); i++) {
                            nodes.get(i).setColor(coloring[i]);
                        }
                    } else {
                        partition(k + 1, coloring, maximum, limit);
                    }
                }
            } while (varySubsolution(k + 1, coloring, maximum));
        }
    }

    // true, wenn der Knoten in Rekursion geprüft werden kann (nicht der letzte Knoten)
    private boolean increaseSubsolution(int k, int[] coloring) {
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
    private boolean correctSubsolution(int k, int[] coloring) {
        for (int j = 0; j < k; j++) {
            Node u = nodes.get(j);
            Node v = nodes.get(k);

            if (coloring[j] == coloring[k] && u.hasNeighbor(v)) {
                return false;
            }
        }

        return true;
    }

    private boolean varySubsolution(int k, int[] coloring, int[] maximum) {
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
