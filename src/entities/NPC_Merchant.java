package entities;

import main.GameState;
import main.Scene;
import objects.*;

import java.awt.*;

import static main.GameState.*;
import static utilities.Constants.DirectionConstant.DOWN;
import static utilities.LoadSave.*;

public class NPC_Merchant extends Entity {
    public NPC_Merchant(Scene scene) {
        super(scene);

        hitbox = new Rectangle(8, 16, 32, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setDefaultValues();
        getOldManImage();
        setDialogues();
        setItem();
    }

    private void setDefaultValues() {
        speed = 1;
        direction = DOWN;
    }

    private void getOldManImage() {
        up1 = GetSpriteAtlas(MERCHANT_DOWN_1_IMAGE);
        up2 = GetSpriteAtlas(MERCHANT_DOWN_2_IMAGE);
        left1 = GetSpriteAtlas(MERCHANT_DOWN_1_IMAGE);
        left2 = GetSpriteAtlas(MERCHANT_DOWN_2_IMAGE);
        down1 = GetSpriteAtlas(MERCHANT_DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(MERCHANT_DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(MERCHANT_DOWN_1_IMAGE);
        right2 = GetSpriteAtlas(MERCHANT_DOWN_2_IMAGE);
    }

    protected void setDialogues() {
        dialogues[0] = "He he, so you found me.\nI have some good stuffs.\nDo you want to trade?";
    }

    public void speak() {
        super.speak();
        gameState = TRADE;
        scene.getGui().setNpc(this);
    }

    public void setItem() {
        inventory.add(new Potion_Red(scene));
        inventory.add(new Key(scene));
        inventory.add(new Sword_Normal(scene));
        inventory.add(new Axe(scene));
        inventory.add(new Shield_Wood(scene));
        inventory.add(new Shield_Blue(scene));
    }
}
