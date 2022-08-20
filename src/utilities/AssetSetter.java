package utilities;

import entities.NPC_OldMan;
import main.Scene;
import monsters.GreenSlime;

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
        scene.getNPCs()[0].setWorldX(21 * TILE_SIZE);
        scene.getNPCs()[0].setWorldY(21 * TILE_SIZE);
    }

    public void setMonsters() {
        scene.getMonsters()[0] = new GreenSlime(scene);
        scene.getMonsters()[0].setWorldX(23 * TILE_SIZE);
        scene.getMonsters()[0].setWorldY(36 * TILE_SIZE);

        scene.getMonsters()[1] = new GreenSlime(scene);
        scene.getMonsters()[1].setWorldX(23 * TILE_SIZE);
        scene.getMonsters()[1].setWorldY(37 * TILE_SIZE);
    }
}
