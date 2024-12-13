package graphennetzwerke;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;

public class GraphVisualization {

    // Colors
    String[] colors = { "red", "blue", "green", "yellow", "orange",
            "purple", "pink", "brown", "cyan", "lime" };

    public GraphVisualization() {

    }

    public void visualizeGraph(graphennetzwerke.Graph graphToVisualize) {
        // Graph Builder
        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected()
                .allowingMultipleEdges(true)
                .allowingSelfLoops(true)
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier())
                .buildGraph();

        // Mapping for Nodes and Vertices
        Map<Node, String> nodeMap = new HashMap<>();
        Map<String, Node> vertexMap = new HashMap<>();

        // Create Vertices
        int index = 1;
        for (Node node : graphToVisualize.getNodes()) {
            String v = Integer.toString(index);
            graph.addVertex(v);
            nodeMap.put(node, v);
            vertexMap.put(v, node);
            index++;
        }

        // Create Edges
        for (Edge edge : graphToVisualize.getEdges()) {
            String u = nodeMap.get(edge.getU());
            String v = nodeMap.get(edge.getV());
            graph.addEdge(u, v);
        }

        // JGraphX-Adapter
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

        // Edge style
        for (Object edge : graphAdapter.getEdgeToCellMap().values()) {
            ((mxICell) edge).setValue(""); // Remove label
            graphAdapter.setCellStyle("endArrow=none;strokeColor=black;", new Object[]{edge}); // Set edge color to black
        }

        // Set Node style to Circle with fixed size
        int nodeSize = 40; // Einheitliche Größe für alle Kreise
        Map<String, mxICell> vertexToCellMap = graphAdapter.getVertexToCellMap();
        for (String vertex : graph.vertexSet()) {
            Node node = vertexMap.get(vertex);
            String color = colors[node.getColor()]; // Zyklische Farbzuweisung

            // Set style to circular nodes with fixed size
            graphAdapter.setCellStyle(
                    "shape=ellipse;fillColor=" + color + ";strokeColor=black;perimeter=ellipsePerimeter;" +
                            "fontSize=14;fontStyle=1;fontColor=black;",
                    new mxICell[]{vertexToCellMap.get(vertex)}
            );

            // Set fixed size for the vertex
            mxICell cell = vertexToCellMap.get(vertex);
            graphAdapter.getModel().setGeometry(cell, new com.mxgraph.model.mxGeometry(0, 0, nodeSize, nodeSize));
        }

        // JFrame für die Visualisierung
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Graph-Komponente (Zentrum)
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        frame.add(graphComponent, BorderLayout.CENTER);

        // Infos zu den Nodes als Tabelle (Linke Spalte)
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(400, frame.getHeight()));

        // Tabellenmodell erstellen
        String[] columnNames = {"Node", "Professor", "Raum", "Semester"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Fülle das Tabellenmodell mit den Node-Details
        for (Node node : graphToVisualize.getNodes()) {
            String nodeNumber = "" + node.getName();
            String professor = "" + node.getLecturer();
            String room = node.getRoom() != null ? "" + (100 + node.getRoom()) : "-";
            String semester = "" + (node.getSemester() + 1);

            tableModel.addRow(new Object[]{nodeNumber, professor, room, semester});
        }

        // Erstelle die Tabelle
        JTable nodeTable = new JTable(tableModel);
        nodeTable.setFillsViewportHeight(true);

        // Kopfzeilen-Stil anpassen
        nodeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        nodeTable.getTableHeader().setPreferredSize(new Dimension(nodeTable.getColumnModel().getTotalColumnWidth(), 40));

        // Zellen-Stil anpassen (horizontal zentriert, größere Schrift)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int col = 0; col < nodeTable.getColumnCount(); col++) {
            nodeTable.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
        }

        // Tabelle zum Panel hinzufügen
        JScrollPane tableScrollPane = new JScrollPane(nodeTable);
        infoPanel.add(tableScrollPane, BorderLayout.CENTER);


        // Füge das Panel zum Frame hinzu
        frame.add(infoPanel, BorderLayout.WEST);

        // Layout für den Graph
        mxOrganicLayout layout = new mxOrganicLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // JFrame
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
