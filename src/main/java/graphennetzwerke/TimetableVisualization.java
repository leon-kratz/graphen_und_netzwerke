package graphennetzwerke;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableVisualization {
    // Timeslots
    List<String> fullTimeSlots = List.of(
            "Mo 08:00-11:00", "Mo 11:30-14:30", "Mo 15:00-18:00",
            "Di 08:00-11:00", "Di 11:30-14:30", "Di 15:00-18:00",
            "Mi 08:00-11:00", "Mi 11:30-14:30", "Mi 15:00-18:00",
            "Do 08:00-11:00", "Do 11:30-14:30", "Do 15:00-18:00",
            "Fr 08:00-11:00", "Fr 11:30-14:30", "Fr 15:00-18:00");

    String[] days = { "Mo", "Di", "Mi", "Do", "Fr" };
    String[] timeSlots = { "08:00-11:00", "11:30-14:30", "15:00-18:00" };

    public TimetableVisualization() {
    }

    private Map<Integer, List<Integer>> createColorNodeMap(List<Node> nodes) {
        // Map Color to Node (for every color a list with the related nodes)
        return nodes.stream()
                .collect(Collectors.groupingBy(
                        Node::getColor, // Group by color
                        Collectors.mapping( // Map Node numbers
                                Node::getName, // Node-Name as Integer
                                Collectors.toList() // Collect as List
                        )));
    }

    public void generateTimetable(graphennetzwerke.Graph graph) {
        // Map Color/Block to Node number/Module
        Map<Integer, List<Integer>> blockModules = createColorNodeMap(graph.getNodes());

        // Print Node to Color/Block
        System.out.println("\nNode to Color/Block:");
        for (Map.Entry<Integer, List<Integer>> entry : blockModules.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

        // Map Color to Timetableslot
        Map<Integer, String> colorToTimeSlot = new HashMap<>();
        Random random = new Random();
        Set<String> usedSlots = new HashSet<>();

        for (int color = 1; color < (graph.getMinColor() + 1); color++) {
            String timeSlot;
            do {
                timeSlot = fullTimeSlots.get(random.nextInt(fullTimeSlots.size()));
            } while (usedSlots.contains(timeSlot));
            colorToTimeSlot.put(color, timeSlot);
            usedSlots.add(timeSlot);
        }

        // Print Color/Block to Timetableslot
        System.out.println("\nColor/Block to Timetableslot:");
        for (Map.Entry<Integer, String> entry : colorToTimeSlot.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

        visualizeTimetable(colorToTimeSlot, blockModules);
    }

    public void visualizeTimetable(Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules) {
        // Prepare table
        String[] columns = new String[days.length + 1];
        columns[0] = "Time / Day"; // First row is the time
        System.arraycopy(days, 0, columns, 1, days.length);

        String[][] data = new String[timeSlots.length][columns.length];
        for (int i = 0; i < timeSlots.length; i++) {
            data[i][0] = timeSlots[i]; // Fill first row with the time
        }

        // Fill rows and columns
        for (Map.Entry<Integer, String> entry : colorToTimeSlot.entrySet()) {
            int color = entry.getKey();
            String timeSlot = entry.getValue();

            // Get time and day from timeslot
            String[] split = timeSlot.split(" ");
            String day = split[0];
            String time = split[1];

            // Get row and columns for the entry in the table
            int row = Arrays.asList(timeSlots).indexOf(time);
            int col = Arrays.asList(days).indexOf(day) + 1; // +1 cause of the time col

            // Modules/Nodes for this block
            List<String> modules = blockModules.getOrDefault(color, List.of()).stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            // Write to cel
            if (data[row][col] == null) {
                data[row][col] = String.join(", ", modules);
            } else {
                data[row][col] += ", " + String.join(", ", modules);
            }
        }

        // Create Table
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);

        // Style Table
        // Center cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Customize header style
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);

        // Customize table cell style
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        // Create Frame
        JFrame frame = new JFrame("Stundenplan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
