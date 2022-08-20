package events;

import entities.Player;
import main.GameState;
import main.Scene;

import java.awt.*;

import static main.GameState.*;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;

public class EventManager {
    private Scene scene;
    private Rectangle eventBox;
    private int eventBoxDefaultX, eventBoxDefaultY;

    public EventManager(Scene scene) {
        this.scene = scene;

        eventBox = new Rectangle(23,23,2,2);
        eventBoxDefaultX = eventBox.x;
        eventBoxDefaultY = eventBox.y;
    }

    public void checkEvent() {
        if(trigger(27, 16, RIGHT)) {damagePit(DIALOGUE);}
        if(trigger(23, 12, UP)) {healingPool(DIALOGUE);}
        if(trigger(23, 25, DOWN)) {teleport(DIALOGUE);}
    }

    private boolean trigger(int eventCol, int eventRow, String reqDirection) {
        boolean trigger = false;

        Player player =  scene.getPlayer();
        player.getHitbox().x = player.getWorldX() + player.getHitbox().x;
        player.getHitbox().y = player.getWorldY() + player.getHitbox().y;
        eventBox.x = eventCol * TILE_SIZE + eventBox.x;
        eventBox.y = eventRow * TILE_SIZE + eventBox.y;

        if(player.getHitbox().intersects(eventBox)) {
            if(player.getDirection().contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                trigger = true;
            }
        }

        player.getHitbox().x = player.getHitboxDefaultX();
        player.getHitbox().y = player.getHitboxDefaultY();
        eventBox.x = eventBoxDefaultX;
        eventBox.y = eventBoxDefaultY;

        return trigger;
    }

    private void damagePit(GameState gameState) {
        GameState.gameState = gameState;
        scene.getGui().setCurrentDialogue("You fall into a pit!");
        scene.getPlayer().lostLife();
    }

    private void healingPool(GameState gameState) {
        if(scene.getKeyInputs().isEnterPressed()) {
            GameState.gameState = gameState;
            scene.getGui().setCurrentDialogue("You life has been recovered!");
            scene.getPlayer().setLife(scene.getPlayer().getMaxLives());
        }
    }

    private void teleport(GameState gameState) {
        GameState.gameState = gameState;
        scene.getGui().setCurrentDialogue("Teleport!");
        scene.getPlayer().setWorldX(37* TILE_SIZE);
        scene.getPlayer().setWorldY(10* TILE_SIZE);
    }
}
