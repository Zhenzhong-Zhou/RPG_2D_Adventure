package objects;

import entities.Entity;
import main.Scene;

import java.awt.*;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.EntityConstant.DOOR;
import static utilities.Constants.EntityConstant.OBSTACLE;
import static utilities.LoadSave.DOOR_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Door extends Entity {
    public Door(Scene scene) {
        super(scene);

        entityType = OBSTACLE;
        objectName = DOOR;
        down1 = GetSpriteAtlas(DOOR_IMAGE);
        collision = true;

        hitbox = new Rectangle(0, 16, 48, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public void interact() {
        gameState = DIALOGUE;
        scene.getGui().setCurrentDialogue("You need a key to open a door!");
    }
}
