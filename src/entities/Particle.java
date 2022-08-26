package entities;

import main.Scene;

import java.awt.*;

import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;

public class Particle extends Entity{
    private Entity generator;
    private Color color;
    private int size, xd, yd;

    public Particle(Scene scene, Entity generator, Color color, int size, int speed, int maxLives, int xd, int yd) {
        super(scene);
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLives = maxLives;
        this.xd = xd;
        this.yd = yd;

        life = maxLives;
        int offset = (TILE_SIZE/2) - (size/2);
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;
    }

    public void update() {
        life--;

        worldX += xd*speed;
        worldY += yd*speed;

        if(life == 0) {
            alive = false;
        }
    }

    public void draw(Graphics2D graphics2D) {
        Player player = scene.getPlayer();
        int playerWorldX = player.getWorldX();
        int playerWorldY = player.getWorldY();
        int playerScreenX = player.getScreenX();
        int playerScreenY = player.getScreenY();

        int screenX = worldX - playerWorldX + playerScreenX;
        int screenY = worldY - playerWorldY + playerScreenY;

        int left = playerWorldX - playerScreenX;
        int right = playerWorldX + playerScreenX;
        int up = playerWorldY - playerScreenY;
        int down = playerWorldY + playerScreenY;

        // STOP MOVING CAMERA
        if(playerWorldX < playerScreenX) screenX = worldX;
        if(playerWorldY < playerScreenY) screenY = worldY;

        int rightOffset = SCENE_WIDTH - playerScreenX;
        if(rightOffset > WORLD_WIDTH - playerWorldX) screenX = SCENE_WIDTH - (WORLD_WIDTH - worldX);

        int bottomOffset = SCENE_HEIGHT - playerScreenY;
        if(bottomOffset > WORLD_HEIGHT - playerWorldY) screenY = SCENE_HEIGHT - (WORLD_HEIGHT - worldY);

        if(worldX + TILE_SIZE > left && worldX - TILE_SIZE < right && worldY + TILE_SIZE > up && worldY - TILE_SIZE < down) {
            graphics2D.setColor(color);
            graphics2D.fillRect(screenX,screenY,size,size);
        } else if(playerScreenX > playerWorldX ||       //TODO: need to fix later
                playerScreenY > playerWorldY ||
                rightOffset > WORLD_WIDTH - playerWorldX ||
                bottomOffset > WORLD_HEIGHT - playerWorldY) {
            graphics2D.setColor(color);
            graphics2D.fillRect(screenX,screenY,size,size);
        }
    }
}
