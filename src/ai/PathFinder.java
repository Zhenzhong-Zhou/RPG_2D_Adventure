package ai;

import main.Scene;

import java.util.ArrayList;

import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;

public class PathFinder {
    private final ArrayList<Node> pathList = new ArrayList<>();
    private final Scene scene;
    private final ArrayList<Node> openList = new ArrayList<>();
    private Node[][] node;
    private Node startNode, goalNode, currentNode;
    private boolean goalReached;
    private int step;

    public PathFinder(Scene scene) {
        this.scene = scene;

        instantiateNodes();
    }

    private void instantiateNodes() {
        node = new Node[MAX_WORLD_COL][MAX_WORLD_ROW];

        int col = 0;
        int row = 0;

        while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
            node[col][row] = new Node(col, row);

            col++;
            if(col == MAX_WORLD_COL) {
                col = 0;
                row++;
            }
        }
    }

    private void resetNodes() {
        int col = 0;
        int row = 0;

        while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
            // Rest open, checked and solid state
            node[col][row].setSolid(false);
            node[col][row].setOpen(false);
            node[col][row].setChecked(false);

            col++;
            if(col == MAX_WORLD_COL) {
                col = 0;
                row++;
            }
        }

        // Reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        // Set Start and Goal node
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
            // SET SOLID NODE
            // CHECK TILES
            int tileNum = scene.getLevelManager().getTileId()[scene.currentMap][col][row];
            if(scene.getLevelManager().getTileManager().getTiles()[tileNum].isCollision()) {
                node[col][row].setSolid(true);
            }
            // CHECK INTERACTIVE TILES
            for(int i = 0; i < scene.getInteractiveTiles()[1].length; i++) {
                if(scene.getInteractiveTiles()[scene.currentMap][i] != null && scene.getInteractiveTiles()[scene.currentMap][i].destructible) {
                    int itCol = scene.getInteractiveTiles()[scene.currentMap][i].getWorldX() / TILE_SIZE;
                    int itRow = scene.getInteractiveTiles()[scene.currentMap][i].getWorldY() / TILE_SIZE;
                    node[itCol][itRow].setSolid(true);
                }
            }
            // SET COST
            getCost(node[col][row]);

            col++;
            if(col == MAX_WORLD_COL) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {
        // G Cost
        int xDistance = Math.abs(node.getCol() - startNode.getCol());
        int yDistance = Math.abs(node.getRow() - startNode.getRow());
        node.setgCost(xDistance + yDistance);
        // H Cost
        xDistance = Math.abs(node.getCol() - goalNode.getCol());
        yDistance = Math.abs(node.getRow() - goalNode.getRow());
        node.sethCost(xDistance + yDistance);
        // F Cost
        node.setfCost(node.getgCost() + node.gethCost());
    }

    public boolean search() {
        while(! goalReached && step < 500) {
            int col = currentNode.getCol();
            int row = currentNode.getRow();

            // Check the current node
            currentNode.setChecked(true);
            openList.remove(currentNode);

            // Open the Up node
            if(row - 1 >= 0) openNode(node[col][row - 1]);
            // Open the Left node
            if(col - 1 >= 0) openNode(node[col - 1][row]);
            // Open the Down node
            if(row + 1 < MAX_WORLD_ROW) openNode(node[col][row + 1]);
            // Open the Right node
            if(col + 1 < MAX_WORLD_COL) openNode(node[col + 1][row]);

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better
                if(openList.get(i).getfCost() < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).getfCost();
                }
                // If F cost is equal, check the G cost
                else if(openList.get(i).getfCost() == bestNodefCost) {
                    if(openList.get(i).getgCost() < openList.get(bestNodeIndex).getgCost()) {
                        bestNodeIndex = i;
                    }
                }
            }

            // If there is no node in the openList, end the loop
            if(openList.size() == 0) {
                break;
            }

            // After the loop, openList[bestNodeIndex] is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    private void openNode(Node node) {
        if(! node.isOpen() && ! node.isChecked() && ! node.isSolid()) {
            node.setOpen(true);
            node.setParent(currentNode);
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;

        while(current != startNode) {
            pathList.add(0, current);
            current = current.getParent();
        }
    }

    public ArrayList<Node> getPathList() {
        return pathList;
    }
}
