package environment;

import entities.Player;
import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilities.Constants.EnvironmentConstant.*;
import static utilities.Constants.SceneConstant.*;

public class Lighting {
    private final Scene scene;
    private BufferedImage darknessFilter;
    private int dayCounter;
    private float filterAlpha;
    private int dayState = DAY;

    public Lighting(Scene scene) {
        this.scene = scene;

        setLightSource();
    }

    private void setLightSource() {
        // Create a buffered image
        darknessFilter = new BufferedImage(SCENE_WIDTH, SCENE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

        Player player = scene.getPlayer();
        float r =0, g=0, b =0.11f;
        if(player.getCurrentLight() == null) {
            graphics2D.setColor(new Color(r,g,b,0.98f));
        } else {
            // Get the center x and y of the light circle
            int centerX = player.getScreenX() + (TILE_SIZE) / 2;
            int centerY = player.getScreenY() + (TILE_SIZE) / 2;

            // Create a gradation effect within the light circle
            Color[] color = new Color[12];
            float[] fraction = new float[12];

            int i= 0;
            color[i] = new Color(r,g,b, 0.1f); i++;
            color[i] = new Color(r,g,b, 0.42f);i++;
            color[i] = new Color(r,g,b, 0.52f);i++;
            color[i] = new Color(r,g,b, 0.61f);i++;
            color[i] = new Color(r,g,b, 0.69f);i++;
            color[i] = new Color(r,g,b, 0.76f);i++;
            color[i] = new Color(r,g,b, 0.82f);i++;
            color[i] = new Color(r,g,b, 0.87f);i++;
            color[i] = new Color(r,g,b, 0.91f);i++;
            color[i] = new Color(r,g,b, 0.94f);i++;
            color[i] = new Color(r,g,b, 0.96f);i++;
            color[i] = new Color(r,g,b, 0.98f);

            i = 0;
            fraction[i] = 0f;i++;
            fraction[i] = 0.4f;i++;
            fraction[i] = 0.5f;i++;
            fraction[i] = 0.6f;i++;
            fraction[i] = 0.65f;i++;
            fraction[i] = 0.7f;i++;
            fraction[i] = 0.75f;i++;
            fraction[i] = 0.8f;i++;
            fraction[i] = 0.85f;i++;
            fraction[i] = 0.9f;i++;
            fraction[i] = 0.95f;i++;
            fraction[i] = 1f;

            // Create a gradation paint settings
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, player.getCurrentLight().getLightRadius(), fraction, color);

            // Set the gradient data on graphics2D
            graphics2D.setPaint(gPaint);
        }
        // Draw the screen rectangle without the light circle area
        graphics2D.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        graphics2D.dispose();
    }

    public void update() {
        Player player = scene.getPlayer();
        if(player.isLightUpdated()) {
            setLightSource();
            player.setLightUpdated(false);
        }

        // Check the state of the day
        int framePerSec = 60;
        int day = 3600 * framePerSec;
        int night = 1800 * framePerSec;
        float transitionSpeed = 0.00001f;
        switch(dayState) {
            case DAY -> {
                dayCounter++;
                if(dayCounter > day) {
                    dayState = DUSK;
                    dayCounter = 0;
                }
            }
            case DUSK -> {
                filterAlpha+=transitionSpeed;
                if(filterAlpha>1f) {
                    filterAlpha = 1f;
                    dayState = NIGHT;
                }
            }
            case NIGHT -> {
                dayCounter++;
                if(dayCounter > night) {
                    dayState = DAWN;
                    dayCounter = 0;
                }
            }
            case DAWN -> {
                filterAlpha -= transitionSpeed;
                if(filterAlpha<0) {
                    filterAlpha = 0;
                    dayState = DAY;
                }
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        graphics2D.drawImage(darknessFilter, 0, 0, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG
        String currentDayState = "";
        switch(dayState) {
            case DAY -> currentDayState = DAY_STATE;
            case DUSK -> currentDayState = DUSK_STATE;
            case NIGHT -> currentDayState = NIGHT_STATE;
            case DAWN -> currentDayState = DAWN_STATE;
        }
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.BOLD, 50F));
        graphics2D.drawString(currentDayState, 21*TILE_SIZE, (int) (16.5*TILE_SIZE));
    }
}
