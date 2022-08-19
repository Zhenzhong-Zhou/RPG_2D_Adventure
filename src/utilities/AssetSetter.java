package utilities;

import entities.NPC_OldMan;
import main.Scene;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class AssetSetter {
    private final Scene scene;

    public AssetSetter(Scene scene) {
        this.scene = scene;
    }

    public void setObjects() {

    }

    public void setNPCs() {
        scene.getNPCs()[0] = new NPC_OldMan(scene);
        scene.getNPCs()[0].setWorldX(21 *TILE_SIZE);
        scene.getNPCs()[0].setWorldY(21 *TILE_SIZE);

        scene.getNPCs()[1] = new NPC_OldMan(scene);
        scene.getNPCs()[1].setWorldX(20 *TILE_SIZE);
        scene.getNPCs()[1].setWorldY(21 *TILE_SIZE);
    }
}
