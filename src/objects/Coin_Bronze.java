package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.AudioManager.COIN;
import static utilities.Constants.EntityConstant.BRONZE_COIN;
import static utilities.Constants.EntityConstant.PICKUP;
import static utilities.LoadSave.COIN_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Coin_Bronze extends Entity {
    public Coin_Bronze(Scene scene) {
        super(scene);

        objectName = BRONZE_COIN;
        entityType = PICKUP;
        value = 1;
        down1 = GetSpriteAtlas(COIN_IMAGE);
    }

    public boolean use(Entity player) {
        scene.getAudioManager().playEffect(COIN);
        scene.getGui().addMessage("Coin + " + value);
        scene.getPlayer().pickupCoin(value);
        return true;
    }
}
