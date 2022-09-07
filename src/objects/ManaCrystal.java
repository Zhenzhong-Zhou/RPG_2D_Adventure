package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.AudioManager.POWER_UP;
import static utilities.Constants.EntityConstant.MANA;
import static utilities.Constants.EntityConstant.PICKUP;
import static utilities.LoadSave.*;

public class ManaCrystal extends Entity {
    public ManaCrystal(Scene scene) {
        super(scene);

        objectName = MANA;
        entityType = PICKUP;
        value = 1;
        left1 = GetSpriteAtlas(MANA_FULL);
        down1 = GetSpriteAtlas(MANA_FULL);
        down2 = GetSpriteAtlas(MANA_BLANK);
    }

    public boolean use(Entity player) {
        scene.getAudioManager().playEffect(POWER_UP);
        scene.getGui().addMessage("Mana + " + value);
        scene.getPlayer().pickupMana(value);
        return true;
    }
}
