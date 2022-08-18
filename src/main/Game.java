package main;

public class Game {
    public Game() {
        Scene scene = new Scene();
        new Window(scene);

        scene.start();
    }
}
