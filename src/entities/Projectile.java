package entities;

import main.Scene;

import static utilities.Constants.DirectionConstant.*;

public class Projectile extends Entity {
    private Entity user;

    public Projectile(Scene scene) {
        super(scene);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLives;
    }

    public void update() {
        if(user == scene.getPlayer()) {
            int monsterIndex = scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
            if(monsterIndex != 999) {
                scene.getPlayer().damageMonster(monsterIndex, attack);
                generateParticle(user.projectile, scene.getMonsters()[monsterIndex]);
                alive = false;
            }
        }

        if(user != scene.getPlayer()) {
            boolean interactPlayer = scene.getCollisionDetection().checkPlayer(this);
            if(! scene.getPlayer().invincible && interactPlayer) {
                damagePlayer(attack);
                generateParticle(user.projectile, scene.getPlayer());
                alive = false;
            }
        }
        switch(direction) {
            case UP -> worldY -= speed;
            case LEFT -> worldX -= speed;
            case DOWN -> worldY += speed;
            case RIGHT -> worldX += speed;
        }

        life--;
        if(life <= 0) {
            alive = false;
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public boolean hasEnergy(Entity user) {
        return false;
    }

    public void subtractEnergy(Entity user) {
    }
}
