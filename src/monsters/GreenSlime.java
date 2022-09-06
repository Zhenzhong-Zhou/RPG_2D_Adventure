package monsters;

import entities.Entity;
import main.Scene;
import objects.Coin_Bronze;
import objects.Heart;
import objects.ManaCrystal;
import objects.Rock;

import java.awt.*;
import java.util.Random;

import static utilities.Constants.EntityConstant.GREEN_SLIME;
import static utilities.Constants.EntityConstant.MONSTER;
import static utilities.Constants.SceneConstant.TILE_SIZE;
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

    public void update() {
        super.update();

        int xDistance = Math.abs(worldX - scene.getPlayer().getWorldX());
        int yDistance = Math.abs(worldY - scene.getPlayer().getWorldY());
        int tileDistance = (xDistance + yDistance) / TILE_SIZE;

        if(!onPath && tileDistance < 5) {
            int i = new Random().nextInt(100) + 1;
            if(i > 50) onPath = true;
        }
        //TODO: other rules
//        if(onPath && tileDistance > 20) {
//            onPath = false;
//        }
    }

    public void setAction() {
        if(onPath) {
            int goalCol = (scene.getPlayer().getWorldX() + scene.getPlayer().getHitbox().x) / TILE_SIZE;
            int goalRow = (scene.getPlayer().getWorldY() + scene.getPlayer().getHitbox().y) / TILE_SIZE;

            searchPath(goalCol, goalRow);

            int i = new Random().nextInt(200) + 1;
            if(i > 197 && ! projectile.isAlive() && shotAvailableCounter == 30) {
                projectile.set(worldX, worldY, direction, true, this);
                scene.getProjectileArrayList().add(projectile);
                shotAvailableCounter = 0;
            }
        } else {
            super.setAction();
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }

    public void checkDrop() {
        // CAST A DEAD
        int i = new Random().nextInt(100) + 1;

        // SET THE MONSTER DROP
        if(i < 50) {
            dropItem(new Coin_Bronze(scene));
        }
        if(i >= 50 && i < 75) {
            dropItem(new Heart(scene));
        }
        if(i >= 75 && i < 100) {
            dropItem(new ManaCrystal(scene));
        }
    }
}
