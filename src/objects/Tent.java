package objects;

import entities.Entity;
import entities.Player;
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
        Player player = scene.getPlayer();
        gameState = SLEEP;
        scene.getAudioManager().stopSound();
        scene.getAudioManager().playEffect(REST);
        player.setLife(player.getMaxLives());
        player.setMana(player.getMaxMana());
        player.getSleepImage(down1);
        return true;
    }
}
