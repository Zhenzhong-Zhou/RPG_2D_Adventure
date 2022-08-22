package monsters;

import entities.Entity;
import main.Scene;

import java.awt.*;

import static utilities.Constants.MonsterConstant.GREEN_SLIME;
import static utilities.LoadSave.*;

public class GreenSlime extends Entity {
    public GreenSlime(Scene scene) {
        super(scene);

        hitbox = new Rectangle(3, 18, 42, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setDefaultValues();
        getGreenSlimeImage();
    }

    private void setDefaultValues() {
        entityType = 2;
        objectName = GREEN_SLIME;
        speed = 1;
        maxLives = 4;
        life = maxLives;
        attack = 5;
        defense = 0;
    }

    private void getGreenSlimeImage() {
        up1 = GetSpriteAtlas(GREEN_SLIME_DOWN_1_IMAGE);
        up2 = GetSpriteAtlas(GREEN_SLIME_DOWN_2_IMAGE);
        left1 = GetSpriteAtlas(GREEN_SLIME_DOWN_1_IMAGE);
        left2 = GetSpriteAtlas(GREEN_SLIME_DOWN_2_IMAGE);
        down1 = GetSpriteAtlas(GREEN_SLIME_DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(GREEN_SLIME_DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(GREEN_SLIME_DOWN_1_IMAGE);
        right2 = GetSpriteAtlas(GREEN_SLIME_DOWN_2_IMAGE);
    }

    public void setAction() {
        super.setAction();
    }

    public void damageReaction() {
        actionLockCounter = 0;
        direction = scene.getPlayer().getDirection();
    }
}
