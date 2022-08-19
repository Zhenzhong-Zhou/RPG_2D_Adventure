package utilities;

import entities.NPC_OldMan;
import main.Scene;
import objects.Door;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class AssetSetter {
    private final Scene scene;

    public AssetSetter(Scene scene) {
        this.scene = scene;
    }

    public void setObjects() {
        scene.getGameObjects()[0] = new Door(scene);
        scene.getGameObjects()[0].setWorldX(21 * TILE_SIZE);
        scene.getGameObjects()[0].setWorldY(22 * TILE_SIZE);

        scene.getGameObjects()[1] = new Door(scene);
        scene.getGameObjects()[1].setWorldX(23 * TILE_SIZE);
        scene.getGameObjects()[1].setWorldY(25 * TILE_SIZE);
    }

    public void setNPCs() {
        scene.getNPCs()[0] = new NPC_OldMan(scene);
        scene.getNPCs()[0].setWorldX(21 *TILE_SIZE);
        scene.getNPCs()[0].setWorldY(21 *TILE_SIZE);

        scene.getNPCs()[1] = new NPC_OldMan(scene);
        scene.getNPCs()[1].setWorldX(10 *TILE_SIZE);
        scene.getNPCs()[1].setWorldY(21 *TILE_SIZE);

        scene.getNPCs()[2] = new NPC_OldMan(scene);
        scene.getNPCs()[2].setWorldX(30 *TILE_SIZE);
        scene.getNPCs()[2].setWorldY(21 *TILE_SIZE);

        scene.getNPCs()[3] = new NPC_OldMan(scene);
        scene.getNPCs()[3].setWorldX(20 *TILE_SIZE);
        scene.getNPCs()[3].setWorldY(11 *TILE_SIZE);
    }
}
