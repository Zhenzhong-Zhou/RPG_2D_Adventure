package environment;

import main.Scene;

import java.awt.*;

public class EnvironmentManager {
    private final Scene scene;
    private Lighting lighting;

    public EnvironmentManager(Scene scene) {
        this.scene = scene;
        lighting = new Lighting(scene);
    }

    public void update() {
        lighting.update();
    }

    public void draw(Graphics2D graphics2D) {
        lighting.draw(graphics2D);
    }

    public Lighting getLighting() {
        return lighting;
    }

    public void setLighting(Lighting lighting) {
        this.lighting = lighting;
    }
}
