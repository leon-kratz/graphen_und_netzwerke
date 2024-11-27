public class Node {
  private Integer name;
  private Integer color;
  private Integer lecturer;
  private Integer semester;
  private Integer room;
  private String title;

  public Node(Integer name, Integer color, Integer lecturer, Integer semester, Integer room) {
      this.name = name;
      this.color = color;
      this.lecturer = lecturer;
      this.semester = semester;
      this.room = room;
      this.title = "";
  }
  
  public Node(Integer name, Integer color, Integer lecturer, Integer semester, Integer room, String title) {
    this.name = name;
    this.color = color;
    this.lecturer = lecturer;
    this.semester = semester;
    this.room = room;
    this.title = title;
}

  public Integer getName() {
      return name;
  }

  public void setName(Integer name) {
      this.name = name;
  }

  public Integer getColor() {
      return color;
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
      return semester;
  }

  public void setSemester(Integer semester) {
      this.semester = semester;
  }

  public Integer getRoom() {
      return room;
  }

  public void setRoom(Integer room) {
      this.room = room;
  }

  public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
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
