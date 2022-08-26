package events;

import entities.Player;
import main.GameState;
import main.Scene;

import static main.GameState.DIALOGUE;
import static utilities.Constants.AudioManager.*;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.GameConstant.MAX_MAP;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;

public class EventManager {
    private final Scene scene;
    private final EventBox[][][] eventBox;
    private int previousEventX, previousEventY;
    private boolean canTouchEvent;

    public EventManager(Scene scene) {
        this.scene = scene;
        eventBox = new EventBox[MAX_MAP][MAX_WORLD_COL][MAX_WORLD_ROW];

        initEventBox();
    }

    private void initEventBox() {
        int map = 0;
        int col = 0;
        int row = 0;
        while(map < MAX_MAP && col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {

            eventBox[map][col][row] = new EventBox();
            eventBox[map][col][row].x = 23;
            eventBox[map][col][row].y = 23;
            eventBox[map][col][row].width = 2;
            eventBox[map][col][row].height = 2;
            eventBox[map][col][row].setEventBoxDefaultX(eventBox[map][col][row].x);
            eventBox[map][col][row].setEventBoxDefaultY(eventBox[map][col][row].y);

            col++;
            if(col == MAX_WORLD_COL) {
                col = 0;
                row++;
                if(row == MAX_WORLD_ROW) {
                    row = 0;
                    map++;
                }
            }
        }
    }

    public void checkEvent() {
        // Check if the player is more than 1 tile away from the last event
        int xDistance = Math.abs(scene.getPlayer().getWorldX() - previousEventX);
        int yDistance = Math.abs(scene.getPlayer().getWorldY() - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > TILE_SIZE) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if(trigger(0,27, 16, RIGHT)) {
                damagePit(DIALOGUE);
            }
            else if(trigger(0,23, 19, ANY)) {
                damagePit(DIALOGUE);
            }
            else if(trigger(0,23, 12, UP)) {
                healingPool(DIALOGUE);
            }
            else if(trigger(0,19, 21, ANY)) {
                teleport(1,12,13,DIALOGUE);
            }
            else if(trigger(1,12,13, ANY)) {
                teleport(0,19, 21,DIALOGUE);
            }
        }
    }

    private boolean trigger(int map, int col, int row, String reqDirection) {
        boolean trigger = false;

        if(map == scene.currentMap) {
            Player player = scene.getPlayer();
            player.getHitbox().x = player.getWorldX() + player.getHitbox().x;
            player.getHitbox().y = player.getWorldY() + player.getHitbox().y;
            eventBox[map][col][row].x = col * TILE_SIZE + eventBox[map][col][row].x;
            eventBox[map][col][row].y = row * TILE_SIZE + eventBox[map][col][row].y;

            if(player.getHitbox().intersects(eventBox[map][col][row]) && ! eventBox[map][col][row].isEventHappened()) {
                if(player.getDirection().contentEquals(reqDirection) || reqDirection.contentEquals(ANY)) {
                    trigger = true;

                    previousEventX = scene.getPlayer().getWorldX();
                    previousEventY = scene.getPlayer().getWorldY();
                }
            }

            player.getHitbox().x = player.getHitboxDefaultX();
            player.getHitbox().y = player.getHitboxDefaultY();
            eventBox[map][col][row].x = eventBox[map][col][row].getEventBoxDefaultX();
            eventBox[map][col][row].y = eventBox[map][col][row].getEventBoxDefaultY();
        }

        return trigger;
    }

    private void damagePit(GameState gameState) {
        GameState.gameState = gameState;
        scene.getAudioManager().playEffect(RECEIVED_DAMAGE);
        scene.getGui().setCurrentDialogue("You fall into a pit!");
        scene.getPlayer().lostLife();
        canTouchEvent = false;
        scene.getAssetSetter().setMonsters();
    }

    private void healingPool(GameState gameState) {
        if(scene.getKeyInputs().isEnterPressed()) {
            GameState.gameState = gameState;
            scene.getPlayer().setAttackCanceled(true);
            scene.getAudioManager().playEffect(POWER_UP);
            scene.getGui().setCurrentDialogue("You drink the water.\nYour life and mana have been recovered!");
            scene.getPlayer().setLife(scene.getPlayer().getMaxLives());
            scene.getPlayer().setMana(scene.getPlayer().getMaxMana());
            eventBox[scene.currentMap][23][12].setEventHappened(true);
            scene.getAssetSetter().setMonsters();
        }
    }

    private void teleport(int map, int col, int row, GameState gameState) {
        GameState.gameState = gameState;
        scene.currentMap = map;
        scene.getPlayer().setWorldX(TILE_SIZE * col);
        scene.getPlayer().setWorldY(TILE_SIZE * row);
        previousEventX = scene.getPlayer().getWorldX();
        previousEventY = scene.getPlayer().getWorldY();
        canTouchEvent = false;
        scene.getAudioManager().playEffect(FAN_FARE);
        scene.getGui().setCurrentDialogue("Teleport!");
    }
}
