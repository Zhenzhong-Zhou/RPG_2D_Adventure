package tile_interactive;

import entities.Entity;
import main.Scene;

public class InteractiveTile extends Entity {
    public boolean destructible = false;

    public InteractiveTile(Scene scene, int col, int row) {
        super(scene);
    }

    public void update() {

    }
}
