package objects;

import entities.Entity;
import entities.Projectile;
import main.Scene;

import static utilities.Constants.EntityConstant.FIREBALL;
import static utilities.LoadSave.*;

public class Fireball extends Projectile {
    private Scene scene;
    public Fireball(Scene scene) {
        super(scene);
        this.scene = scene;

        objectName = FIREBALL;
        speed = 5;
        maxLives = 80;
        life = maxLives;
        attack = 2;
        useCost = 1;
        alive = false;
        getFireballImage();
    }

    private void getFireballImage() {
        up1 = GetSpriteAtlas(FIREBALL_UP_1_IMAGE);
        up2 = GetSpriteAtlas(FIREBALL_UP_2_IMAGE);
        left1 = GetSpriteAtlas(FIREBALL_LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(FIREBALL_LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(FIREBALL_DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(FIREBALL_DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(FIREBALL_RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(FIREBALL_RIGHT_2_IMAGE);
    }

    public boolean hasEnergy(Entity user) {
        return user.getMana() >= useCost;
    }

    public void subtractEnergy(Entity user) {
        user.subtractMana(useCost);
    }
}
