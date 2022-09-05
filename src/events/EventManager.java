package events;

import entities.Entity;
import entities.Player;
import main.GameState;
import main.Scene;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.*;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;

public class EventManager {
    private final Scene scene;
    private final EventBox[][] eventBox;
    private int previousEventX, previousEventY;
    private boolean canTouchEvent;

    public EventManager(Scene scene) {
        this.scene = scene;
        eventBox = new EventBox[MAX_WORLD_COL][MAX_WORLD_ROW];

        initEventBox();
    }

    private void initEventBox() {
        int col = 0;
        int row = 0;
        while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
            eventBox[col][row] = new EventBox();
            eventBox[col][row].x = 23;
            eventBox[col][row].y = 23;
            eventBox[col][row].width = 2;
            eventBox[col][row].height = 2;
            eventBox[col][row].setEventBoxDefaultX(eventBox[col][row].x);
            eventBox[col][row].setEventBoxDefaultY(eventBox[col][row].y);

            col++;
            if(col == MAX_WORLD_COL) {
                col = 0;
                row++;
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
            if(trigger(27, 16, RIGHT)) {
                damagePit(27, 16, DIALOGUE);
            } else if(trigger(23, 19, ANY)) {
                damagePit(27, 16, DIALOGUE);
            } else if(trigger(23, 12, UP)) {
                healingPool(23, 12, DIALOGUE);
            } else if(trigger(13, 21, LEFT)) {
                teleport(13, 21, DIALOGUE);
            } else if(trigger(12, 22, UP)) {
                speak(scene.getNPCs());
            }
        }
    }

    private boolean trigger(int col, int row, String reqDirection) {
        boolean trigger = false;

        Player player = scene.getPlayer();
        player.getHitbox().x = player.getWorldX() + player.getHitbox().x;
        player.getHitbox().y = player.getWorldY() + player.getHitbox().y;
        eventBox[col][row].x = col * TILE_SIZE + eventBox[col][row].x;
        eventBox[col][row].y = row * TILE_SIZE + eventBox[col][row].y;

        if(player.getHitbox().intersects(eventBox[col][row]) && ! eventBox[col][row].isEventHappened()) {
            if(player.getDirection().contentEquals(reqDirection) || reqDirection.contentEquals(ANY)) {
                trigger = true;

                previousEventX = scene.getPlayer().getWorldX();
                previousEventY = scene.getPlayer().getWorldY();
            }
        }

        player.getHitbox().x = player.getHitboxDefaultX();
        player.getHitbox().y = player.getHitboxDefaultY();
        eventBox[col][row].x = eventBox[col][row].getEventBoxDefaultX();
        eventBox[col][row].y = eventBox[col][row].getEventBoxDefaultY();

        return trigger;
    }

    private void damagePit(int col, int row, GameState gameState) {
        GameState.gameState = gameState;
        scene.getAudioManager().playEffect(RECEIVED_DAMAGE);
        scene.getGui().setCurrentDialogue("You fall into a pit!");
        scene.getPlayer().lostLife();
        canTouchEvent = false;
        scene.getAssetSetter().setMonsters();
    }

    private void healingPool(int col, int row, GameState gameState) {
        if(scene.getKeyInputs().isEnterPressed()) {
            GameState.gameState = gameState;
            scene.getPlayer().setAttackCanceled(true);
            scene.getAudioManager().playEffect(POWER_UP);
            scene.getGui().setCurrentDialogue("You drink the water.\nYour life and mana have been recovered!");
            scene.getPlayer().setLife(scene.getPlayer().getMaxLives());
            scene.getPlayer().setMana(scene.getPlayer().getMaxMana());
            eventBox[col][row].setEventHappened(true);
            scene.getAssetSetter().setMonsters();
        }
    }

    private void teleport(int col, int row, GameState gameState) {
        GameState.gameState = gameState;
        scene.getAudioManager().playEffect(FAN_FARE);
        scene.getGui().setCurrentDialogue("Teleport!");
        scene.getPlayer().setWorldX(37 * TILE_SIZE);
        scene.getPlayer().setWorldY(10 * TILE_SIZE);
    }


    private void speak(Entity[] npc) {
        if(scene.getKeyInputs().isEnterPressed()) {
            gameState = DIALOGUE;
            scene.getPlayer().setAttackCanceled(true);
            npc[1].speak();
        }
    }
}
