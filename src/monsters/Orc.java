package monsters;

import entities.Entity;
import entities.Player;
import main.Scene;
import objects.*;

import java.awt.*;
import java.util.Random;

import static utilities.Constants.EntityConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.LoadSave.*;

public class Orc extends Entity {
    public Orc(Scene scene) {
        super(scene);

        hitbox = new Rectangle(4, 4, 40, 44);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackBox.width = TILE_SIZE;
        attackBox.height = TILE_SIZE;

        setDefaultValues();
        getOrcImage();
        getOrcAttackImage();
    }

    private void setDefaultValues() {
        entityType = MONSTER;
        objectName = ORC;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLives = 10;
        life = maxLives;
        attack = 8;
        defense = 2;
        exp = 10;
    }

    private void getOrcImage() {
        up1 = GetSpriteAtlas(ORC_UP_1_IMAGE);
        up2 = GetSpriteAtlas(ORC_UP_2_IMAGE);
        left1 = GetSpriteAtlas(ORC_LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(ORC_LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(ORC_DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(ORC_DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(ORC_RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(ORC_RIGHT_2_IMAGE);
    }

    private void getOrcAttackImage() {
        up1 = GetAttackImage(ORC_ATTACK_UP_1_IMAGE, TILE_SIZE, TILE_SIZE * 2);
        up2 = GetAttackImage(ORC_ATTACK_UP_2_IMAGE, TILE_SIZE, TILE_SIZE * 2);
        left1 = GetAttackImage(ORC_ATTACK_LEFT_1_IMAGE, TILE_SIZE * 2, TILE_SIZE);
        left2 = GetAttackImage(ORC_ATTACK_LEFT_2_IMAGE, TILE_SIZE * 2, TILE_SIZE);
        down1 = GetAttackImage(ORC_ATTACK_DOWN_1_IMAGE, TILE_SIZE, TILE_SIZE * 2);
        down2 = GetAttackImage(ORC_ATTACK_DOWN_2_IMAGE, TILE_SIZE, TILE_SIZE * 2);
        right1 = GetAttackImage(ORC_ATTACK_RIGHT_1_IMAGE, TILE_SIZE * 2, TILE_SIZE);
        right2 = GetAttackImage(ORC_ATTACK_RIGHT_2_IMAGE, TILE_SIZE * 2, TILE_SIZE);
    }

    public void setAction() {
        Player player = scene.getPlayer();

        if(onPath) {
            // Check if it stops chasing
            checkStopChasingOrNot(player, 15, 100);

            // Search the direction to go
            searchPath(getGoalCol(player), getGoalRow(player));
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
