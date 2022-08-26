package tile_interactive;

import entities.Entity;
import main.Scene;

import java.awt.*;

import static utilities.Constants.AudioManager.CUT_TREE;
import static utilities.Constants.EntityConstant.AXE;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.LoadSave.DRY_TREE_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class DryTree extends InteractiveTile{
    public DryTree(Scene scene, int col, int row) {
        super(scene);
        this.worldX = col * TILE_SIZE;
        this.worldY = row * TILE_SIZE;

        down1 = GetSpriteAtlas(DRY_TREE_IMAGE);
        destructible = true;
        life = 3;
    }

    public boolean isCorrectItem(Entity entity) {
        return entity.getCurrentWeapon().getEntityType() == AXE;
    }

    public void playEffect() {
        scene.getAudioManager().playEffect(CUT_TREE);
    }

    public InteractiveTile getDestroyedForm() {
        return new Trunk(scene,worldX/TILE_SIZE, worldY/TILE_SIZE);
    }

    public Color getParticleColor() {
        return new Color(65,50,30);
    }

    public int getParticleSize() {
        return 6;   // 6 pixels
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleMaxLives() {
        return 20;
    }
}
