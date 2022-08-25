package tile_interactive;

import entities.Entity;
import main.Scene;

public class InteractiveTile extends Entity {
    public boolean destructible = false;

    public InteractiveTile(Scene scene) {
        super(scene);
    }

    public boolean isCorrectItem(Entity entity) {
        return false;
    }

    public void update() {

    }
}
