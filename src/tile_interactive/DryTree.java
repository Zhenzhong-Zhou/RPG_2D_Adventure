package tile_interactive;

import entities.Entity;
import main.Scene;

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
    }

    public boolean isCorrectItem(Entity entity) {
        return entity.getCurrentWeapon().getEntityType() == AXE;
    }
}
