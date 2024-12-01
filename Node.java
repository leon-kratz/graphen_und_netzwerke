import java.util.ArrayList;

public class Node {
  private Integer name;
  private Integer color;
  private Integer lecturer;
  private Integer semester;
  private Integer room;
  private Integer degree;
  private String title;
  private ArrayList<Node> neighbors = new ArrayList<Node>();

  public Node(Integer name, Integer color, Integer lecturer, Integer semester, Integer room) {
    this.name = name;
    this.color = color;
    this.lecturer = lecturer;
    this.semester = semester;
    this.room = room;
    this.degree = 0;
    this.title = "";
  }

  public Node(Integer name, Integer color, Integer lecturer, Integer semester, Integer degree, Integer room) {
    this.name = name;
    this.color = color;
    this.lecturer = lecturer;
    this.semester = semester;
    this.room = room;
    this.degree = degree;
    this.title = "";
  }

  public Node(Integer name, Integer color, Integer lecturer, Integer semester, Integer room, Integer degree,
      String title) {
    this.name = name;
    this.color = color;
    this.lecturer = lecturer;
    this.semester = semester;
    this.room = room;
    this.degree = degree;
    this.title = title;
  }

  public Integer getName() {
    return this.name;
  }

  public void setName(Integer name) {
    this.name = name;
  }

  public Integer getColor() {
    return this.color;
  }

  public void setColor(Integer color) {
    this.color = color;
  }

  public Integer getLecturer() {
    return lecturer;
  }

  public void setLecturer(Integer lecturer) {
    this.lecturer = lecturer;
  }

  public Integer getSemester() {
    return this.semester;
  }

  public void setSemester(Integer semester) {
    this.semester = semester;
  }

  public Integer getRoom() {
    return this.room;
  }

  public void setRoom(Integer room) {
    this.room = room;
  }

  public Integer getDegree() {
    return this.degree;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public void addDegree() {
    this.degree += +1;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArrayList<Node> getNeighbors() {
    return this.neighbors;
  }

  public void addNeighbor(Node node) {
    neighbors.add(node);
  }

  public void removeNeighbor(Node node) {
    neighbors.remove(node);
  }

  @Override
  public String toString() {
    return "Node{" +
        "name='" + name + '\'' +
        ", color=" + color +
        ", lecturer=" + lecturer +
        ", room=" + room +
        ", semester=" + semester +
        '}';
  }
}
