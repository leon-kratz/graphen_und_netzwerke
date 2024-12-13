# graphen_und_netzwerke

## Visualization

Um die Ergebnisse der Algorithmen anschaulich und leicht auswertbar zu machen, wird der eingefärbte Graph nach der Berechnung der minimalen Färbung visualisiert. Mit der Visualisierung des Graphen und der Darstellung als Stundenplan stehen zwei unterschiedliche Methoden zur Verfügung, die die Lösung des Problems greifbar machen.

### Graph

Der eingefärbte Graph wird auf einem JFrame visualisiert, was eine intuitive und klare Auswertung der Ergebnisse ermöglicht. Zusätzlich zeigt das "Node Details"-Panel eine Übersicht mit wichtigen Informationen wie Professor, Raum und Semester zu den einzelnen Nodes. Dies liefert alle relevanten Daten zur Analyse des Graphen.

<img src="/images/graph.png" alt="Graph Image" width="200"/>

### Timetable

Um die Ergebnisse der Algorithmen im Kontext der Stundenplanerstellung nachvollziehbar darzustellen, wird ein Stundenplan erstellt. Es stehen zwei Varianten zur Verfügung: ein vereinfachter Stundenplan, der lediglich die Nummern der Nodes den entsprechenden Blöcken zuordnet, und ein detaillierter Stundenplan, der mit zusätzlichen Mock-Daten gefüllt wird, um die Lösung greifbar und verständlich zu machen.

Jede Farbe mit ihren zugehörigen Nodes wird einem zufälligen freien Block im Stundenplan zugewiesen. Im detaillierten Stundenplan werden neben Raum und Semester auch Modulnamen und Professoren (als Mock-Daten) zu den Nodes eingetragen.

#### Simple Timetable

Ein einfacher Stundenplan, der die Knotennummern (Nodes) in die entsprechenden Blöcke einträgt.

<img src="/images/timetable-simple.png" alt="Simple Timetable Image" width="200"/>

#### Detailed Timetable

Ein detaillierter Stundenplan, der neben Raum und Semester zusätzliche Informationen wie Modulnamen und Professoren anzeigt, um die Lösung praxisnah darzustellen.

<img src="/images/timetable-detailed.png" alt="Detailed Timetable with mock Data Image" width="200"/>

## Setup Project

### VS Code

#### Install Java Extension Pack

Install the following vs code extension pack:

Name: Extension Pack for Java\
Id: vscjava.vscode-java-pack\
Description: Popular extensions for Java development that provides Java IntelliSense, debugging, testing, Maven/Gradle support, project management and more\
Version: 0.29.0\
Publisher: Microsoft\
VS Marketplace Link: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack

#### Install Maven

Follow [this Guide](https://phoenixnap.com/kb/install-maven-windows) to install Maven (Windows)

#### Setup Maven

Make sure that you are in the root directory of the repo:

```bash
mvn install
```

### Command Line

## Run Project

Select/Open `CalculateTimetable` file and hit `Run Java` in the vs code editor.

