package main;

import static utilities.LoadSave.CreatedFolder;

public class Game {
    public Game() {
        CreatedFolder();

        Scene scene = new Scene();
        new Window(scene);
        scene.getConfig().loadConfig();

        scene.setupGame();

        scene.start();
    }
}
