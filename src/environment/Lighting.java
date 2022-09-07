package environment;

import entities.Player;
import main.Scene;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilities.Constants.SceneConstant.*;

public class Lighting {
    private Scene scene;
    private int circleSize;
    private BufferedImage darknessFilter;

    public Lighting(Scene scene, int circleSize) {
        this.scene = scene;
        this.circleSize = circleSize;

        // Create a buffered image
        darknessFilter = new BufferedImage(SCENE_WIDTH, SCENE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

        // Get the center x and y of the light circle
        Player player = scene.getPlayer();
        int centerX = player.getScreenX() + (TILE_SIZE) /2;
        int centerY = player.getScreenY() + (TILE_SIZE) /2;
        // Create a gradation effect
        Color[] color = new Color[12];
        float[] fraction = new float[12];

        color[0] = new Color(0,0,0,0.1f);
        color[1] = new Color(0,0,0,0.42f);
        color[2] = new Color(0,0,0,0.52f);
        color[3] = new Color(0,0,0,0.61f);
        color[4] = new Color(0,0,0,0.69f);
        color[5] = new Color(0,0,0,0.76f);
        color[6] = new Color(0,0,0,0.82f);
        color[7] = new Color(0,0,0,0.87f);
        color[8] = new Color(0,0,0,0.91f);
        color[9] = new Color(0,0,0,0.94f);
        color[10] = new Color(0,0,0,0.96f);
        color[11] = new Color(0,0,0,0.98f);

        fraction[0] = 0f;
        fraction[1] = 0.4f;
        fraction[2] = 0.5f;
        fraction[3] = 0.6f;
        fraction[4] = 0.65f;
        fraction[5] = 0.7f;
        fraction[6] = 0.75f;
        fraction[7] = 0.8f;
        fraction[8] = 0.85f;
        fraction[9] = 0.9f;
        fraction[10] = 0.95f;
        fraction[11] = 1f;

        // Create a gradation paint settings
        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (circleSize/2.0f), fraction, color);

        // Set the gradient data on graphics2D
        graphics2D.setPaint(gPaint);

        // Draw the screen rectangle without the light circle area
        graphics2D.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        graphics2D.dispose();
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(darknessFilter, 0,0,null);
    }
}
