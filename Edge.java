public class Edge {
    private Node v;
    private Node u;

    public Edge(Node v, Node u) {
        this.v = v;
        this.u = u;
        this.u.addDegree();
        this.v.addDegree();
        this.u.addNeighbor(v);
        this.v.addNeighbor(u);
    }

    public Node getV() {
        return v;
    }

    public void setV(Node v) {
        this.v = v;
    }

    public Node getU() {
        return u;
    }

    public void setU(Node u) {
        this.u = u;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "v=" + v.getName() +
                ", u=" + u.getName() +
                '}';
    }
}
