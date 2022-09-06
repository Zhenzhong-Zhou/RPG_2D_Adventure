package ai;

public class Node {
    public int col, row;
    Node parent;
    int gCost, hCost, fCost;
    boolean solid, open, checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}
