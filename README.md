# graphen_und_netzwerke

## Visualization

Um die Ergebnisse der Algorithmen anschaulich und leicht auswertbar zu machen, wird der eingefärbte Graph nach der Berechnung der minimalen Färbung visualisiert. Mit der Visualisierung des Graphen und der Darstellung als Stundenplan stehen zwei unterschiedliche Methoden zur Verfügung, die die Lösung des Problems greifbar machen.

### Graph

Der eingefärbte Graph wird auf einem JFrame visualisiert, was eine intuitive und klare Auswertung der Ergebnisse ermöglicht. Zusätzlich zeigt das "Node Details"-Panel eine Übersicht mit wichtigen Informationen wie Professor, Raum und Semester zu den einzelnen Nodes. Dies liefert alle relevanten Daten zur Analyse des Graphen.

<img src="/images/graph.png" alt="Graph Image" width="800"/>

### Timetable

Um die Ergebnisse der Algorithmen im Kontext der Stundenplanerstellung nachvollziehbar darzustellen, wird ein Stundenplan erstellt. Es stehen zwei Varianten zur Verfügung: ein vereinfachter Stundenplan, der lediglich die Nummern der Nodes den entsprechenden Blöcken zuordnet, und ein detaillierter Stundenplan, der mit zusätzlichen Mock-Daten gefüllt wird, um die Lösung greifbar und verständlich zu machen.

Jede Farbe mit ihren zugehörigen Nodes wird einem zufälligen freien Block im Stundenplan zugewiesen. Im detaillierten Stundenplan werden neben Raum und Semester auch Modulnamen und Professoren (als Mock-Daten) zu den Nodes eingetragen.

#### Simple Timetable

Ein einfacher Stundenplan, der die Knotennummern (Nodes) in die entsprechenden Blöcke einträgt.

<img src="/images/timetable-simple.png" alt="Simple Timetable Image" width="800"/>

#### Detailed Timetable

Ein detaillierter Stundenplan, der neben Raum und Semester zusätzliche Informationen wie Modulnamen und Professoren anzeigt, um die Lösung praxisnah darzustellen.

<img src="/images/timetable-detailed.png" alt="Detailed Timetable with mock Data Image" width="800"/>

## Setup Project

### VS Code

#### Installiere Java Extension Pack

Installiere das folgende Java Extension Pack:

Name: Extension Pack for Java\
Id: vscjava.vscode-java-pack\
Description: Popular extensions for Java development that provides Java IntelliSense, debugging, testing, Maven/Gradle support, project management and more\
Version: 0.29.0\
Publisher: Microsoft\
VS Marketplace Link: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack

#### Installiere Maven

Folge [dem Guide](https://phoenixnap.com/kb/install-maven-windows), um Maven (Windows) zu installieren

#### Setup Maven

Stelle sicher, dass du im root directory des Repo's bist:

```bash
mvn install
```

### Command Line

## Run Project

Selektiere und öffne die `CalculateTimetable` Datei und klicke `Run Java` in dem vs code editor.

Das Programm führt standardmäßig alle drei implementierten Algorithmen ([Sequenzieller Algorithmus](/src/main/java/graphennetzwerke/Graph.java#L87), [Johnson Algorithmus](/src/main/java/graphennetzwerke/Graph.java#L112) und [Backtracking](/src/main/java/graphennetzwerke/Graph.java#L167)) auf allen fünf gegebenen Testinstanzen aus. Dabei findet noch keine Visualisierung statt.

Für die Visualisierung muss das Programm entweder mit dem relativen Dateipfad (bspw. ".\Testinstanzen\Instanz1\") als erstes Argument ausgeführt werden oder die Variable [folderPath](/src/main/java/graphennetzwerke/CalculateTimetable.java#L10) aus der CalculateTimetable.java muss händisch gesetzt werden.

## Own Dataset

Neben den fünf Datensätzen haben wir, um die Laufzeit der Algorithmen zu testen, das Programm [ModuleGrouping.java](/Testinstanzen/Instanz6/ModuleGrouping.java) geschrieben, dass in dem Ordner [Instanz6](/Testinstanzen/Instanz6/) gespeichert ist. Dieses Programm erzeugt einen eigenen Datensatz mit 50 Modulen basierend auf dem Aufbau der anderen Datensätze.

