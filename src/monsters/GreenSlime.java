package monsters;

import entities.Entity;
import main.Scene;
import objects.Rock;

import java.awt.*;
import java.util.Random;

import static utilities.Constants.EntityConstant.GREEN_SLIME;
import static utilities.Constants.EntityConstant.MONSTER;
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
        entityType = MONSTER;
        objectName = GREEN_SLIME;
        speed = 1;
        maxLives = 4;
        life = maxLives;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new Rock(scene);
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

        int i = new Random().nextInt(100)+1;
        if(i > 99 && !projectile.isAlive() && shotAvailableCounter == 30) {
            projectile.set(worldX,worldY, direction,true, this);
            scene.getProjectileArrayList().add(projectile);
            shotAvailableCounter = 0;
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        direction = scene.getPlayer().getDirection();
    }
}
