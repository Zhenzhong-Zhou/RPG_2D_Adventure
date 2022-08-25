package objects;

import entities.Entity;
import entities.Projectile;
import main.Scene;

import static utilities.Constants.EntityConstant.ROCK;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.ROCK_IMAGE;

public class Rock extends Projectile {
    private final Scene scene;

    public Rock(Scene scene) {
        super(scene);
        this.scene = scene;

        objectName = ROCK;
        speed = 8;
        maxLives = 80;
        life = maxLives;
        attack = 2;
        useCost = 1;
        alive = false;
        getFireballImage();
    }

    private void getFireballImage() {
        up1 = GetSpriteAtlas(ROCK_IMAGE);
        up2 = GetSpriteAtlas(ROCK_IMAGE);
        left1 = GetSpriteAtlas(ROCK_IMAGE);
        left2 = GetSpriteAtlas(ROCK_IMAGE);
        down1 = GetSpriteAtlas(ROCK_IMAGE);
        down2 = GetSpriteAtlas(ROCK_IMAGE);
        right1 = GetSpriteAtlas(ROCK_IMAGE);
        right2 = GetSpriteAtlas(ROCK_IMAGE);
    }

    public boolean hasEnergy(Entity user) {
        return user.getAmmo() >= useCost;
    }

    public void subtractEnergy(Entity user) {
        user.subtractAmmo(useCost);
    }
}
