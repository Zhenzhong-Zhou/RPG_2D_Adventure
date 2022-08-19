package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.ObjectConstant.HEART;
import static utilities.LoadSave.*;

public class Heart extends Entity {
    public Heart(Scene scene) {
        super(scene);
        objectName = HEART;
        down1 = GetSpriteAtlas(HEART_FULL);
        down2 = GetSpriteAtlas(HEART_HALF);
        left1 = GetSpriteAtlas(HEART_BLANK);
    }
}
