package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.AudioManager.POWER_UP;
import static utilities.Constants.EntityConstant.HEART;
import static utilities.Constants.EntityConstant.PICKUP;
import static utilities.LoadSave.*;

public class Heart extends Entity {
    public Heart(Scene scene) {
        super(scene);

        objectName = HEART;
        entityType = PICKUP;
        value = 2;
        left2 = GetSpriteAtlas(HEART_FULL);
        down1 = GetSpriteAtlas(HEART_FULL);
        down2 = GetSpriteAtlas(HEART_HALF);
        left1 = GetSpriteAtlas(HEART_BLANK);
    }

    public void use(Entity player) {
        scene.getAudioManager().playEffect(POWER_UP);
        scene.getGui().addMessage("Life + " + value);
        scene.getPlayer().gainLife(value);
    }
}
