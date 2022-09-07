package utilities;

import entities.NPC_Merchant;
import entities.NPC_OldMan;
import main.Scene;
import monsters.GreenSlime;
import objects.Chest;
import objects.Door;
import objects.Key;
import tile_interactive.DryTree;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class AssetSetter {
    private final Scene scene;

    public AssetSetter(Scene scene) {
        this.scene = scene;
    }

    public void setObjects() {
        int mapNum = 0;
        int i = 0;
        scene.getGameObjects()[mapNum][i] = new Door(scene);
        scene.getGameObjects()[mapNum][i].setWorldX(14 * TILE_SIZE);
        scene.getGameObjects()[mapNum][i].setWorldY(28 * TILE_SIZE);
        i++;

        scene.getGameObjects()[mapNum][i] = new Chest(scene, new Key(scene));
        scene.getGameObjects()[mapNum][i].setWorldX(23 * TILE_SIZE);
        scene.getGameObjects()[mapNum][i].setWorldY(25 * TILE_SIZE);
    }

    public void setNPCs() {
        int map = 0;
        int i = 0;
        scene.getNPCs()[map][i] = new NPC_OldMan(scene);
        scene.getNPCs()[map][i].setWorldX(21 * TILE_SIZE);
        scene.getNPCs()[map][i].setWorldY(21 * TILE_SIZE);
        map++;

        scene.getNPCs()[map][i] = new NPC_Merchant(scene);
        scene.getNPCs()[map][i].setWorldX(12 * TILE_SIZE);
        scene.getNPCs()[map][i].setWorldY(7 * TILE_SIZE);
    }

    public void setMonsters() {
        int mapNum = 0;
        for(int j = 0; j < 7; j++) {
            scene.getMonsters()[mapNum][j] = new GreenSlime(scene);
            scene.getMonsters()[mapNum][j].setWorldX((20 + j) * TILE_SIZE);
            scene.getMonsters()[mapNum][j].setWorldY((35 + j) * TILE_SIZE);
        }
    }

    public void setInteractiveTile() {
        int mapNum = 0;
        for(int i = 0; i < 7; i++) {
            scene.getInteractiveTiles()[mapNum][i] = new DryTree(scene, (27 + i), 12);
        }
    }
}
