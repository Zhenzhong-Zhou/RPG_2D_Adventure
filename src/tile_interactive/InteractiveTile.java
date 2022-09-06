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

    public void playEffect() {
    }

    public InteractiveTile getDestroyedForm() {
        return null;
    }

    public void update() {
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 20) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
}
