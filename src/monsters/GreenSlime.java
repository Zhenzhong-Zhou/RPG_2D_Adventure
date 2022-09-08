package monsters;

import entities.Entity;
import entities.Player;
import main.Scene;
import objects.*;

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
        defaultSpeed = 1;
        speed = defaultSpeed;
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
        Player player = scene.getPlayer();

        if(onPath) {
            // Check if it stops chasing
            checkStopChasingOrNot(player, 15, 100);

            // Search the direction to go
            searchPath(getGoalCol(player), getGoalRow(player));

            // Check if it shoots a projectile
            checkShootOrNot(200, 30);
        } else {
            // Check if it starts chasing
            checkStartChasingOrNot(player, 5, 100);
            // Get a random direction
            getRandomDirection();
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
    }

    public void checkDrop() {
        // CAST A DEAD
        int i = new Random().nextInt(125) + 1;

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
        if(i >= 100 && i < 125) {
            dropItem(new Potion_Red(scene));
        }
    }
}
