package ai;

import main.Scene;

import java.util.ArrayList;

import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;

public class PathFinder {
    public ArrayList<Node> pathList = new ArrayList<>();
    Scene scene;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached;
    int step;

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
            node[col][row].solid = false;
            node[col][row].open = false;
            node[col][row].checked = false;

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
                node[col][row].solid = true;
            }
            // CHECK INTERACTIVE TILES
            for(int i = 0; i < scene.getInteractiveTiles()[1].length; i++) {
                if(scene.getInteractiveTiles()[scene.currentMap][i] != null && scene.getInteractiveTiles()[scene.currentMap][i].destructible) {
                    int itCol = scene.getInteractiveTiles()[scene.currentMap][i].getWorldX() / TILE_SIZE;
                    int itRow = scene.getInteractiveTiles()[scene.currentMap][i].getWorldY() / TILE_SIZE;
                    node[itCol][itRow].solid = true;
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
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // F Cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while(! goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
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
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // If F cost is equal, check the G cost
                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
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
        if(! node.open && ! node.checked && ! node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;

        while(current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}