package graphennetzwerke;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;

/**
 * Hello JGraphT!
 */
public class GraphVisualization {

	// Colors
	String[] colors = {"red", "blue", "green", "yellow", "orange", 
                   "purple", "pink", "brown", "cyan", "lime"};

	public GraphVisualization() {

	}

	public void visualizeGraph(graphennetzwerke.Graph graphToVisualize) {
		Graph<String, DefaultEdge> graph = GraphTypeBuilder
		.directed()
		.allowingMultipleEdges(true)
		.allowingSelfLoops(true)
		.vertexSupplier(SupplierUtil.createStringSupplier())
		.edgeSupplier(SupplierUtil.createDefaultEdgeSupplier())
		.buildGraph();

		// Mapping for Nodes and Vertices
		Map<Node, String> nodeMap = new HashMap<>();
		Map<String, Node> vertexMap = new HashMap<>();

		// Create Vertices
		for (Node node : graphToVisualize.getNodes()) {
			String v = graph.addVertex();
			nodeMap.put(node, v);
			vertexMap.put(v, node);
		}

		// Create Edges
		for (Edge edge : graphToVisualize.getEdges()) {
			String u = nodeMap.get(edge.getU());
			String v = nodeMap.get(edge.getV());
			graph.addEdge(u, v);
		}

		// JGraphX-Adapter
		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

		// Color graph
		Map<String, mxICell> vertexToCellMap = graphAdapter.getVertexToCellMap();
		int index = 0;
        for (String vertex : graph.vertexSet()) {
			Node node = vertexMap.get(vertex);
            String color = colors[node.getColor()]; // Zyklische Farbzuweisung
            graphAdapter.setCellStyle("fillColor=" + color, new mxICell[]{vertexToCellMap.get(vertex)});
            index++;
        }

		// JFrame für die Visualisierung
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
		frame.add(graphComponent);

		// Layout für den Graphen
		mxCircleLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		frame.setVisible(true);
	}

	public void visualizeGraphDemo() {
		Graph<String, DefaultEdge> graph = GraphTypeBuilder
		.directed()
		.allowingMultipleEdges(true)
		.allowingSelfLoops(true)
		.vertexSupplier(SupplierUtil.createStringSupplier())
		.edgeSupplier(SupplierUtil.createDefaultEdgeSupplier())
		.buildGraph();

		String v0 = graph.addVertex();
		String v1 = graph.addVertex();
		String v2 = graph.addVertex();

		graph.addEdge(v0, v1);
		graph.addEdge(v1, v2);
		graph.addEdge(v0, v2);

		for (String v : graph.vertexSet()) {
			System.out.println("vertex: " + v);
		}

		for (DefaultEdge e : graph.edgeSet()) {
			System.out.println("edge: " + e);
		}

		// JGraphX-Adapter
		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

		// JFrame für die Visualisierung
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
		frame.add(graphComponent);

		// Layout für den Graphen
		mxCircleLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		frame.setVisible(true);
	}
}
