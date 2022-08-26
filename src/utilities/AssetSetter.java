package utilities;

import entities.NPC_OldMan;
import main.Scene;
import monsters.GreenSlime;
import objects.*;
import tile_interactive.DryTree;

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

        scene.getGameObjects()[i] = new Coin_Bronze(scene);
        scene.getGameObjects()[i].setWorldX(25 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(23 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new Coin_Bronze(scene);
        scene.getGameObjects()[i].setWorldX(24 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(23 * TILE_SIZE);
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

        scene.getGameObjects()[i] = new Heart(scene);
        scene.getGameObjects()[i].setWorldX(22 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(29 * TILE_SIZE);
        i++;

        scene.getGameObjects()[i] = new ManaCrystal(scene);
        scene.getGameObjects()[i].setWorldX(22 * TILE_SIZE);
        scene.getGameObjects()[i].setWorldY(31 * TILE_SIZE);
        i++;
    }

    public void setNPCs() {
        scene.getNPCs()[0] = new NPC_OldMan(scene);
        scene.getNPCs()[0].setWorldX(21 * TILE_SIZE);
        scene.getNPCs()[0].setWorldY(21 * TILE_SIZE);
    }

    public void setMonsters() {
        for(int j = 0; j < 7; j++) {
            scene.getMonsters()[j] = new GreenSlime(scene);
            scene.getMonsters()[j].setWorldX((19 + j) * TILE_SIZE);
            scene.getMonsters()[j].setWorldY((35 + j) * TILE_SIZE);
        }
    }

    public void setInteractiveTile() {
        for(int i = 0; i < 7; i++) {
            scene.getInteractiveTiles()[i] = new DryTree(scene, (27 + i), 11);
        }
    }
}
