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

    // Mock Data for Modules and Professors
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

    private Map<Integer, List<Integer>> createColorToNodeMap(List<Node> nodes) {
        // Map Color to Node Number (for every color a list with the related nodes)
        return nodes.stream()
                .collect(Collectors.groupingBy(
                        Node::getColor, // Group by color
                        Collectors.mapping( // Map Node numbers
                                Node::getName, // Node-Name as Integer
                                Collectors.toList() // Collect as List
                        )));
    }

    private Map<Integer, String> createColorToTimeslotMap(graphennetzwerke.Graph graph, int minColor) {
        Map<Integer, String> colorToTimeSlot = new HashMap<>();
        Random random = new Random();
        Set<String> usedSlots = new HashSet<>();

        for (int color = 1; color < (minColor + 1); color++) {
            String timeSlot;
            do {
                timeSlot = fullTimeSlots.get(random.nextInt(fullTimeSlots.size()));
            } while (usedSlots.contains(timeSlot));
            colorToTimeSlot.put(color, timeSlot);
            usedSlots.add(timeSlot);
        }
        return colorToTimeSlot;
    }

    public void generateTimetable(graphennetzwerke.Graph graph, int minColor) {
        // Map Color/Block to Node number/Module
        Map<Integer, List<Integer>> blockModules = createColorToNodeMap(graph.getNodes());
        // Print Node to Color/Block
        // System.out.println("\nNode to Color/Block:");
        // for (Map.Entry<Integer, List<Integer>> entry : blockModules.entrySet()) {
        //     System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        // }

        // Map Color to Timetableslot
        Map<Integer, String> colorToTimeSlot = createColorToTimeslotMap(graph, minColor);
        // Print Color/Block to Timetableslot
        // System.out.println("\nColor/Block to Timetableslot:");
        // for (Map.Entry<Integer, String> entry : colorToTimeSlot.entrySet()) {
        //     System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        // }

        visualizeDetailedTimetable(graph, colorToTimeSlot, blockModules);
        visualizeSimpleTimetable(graph, colorToTimeSlot, blockModules);
    }

    public void visualizeSimpleTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules) {
        visualizeTimetable(graph, colorToTimeSlot, blockModules, false);
    }

    public void visualizeDetailedTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules) {
        visualizeTimetable(graph, colorToTimeSlot, blockModules, true);
    }

    public void visualizeTimetable(graphennetzwerke.Graph graph, Map<Integer, String> colorToTimeSlot, Map<Integer, List<Integer>> blockModules, boolean detailed) {
        // Tabelle vorbereiten
        String[] columns = new String[days.length + 1];
        columns[0] = "Zeit / Tag"; // Erste Zeile für die Zeit
        System.arraycopy(days, 0, columns, 1, days.length);
    
        String[][] data = new String[timeSlots.length][columns.length];
        for (int i = 0; i < timeSlots.length; i++) {
            data[i][0] = timeSlots[i]; // Fülle die erste Spalte mit den Zeiträumen
        }
    
        // Daten füllen
        for (Map.Entry<Integer, String> entry : colorToTimeSlot.entrySet()) {
            List<Node> nodes = graph.getNodes();
            int color = entry.getKey();
            String timeSlot = entry.getValue();
    
            // Zeit und Tag aus dem Zeitblock extrahieren
            String[] split = timeSlot.split(" ");
            String day = split[0];
            String time = split[1];
    
            // Finde Zeile und Spalte
            int row = Arrays.asList(timeSlots).indexOf(time);
            int col = Arrays.asList(days).indexOf(day) + 1; // +1 wegen der Zeitspalte
    
            // Module für diesen Block
            List<Integer> nodeNumbers = blockModules.getOrDefault(color - 1, List.of());
    
            StringBuilder cellContent = new StringBuilder("<html>");
            for (int i = 0; i < nodeNumbers.size(); i++) {
                int nodeNumber = nodeNumbers.get(i);
                Node node = nodes.get(nodeNumber - 1);

                if (detailed) {
                    int professorId = node.getLecturer();
                    String moduleName = moduleNames[nodeNumber - 1];
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
                    // Füge eine horizontale Linie hinzu, außer beim letzten Modul
                    if (i < nodeNumbers.size() - 1) {
                        cellContent.append("<hr>").append("<br>");
                    }
                } else {
                    cellContent.append(nodeNumber).append(", ");
                }               
            }
            cellContent.append("</html>");
            data[row][col] = cellContent.toString();
        }
    
        // Tabelle erstellen
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);
    
        // Kopfzeilen-Stil anpassen
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
    
        // Zeilenhöhe basierend auf HTML-Inhalt anpassen
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxLines = 1; // Mindestens eine Zeile
            for (int col = 1; col < table.getColumnCount(); col++) { // Spalte 0 ist die Zeit
                Object value = table.getValueAt(row, col);
                if (value != null && value.toString().startsWith("<html>")) {
                    // Zeilen zählen: Anzahl der <br>-Tags + 1
                    maxLines = Math.max(maxLines, value.toString().split("<br>").length) - 1;
                }
            }
            System.out.println("Row" + row + ": " + maxLines);
            if (maxLines <= 1) {
                table.setRowHeight(row, 80); // Standardhöhe
            }
            else {
                table.setRowHeight(row, maxLines * 20); // 20px pro Zeile
            } 
        }
    
        // Zelleninhalt als HTML rendern und horizontal zentrieren
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setHorizontalAlignment(SwingConstants.CENTER); // Inhalt horizontal zentrieren
                label.setVerticalAlignment(SwingConstants.TOP);      // Inhalt oben ausrichten
                label.setOpaque(true);
                label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return label;
            }
        };

        // Renderer für alle Spalten setzen
        for (int col = 0; col < table.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }
    
        // Frame erstellen
        JFrame frame = new JFrame(detailed ? "Detaillierter Stundenplan" : "Simpler Stundenplan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    
}
