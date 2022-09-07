package objects;

import entities.Entity;
import main.Scene;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.UNLOCK;
import static utilities.Constants.EntityConstant.*;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.KEY_IMAGE;

public class Key extends Entity {
    public Key(Scene scene) {
        super(scene);

        entityType = CONSUMABLE;
        objectName = KEY;
        down1 = GetSpriteAtlas(KEY_IMAGE);
        description = "[" + objectName + "]\nIt opens a door.";
        price = 100;
        stackable = true;
    }

    public boolean use(Entity entity) {
        gameState = DIALOGUE;

        int objectIndex = getDetected(entity, scene.getGameObjects(), DOOR);
        if(objectIndex != 999) {
            scene.getGui().setCurrentDialogue("You use the " + objectName + " and open the door.");
            scene.getAudioManager().playEffect(UNLOCK);
            scene.getGameObjects()[scene.currentMap][objectIndex] = null;
            return true;
        } else {
            scene.getGui().setCurrentDialogue("What are you doing?");
            return false;
        }
    }
}
