package utilities;

import entities.Entity;
import entities.Player;
import main.Scene;

import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;

public class CollisionDetection {
    private final Scene scene;

    public CollisionDetection(Scene scene) {
        this.scene = scene;
    }

    private void entityPosition(Entity entity) {
        entity.getHitbox().x = entity.getWorldX() + entity.getHitbox().x;
        entity.getHitbox().y = entity.getWorldY() + entity.getHitbox().y;
    }

    private void entityDirection(Entity entity) {
        switch(entity.getDirection()) {
            case UP -> entity.getHitbox().y -= entity.getSpeed();
            case LEFT -> entity.getHitbox().x += entity.getSpeed();
            case DOWN -> entity.getHitbox().y += entity.getSpeed();
            case RIGHT -> entity.getHitbox().x -= entity.getSpeed();
        }
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getWorldX() + entity.getHitbox().x;
        int entityRightWorldX = entity.getWorldX() + entity.getHitbox().x + entity.getHitbox().width;
        int entityTopWorldY = entity.getWorldY() + entity.getHitbox().y;
        int entityBottomWorldY = entity.getWorldY() + entity.getHitbox().y + entity.getHitbox().height;

        int entityLeftCol = entityLeftWorldX / TILE_SIZE;
        int entityRightCol = entityRightWorldX / TILE_SIZE;
        int entityTopRow = entityTopWorldY / TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / TILE_SIZE;

        int tileNum1, tileNum2;
        switch(entity.getDirection()) {
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[entityLeftCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[entityRightCol][entityTopRow];
                boolean collision1 = scene.getLevelManager().getTileManager().getTiles().get(tileNum1).isCollision();
                boolean collision2 = scene.getLevelManager().getTileManager().getTiles().get(tileNum2).isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[entityLeftCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[entityLeftCol][entityBottomRow];
                boolean collision1 = scene.getLevelManager().getTileManager().getTiles().get(tileNum1).isCollision();
                boolean collision2 = scene.getLevelManager().getTileManager().getTiles().get(tileNum2).isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[entityLeftCol][entityBottomRow];
                tileNum2 = scene.getLevelManager().getTileId()[entityRightCol][entityBottomRow];
                boolean collision1 = scene.getLevelManager().getTileManager().getTiles().get(tileNum1).isCollision();
                boolean collision2 = scene.getLevelManager().getTileManager().getTiles().get(tileNum2).isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[entityRightCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[entityRightCol][entityBottomRow];
                boolean collision1 = scene.getLevelManager().getTileManager().getTiles().get(tileNum1).isCollision();
                boolean collision2 = scene.getLevelManager().getTileManager().getTiles().get(tileNum2).isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        Entity[] objects = scene.getGameObjects();
        for(int i = 0; i < objects.length; i++) {
            Entity object = objects[i];
            if(object != null) {
                // Get entity's hitbox position
                entityPosition(entity);

                // Get the object's hitbox position
                object.getHitbox().x = object.getWorldX() + object.getHitbox().x;
                object.getHitbox().y = object.getWorldY() + object.getHitbox().y;

                entityDirection(entity);

                if(entity.getHitbox().intersects(object.getHitbox())) {
                    if(object.isCollision()) {
                        entity.setCollision(true);
                    }
                    if(player) {
                        index = i;
                    }
                }

                entity.getHitbox().x = entity.getHitboxDefaultX();
                entity.getHitbox().y = entity.getHitboxDefaultY();
                object.getHitbox().x = object.getHitboxDefaultX();
                object.getHitbox().y = object.getHitboxDefaultY();
            }
        }
        return index;
    }

    // NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999;

        for(int i = 0; i < targets.length; i++) {
            Entity target = targets[i];
            if(target != null) {
                // Get entity's hitbox position
                entityPosition(entity);

                // Get the object's hitbox position
                target.getHitbox().x = target.getWorldX() + target.getHitbox().x;
                target.getHitbox().y = target.getWorldY() + target.getHitbox().y;

                entityDirection(entity);

                if(entity.getHitbox().intersects(target.getHitbox())) {
                    if(target != entity) {
                        entity.setCollision(true);
                        index = i;
                    }
                }

                entity.getHitbox().x = entity.getHitboxDefaultX();
                entity.getHitbox().y = entity.getHitboxDefaultY();
                target.getHitbox().x = target.getHitboxDefaultX();
                target.getHitbox().y = target.getHitboxDefaultY();
            }
        }
        return index;
    }

    public void checkPlayer(Entity entity) {
        // Get entity's hitbox position
        entityPosition(entity);

        Player player = scene.getPlayer();
        // Get the object's hitbox position
        player.getHitbox().x = player.getWorldX() + player.getHitbox().x;
        player.getHitbox().y = player.getWorldY() + player.getHitbox().y;

        entityDirection(entity);

        if(entity.getHitbox().intersects(player.getHitbox())) {
            entity.setCollision(true);
        }

        entity.getHitbox().x = entity.getHitboxDefaultX();
        entity.getHitbox().y = entity.getHitboxDefaultY();
        player.getHitbox().x = player.getHitboxDefaultX();
        player.getHitbox().y = player.getHitboxDefaultY();
    }
}
