package environment;

import main.Scene;

import java.awt.*;

public class EnvironmentManager {
    private final Scene scene;
    private final Lighting lighting;

    public EnvironmentManager(Scene scene) {
        this.scene = scene;
        lighting = new Lighting(scene, 500);
    }

    public void setup() {
//        lighting = new Lighting(scene, 700);
    }

    public void draw(Graphics2D graphics2D) {
        lighting.draw(graphics2D);
    }
}
