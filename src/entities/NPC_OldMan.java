package entities;

import main.Scene;

import java.awt.*;
import java.util.Random;

import static utilities.Constants.DirectionConstant.*;
import static utilities.LoadSave.*;

public class NPC_OldMan extends Entity {
    public NPC_OldMan(Scene scene) {
        super(scene);

        hitbox = new Rectangle(0, 16, 48, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setDefaultValues();
        getOldManImage();
        setDialogues();
    }

    private void setDefaultValues() {
        speed = 1;
        direction = DOWN;
    }

    private void getOldManImage() {
        up1 = GetSpriteAtlas(OLD_MAN_UP_1_IMAGE);
        up2 = GetSpriteAtlas(OLD_MAN_UP_2_IMAGE);
        left1 = GetSpriteAtlas(OLD_MAN_LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(OLD_MAN_LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(OLD_MAN_DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(OLD_MAN_DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(OLD_MAN_RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(OLD_MAN_RIGHT_2_IMAGE);
    }

    protected void setDialogues() {
        dialogues[0] = "Hello, blue boy!";
        dialogues[1] = "Good luck!";
        dialogues[2] = "Bye!";
        dialogues[3] = "Test: more than one line -> \nwe will implement a dialogue system so we can talk to NPCs.";
    }

    public void setAction() {
        actionLockCounter++;
        if(actionLockCounter == 180) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;    // pick up a number from 1 to 100;
            if(i <= 25) direction = UP;
            if(i > 25 && i <= 50) direction = DOWN;
            if(i > 50 && i <= 75) direction = LEFT;
            if(i > 75) direction = RIGHT;
            actionLockCounter = 0;
        }
    }

    public void speak() {
        super.speak();
    }
}
