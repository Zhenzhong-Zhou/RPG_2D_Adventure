package objects;

import entities.Entity;
import entities.Player;
import main.Scene;

import java.awt.*;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.UNLOCK;
import static utilities.Constants.EntityConstant.CHEST;
import static utilities.Constants.EntityConstant.OBSTACLE;
import static utilities.LoadSave.*;

public class Chest extends Entity {
    private Entity loot;
    private boolean opened;

    public Chest(Scene scene, Entity loot) {
        super(scene);
        this.loot = loot;

        entityType = OBSTACLE;
        objectName = CHEST;
        up1 = GetSpriteAtlas(CHEST_IMAGE);
        up2 = GetSpriteAtlas(CHEST_OPEN_IMAGE);
        down1 = up1;
        collision = true;

        hitbox = new Rectangle(0, 16, 40, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public void interact() {
        gameState = DIALOGUE;
        if(!opened) {
            scene.getAudioManager().playEffect(UNLOCK);

            StringBuilder sb = new StringBuilder();
            sb.append("You open the chest and find a ").append(loot.getObjectName()).append("!");

            Player player = scene.getPlayer();
            if(player.getInventory().size() == player.getMaxInventorySize()) {
                sb.append("\n...But you cannot carry more items!");
            } else {
                sb.append("\nYou obtain the ").append(loot.getObjectName()).append("!");
                player.getInventory().add(loot);
                down1 = up2;
                opened = true;
            }
            scene.getGui().setCurrentDialogue(sb.toString());
        } else {
            scene.getGui().setCurrentDialogue("It's empty!");
        }
    }
}
