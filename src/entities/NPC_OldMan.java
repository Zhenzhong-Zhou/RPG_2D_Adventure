package entities;

import main.Scene;

import java.awt.*;

import static utilities.Constants.DirectionConstant.DOWN;
import static utilities.LoadSave.*;

public class NPC_OldMan extends Entity {
    public NPC_OldMan(Scene scene) {
        super(scene);

        hitbox = new Rectangle(8, 16, 32, 32);
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
        super.setAction();
    }

    public void speak() {
        super.speak();
    }
}
