package objects;

import entities.Entity;
import main.Scene;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.POWER_UP;
import static utilities.Constants.EntityConstant.CONSUMABLE;
import static utilities.Constants.EntityConstant.POTION_RED;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.RED_POTION_IMAGE;

public class Potion_Red extends Entity {
    public int value = 5;

    public Potion_Red(Scene scene) {
        super(scene);

        entityType = CONSUMABLE;
        objectName = POTION_RED;
        down1 = GetSpriteAtlas(RED_POTION_IMAGE);
        description = "[" + objectName + "]\nHeals your life by " + value + ".";
    }

    public void use(Entity player) {
        gameState = DIALOGUE;
        scene.getGui().setCurrentDialogue("You drink the " + objectName + "!\n" + "Your life has been recovered by " + value + ".");
        player.gainLife(value);
        if(player.getLife() > player.getMaxLives()) {
            player.setLife(player.getMaxLives());
        }
        scene.getAudioManager().playEffect(POWER_UP);
    }
}
