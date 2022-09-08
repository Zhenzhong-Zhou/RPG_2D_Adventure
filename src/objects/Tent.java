package objects;

import entities.Entity;
import main.Scene;

import static main.GameState.SLEEP;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.REST;
import static utilities.Constants.EntityConstant.CONSUMABLE;
import static utilities.Constants.EntityConstant.TENT;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.TENT_IMAGE;

public class Tent extends Entity {
    public Tent(Scene scene) {
        super(scene);

        entityType = CONSUMABLE;
        objectName = TENT;
        down1 = GetSpriteAtlas(TENT_IMAGE);
        description = "[" + objectName + "]\nYou can sleep until \nnext morning.";
        price = 1000;
        stackable = true;
    }

    @Override
    public boolean use(Entity entity) {
        gameState = SLEEP;
        scene.getAudioManager().playEffect(REST);
        scene.getPlayer().setLife(scene.getPlayer().getMaxLives());
        scene.getPlayer().setMana(scene.getPlayer().getMaxMana());
        return true;
    }
}
