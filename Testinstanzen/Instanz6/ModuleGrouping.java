import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ModuleGrouping {

    public static void main(String[] args) {
        int totalModules = 50;       // Anzahl der Module
        int maxGroupSize = 5;       // Maximale Gruppengröße pro Zeile
        String[] fileNames = {"D.txt", "R.txt", "S.txt"}; // Namen der Dateien

        // Liste der Module erstellen (1 bis totalModules)
        List<Integer> modules = new ArrayList<>();
        for (int i = 1; i <= totalModules; i++) {
            modules.add(i);
        }

        // Dozentendatei: Alle Module verwenden
        createFileWithGroups(modules, maxGroupSize, "D.txt", false);

        // Raumdatei: Nicht alle Module verwenden, Reihenfolge zufällig
        createFileWithGroups(modules, maxGroupSize, "R.txt", true);

        // Semesterdatei: Alle Module verwenden, Reihenfolge zufällig
        createFileWithGroups(modules, maxGroupSize, "S.txt", true);

        System.out.println("Dateien wurden erfolgreich erstellt!");
    }

    private static void createFileWithGroups(List<Integer> modules, int maxGroupSize, String fileName, boolean shuffleModules) {
        Random random = new Random();
        List<Integer> remainingModules = new ArrayList<>(modules);

        // Wenn shuffleModules true ist, werden die Module zufällig gemischt
        if (shuffleModules) {
            Collections.shuffle(remainingModules);
            // Für Räume (R.txt): Entferne einen zufälligen Teil der Module (z. B. 30%)
            if (fileName.equals("R.txt")) {
                int toRemove = remainingModules.size() / 3;
                remainingModules = remainingModules.subList(0, remainingModules.size() - toRemove);
            }
        }

        List<List<Integer>> groups = new ArrayList<>();

        // Module in Gruppen aufteilen
        while (!remainingModules.isEmpty()) {
            int groupSize = 2 + random.nextInt(maxGroupSize - 1); // Gruppengröße zwischen 2 und maxGroupSize
            groupSize = Math.min(groupSize, remainingModules.size()); // Sicherstellen, dass die Gruppe nicht größer ist als verbleibende Module
            List<Integer> group = new ArrayList<>(remainingModules.subList(0, groupSize));
            groups.add(group);
            remainingModules.subList(0, groupSize).clear(); // Entfernte Module aus der Liste
        }

        // Datei schreiben
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (List<Integer> group : groups) {
                writer.write(String.join(",", group.stream().map(String::valueOf).toArray(String[]::new)));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der Datei: " + fileName);
            e.printStackTrace();
        }
    }
}
