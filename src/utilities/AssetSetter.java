package utilities;

import entities.NPC_OldMan;
import main.Scene;
import monsters.GreenSlime;
import objects.Axe;
import objects.Key;
import objects.Potion_Red;
import objects.Shield_Blue;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class AssetSetter {
    private final Scene scene;

    public AssetSetter(Scene scene) {
        this.scene = scene;
    }

    public void setObjects() {
        int i = 0;
        scene.getGameObjects()[i] = new Key(scene);
        scene.getGameObjects()[i].setWorldX(25 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(19 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Key(scene);
        scene.getGameObjects()[i].setWorldX(33 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(21 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Key(scene);
        scene.getGameObjects()[i].setWorldX(35 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(21 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Axe(scene);
        scene.getGameObjects()[i].setWorldX(21 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(19 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Shield_Blue(scene);
        scene.getGameObjects()[i].setWorldX(28 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(20 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Potion_Red(scene);
        scene.getGameObjects()[i].setWorldX(22 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(30 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Potion_Red(scene);
        scene.getGameObjects()[i].setWorldX(22 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(40 * TILE_SIZE);
        i++;
    }

    public void setNPCs() {
        scene.getNPCs()[0] = new NPC_OldMan(scene);
        scene.getNPCs()[0].setWorldX(21 * TILE_SIZE);
        scene.getNPCs()[0].setWorldY(21 * TILE_SIZE);
    }

    public void setMonsters() {
        for(int j =0; j < 7; j++) {
            scene.getMonsters()[j] = new GreenSlime(scene);
            scene.getMonsters()[j].setWorldX((19+j) * TILE_SIZE);
            scene.getMonsters()[j].setWorldY((35+j) * TILE_SIZE);
        }
    }
}
