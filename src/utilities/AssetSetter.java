package utilities;

import main.Scene;
import objects.Boot;
import objects.Chest;
import objects.Door;
import objects.Key;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class AssetSetter {
    private final Scene scene;

    public AssetSetter(Scene scene) {
        this.scene = scene;
    }

    public void setObject() {
        scene.getGameObject()[0] = new Key();
        scene.getGameObject()[0].setWorldX(23 * TILE_SIZE);
        scene.getGameObject()[0].setWorldY(7 * TILE_SIZE);

        scene.getGameObject()[1] = new Key();
        scene.getGameObject()[1].setWorldX(23 * TILE_SIZE);
        scene.getGameObject()[1].setWorldY(39 * TILE_SIZE);

        scene.getGameObject()[2] = new Key();
        scene.getGameObject()[2].setWorldX(38 * TILE_SIZE);
        scene.getGameObject()[2].setWorldY(9 * TILE_SIZE);

        scene.getGameObject()[3] = new Door();
        scene.getGameObject()[3].setWorldX(12 * TILE_SIZE);
        scene.getGameObject()[3].setWorldY(22 * TILE_SIZE);

        scene.getGameObject()[4] = new Door();
        scene.getGameObject()[4].setWorldX(8 * TILE_SIZE);
        scene.getGameObject()[4].setWorldY(28 * TILE_SIZE);

        scene.getGameObject()[5] = new Door();
        scene.getGameObject()[5].setWorldX(10 * TILE_SIZE);
        scene.getGameObject()[5].setWorldY(11 * TILE_SIZE);

        scene.getGameObject()[6] = new Chest();
        scene.getGameObject()[6].setWorldX(10 * TILE_SIZE);
        scene.getGameObject()[6].setWorldY(8 * TILE_SIZE);

        scene.getGameObject()[7] = new Boot();
        scene.getGameObject()[7].setWorldX(36 * TILE_SIZE);
        scene.getGameObject()[7].setWorldY(41 * TILE_SIZE);
    }
}
