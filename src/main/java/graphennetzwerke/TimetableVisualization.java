package graphennetzwerke;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableVisualization {

    // Define timeslots for each day
    List<String> fullTimeSlots = List.of(
            "Mo 08:00-11:00", "Mo 11:30-14:30", "Mo 15:00-18:00",
            "Di 08:00-11:00", "Di 11:30-14:30", "Di 15:00-18:00",
            "Mi 08:00-11:00", "Mi 11:30-14:30", "Mi 15:00-18:00",
            "Do 08:00-11:00", "Do 11:30-14:30", "Do 15:00-18:00",
            "Fr 08:00-11:00", "Fr 11:30-14:30", "Fr 15:00-18:00");

    // Days and timeslots used for table headers
    String[] days = { "Mo", "Di", "Mi", "Do", "Fr" };
    String[] timeSlots = { "08:00-11:00", "11:30-14:30", "15:00-18:00" };

    // Mock data for modules and professors
    String[] moduleNames = {
            "Mathematik", "3D Bildverarbeitung", "Programmierung", "Machine Learning", "Parallel Programming",
            "Big Data", "Distributed Applications", "Cloud Computing", "Process Mining", "Masterseminar",
            "Computer Graphics", "Englisch", "Speichersysteme", "VR / AR", "Spanisch",
            "Russisch", "Data Visualization", "Businessanwendungen", "Teamprojekt", "Betriebssysteme",
            "BWL", "Web-Applikationen", "IT-Sicherheit"
    };

    String[] professorNames = {
            "Prof. Müller", "Prof. Schmidt", "Prof. Schneider", "Prof. Fischer", "Prof. Weber",
            "Prof. Meyer", "Prof. Wagner", "Prof. Becker", "Prof. Schulz", "Prof. Hoffmann",
            "Prof. Schott", "Prof. Frank"
    };

    public TimetableVisualization() {
    }

    /**
     * Creates a mapping of color IDs to node numbers.
     * Each color corresponds to a block of modules scheduled in the same timeslot.
     */
    private Map<Integer, List<Integer>> createColorToNodeMap(List<Node> nodes) {
        return nodes.stream()
                .collect(Collectors.groupingBy(
                        Node::getColor, // Group nodes by their color
                        Collectors.mapping(
                                Node::getName, // Map node names (IDs)
                                Collectors.toList() // Collect as a list
                        )));
    }

    /**
     * Assigns timeslots to colors (blocks) randomly, ensuring no duplicates.
     */
    private Map<Integer, String> createColorToTimeslotMap(graphennetzwerke.Graph graph, int minColor) {
        Map<Integer, String> colorToTimeSlot = new HashMap<>();
        Random random = new Random();
        Set<String> usedSlots = new HashSet<>();

        for (int color = 1; color < (minColor + 1); color++) {
            String timeSlot;
            do {
                timeSlot = fullTimeSlots.get(random.nextInt(fullTimeSlots.size())); // Select a random timeslot
            } while (usedSlots.contains(timeSlot)); // Ensure timeslot is not already used
            colorToTimeSlot.put(color, timeSlot);
            usedSlots.add(timeSlot);
        }
        return colorToTimeSlot;
    }

    /**
     * Main method to generate the timetable by mapping nodes to timeslots and displaying them.
     */
    public void generateTimetable(graphennetzwerke.Graph graph, int minColor) {
        Map<Integer, List<Integer>> blockModules = createColorToNodeMap(graph.getNodes());
        Map<Integer, String> colorToTimeSlot = createColorToTimeslotMap(graph, minColor);

        // Render both simple and detailed versions of the timetable
        visualizeDetailedTimetable(graph, colorToTimeSlot, blockModules);
        visualizeSimpleTimetable(graph, colorToTimeSlot, blockModules);
    }

    /**
     * Visualizes a simple timetable where only node numbers are displayed.
     */
    public void visualizeSimpleTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules) {
        visualizeTimetable(graph, colorToTimeSlot, blockModules, false);
    }

    /**
     * Visualizes a detailed timetable with module, professor, room, and semester information.
     */
    public void visualizeDetailedTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules) {
        visualizeTimetable(graph, colorToTimeSlot, blockModules, true);
    }

    /**
     * Generic method to visualize a timetable.
     * Detailed or simple view is toggled with the `detailed` parameter.
     */
    public void visualizeTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules, boolean detailed) {
        // Disable detailed view if there aren't enough mock data
        if (graph.getNodes().size() > 20) detailed = false;

        // Prepare table headers
        String[] columns = new String[days.length + 1];
        columns[0] = "Zeit / Tag"; // First column for time slots
        System.arraycopy(days, 0, columns, 1, days.length);

        // Prepare table data
        String[][] data = new String[timeSlots.length][columns.length];
        for (int i = 0; i < timeSlots.length; i++) {
            data[i][0] = timeSlots[i]; // Fill the first column with times
        }

        // Populate table data with module details
        for (Map.Entry<Integer, String> entry : colorToTimeSlot.entrySet()) {
            List<Node> nodes = graph.getNodes();
            int color = entry.getKey();
            String timeSlot = entry.getValue();

            String[] split = timeSlot.split(" ");
            String day = split[0];
            String time = split[1];

            int row = Arrays.asList(timeSlots).indexOf(time);
            int col = Arrays.asList(days).indexOf(day) + 1;

            List<Integer> nodeNumbers = blockModules.getOrDefault(color - 1, List.of());

            StringBuilder cellContent = new StringBuilder("<html>");
            for (int i = 0; i < nodeNumbers.size(); i++) {
                int nodeNumber = nodeNumbers.get(i);
                Node node = nodes.get(nodeNumber - 1);

                if (detailed) {
                    int professorId = node.getLecturer();
                    String moduleName = moduleNames[nodeNumber - 1] + " (" + node.getName() + ")";
                    String semester = (node.getSemester() + 1) + ". Semester";
                    String professorName = professorNames[professorId];
                    cellContent.append(moduleName).append("<br>")
                            .append(semester).append("<br>")
                            .append(professorName).append("<br>");

                    if (node.getRoom() != null) {
                        int roomId = node.getRoom();
                        String room = "Raum " + (100 + roomId);
                        cellContent.append(room).append("<br>");
                    }
                    if (i < nodeNumbers.size() - 1) {
                        cellContent.append("<br>").append("<hr>").append("<br>");
                    }
                } else {
                    cellContent.append(nodeNumber);
                    if (i < nodeNumbers.size() - 1) {
                        cellContent.append(", ");
                    }
                }
            }
            cellContent.append("</html>");
            data[row][col] = cellContent.toString();
        }

        // Create the JTable
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);

        // Customize header style
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);

        // Adjust row heights based on content
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxLines = 1;
            for (int col = 1; col < table.getColumnCount(); col++) {
                Object value = table.getValueAt(row, col);
                if (value != null && value.toString().startsWith("<html>")) {
                    maxLines = Math.max(maxLines, value.toString().split("<br>").length) - 1;
                }
            }
            table.setRowHeight(row, maxLines <= 1 ? 80 : maxLines * 20);
        }

        // Render cells as HTML and center-align content
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.TOP);
                label.setOpaque(true);
                label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return label;
            }
        };

        // Apply renderer to all columns
        for (int col = 0; col < table.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }

        // Create and display the JFrame
        JFrame frame = new JFrame(detailed ? "Detailed Timetable" : "Simple Timetable");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
