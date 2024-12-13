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

    // Define colors for the nodes. Each color corresponds to a specific "group" or node property.
    String[] colors = {
        "red", "green", "yellow", "orange", 
        "purple", "pink", "brown", "cyan", "lime", 
        "magenta", "teal", "gold", "silver", "maroon", 
        "navy", "olive", "aqua", "coral", "crimson", 
        "darkgreen", "darkblue", "violet", "indigo", "plum",
        "orchid", "turquoise", "khaki", "chocolate", "salmon"
    };
    
    public GraphVisualization() {
    }

    public void visualizeGraph(graphennetzwerke.Graph graphToVisualize) {
        // Create the JGraphT graph
        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected() // Undirected graph
                .allowingMultipleEdges(true) // Allow multiple edges between nodes
                .allowingSelfLoops(true) // Allow self-loops on nodes
                .vertexSupplier(SupplierUtil.createStringSupplier()) // Generate vertex names automatically
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier()) // Default edges
                .buildGraph();

        // Mapping for connecting nodes and vertices
        Map<Node, String> nodeMap = new HashMap<>();
        Map<String, Node> vertexMap = new HashMap<>();

        // Add vertices to the graph
        int index = 1;
        for (Node node : graphToVisualize.getNodes()) {
            String v = Integer.toString(index);
            graph.addVertex(v);
            nodeMap.put(node, v); // Map Node objects to their corresponding vertex
            vertexMap.put(v, node); // Map vertex identifiers back to Node objects
            index++;
        }

        // Add edges to the graph
        for (Edge edge : graphToVisualize.getEdges()) {
            String u = nodeMap.get(edge.getU());
            String v = nodeMap.get(edge.getV());
            graph.addEdge(u, v);
        }

        // Use JGraphX adapter to visualize the graph
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

        // Configure edge styles (e.g., remove arrowheads and set stroke color to black)
        for (Object edge : graphAdapter.getEdgeToCellMap().values()) {
            ((mxICell) edge).setValue(""); // Remove labels from edges
            graphAdapter.setCellStyle("endArrow=none;strokeColor=black;", new Object[]{edge});
        }

        // Configure node styles (circular nodes with a fixed size)
        int nodeSize = 40; // Fixed size for all nodes
        Map<String, mxICell> vertexToCellMap = graphAdapter.getVertexToCellMap();
        for (String vertex : graph.vertexSet()) {
            Node node = vertexMap.get(vertex);
            String color = colors[node.getColor()]; // Assign colors based on the node's color property

            // Set the style for circular nodes
            graphAdapter.setCellStyle(
                    "shape=ellipse;fillColor=" + color + ";strokeColor=black;perimeter=ellipsePerimeter;" +
                            "fontSize=14;fontStyle=1;fontColor=black;",
                    new mxICell[]{vertexToCellMap.get(vertex)}
            );

            // Set a fixed geometry (size and position) for each vertex
            mxICell cell = vertexToCellMap.get(vertex);
            graphAdapter.getModel().setGeometry(cell, new com.mxgraph.model.mxGeometry(0, 0, nodeSize, nodeSize));
        }

        // Create JFrame to display the visualization
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Use BorderLayout to separate graph and node details

        // Add the graph visualization component to the center
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        frame.add(graphComponent, BorderLayout.CENTER);

        // Create a panel for displaying node details (left side)
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(400, frame.getHeight()));

        // Create a table model for the node details
        String[] columnNames = {"Node", "Professor", "Room", "Semester"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table model with node data
        for (Node node : graphToVisualize.getNodes()) {
            String nodeNumber = "" + node.getName();
            String professor = "" + node.getLecturer();
            String room = node.getRoom() != null ? "" + (100 + node.getRoom()) : "-";
            String semester = "" + (node.getSemester() + 1);

            tableModel.addRow(new Object[]{nodeNumber, professor, room, semester});
        }

        // Create a JTable to display the node details
        JTable nodeTable = new JTable(tableModel);
        nodeTable.setFillsViewportHeight(true);

        // Customize the table header (bold and larger font)
        nodeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        nodeTable.getTableHeader().setPreferredSize(new Dimension(nodeTable.getColumnModel().getTotalColumnWidth(), 40));

        // Customize cell styles (center alignment and larger font)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int col = 0; col < nodeTable.getColumnCount(); col++) {
            nodeTable.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
        }

        // Add the table to the info panel
        JScrollPane tableScrollPane = new JScrollPane(nodeTable);
        infoPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add the info panel to the left side of the frame
        frame.add(infoPanel, BorderLayout.WEST);

        // Apply an organic layout to arrange the graph automatically
        mxOrganicLayout layout = new mxOrganicLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // Center the graph within its viewport
        double graphWidth = graphAdapter.getGraphBounds().getWidth();
        double viewWidth = graphComponent.getViewport().getWidth();
        double xOffset = ((viewWidth - graphWidth) / 2) - 100;
        double yOffset = 100;
        graphComponent.getGraph().getView().setTranslate(new com.mxgraph.util.mxPoint(xOffset, yOffset));

        // Display the JFrame
        frame.setSize(1000, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
