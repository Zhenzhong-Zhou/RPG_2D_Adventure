package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EquipmentConstant.NORMAL_SWORD;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.NORMAL_SWORD_IMAGE;

public class Sword_Normal extends Entity {
    public Sword_Normal(Scene scene) {
        super(scene);

        objectName = NORMAL_SWORD;
        down1 = GetSpriteAtlas(NORMAL_SWORD_IMAGE);
        attackValue = 1;
    }
}