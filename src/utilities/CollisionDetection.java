package utilities;

import entities.Entity;
import entities.Player;
import main.Scene;
import tiles.Tile;

import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;

public class CollisionDetection {
    private final Scene scene;

    public CollisionDetection(Scene scene) {
        this.scene = scene;
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
        Tile[] tiles = scene.getLevelManager().getTileManager().getTiles();

        // Use a temporal direction when it's being knockBacked
        String direction = entity.getDirection();
        if(entity.isKnockBack()) direction = entity.getKnockBackDirection();

        switch(direction) {
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[scene.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[scene.currentMap][entityRightCol][entityTopRow];
                boolean collision1 = tiles[tileNum1].isCollision();
                boolean collision2 = tiles[tileNum2].isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[scene.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[scene.currentMap][entityLeftCol][entityBottomRow];
                boolean collision1 = tiles[tileNum1].isCollision();
                boolean collision2 = tiles[tileNum2].isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[scene.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = scene.getLevelManager().getTileId()[scene.currentMap][entityRightCol][entityBottomRow];
                boolean collision1 = tiles[tileNum1].isCollision();
                boolean collision2 = tiles[tileNum2].isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / TILE_SIZE;
                tileNum1 = scene.getLevelManager().getTileId()[scene.currentMap][entityRightCol][entityTopRow];
                tileNum2 = scene.getLevelManager().getTileId()[scene.currentMap][entityRightCol][entityBottomRow];
                boolean collision1 = tiles[tileNum1].isCollision();
                boolean collision2 = tiles[tileNum2].isCollision();

                if(collision1 || collision2) {
                    entity.setCollision(true);
                }
            }
        }
    }

    private void entityPosition(Entity entity) {
        entity.getHitbox().x = entity.getWorldX() + entity.getHitbox().x;
        entity.getHitbox().y = entity.getWorldY() + entity.getHitbox().y;
    }

    private void entityDirection(Entity entity, String direction) {
        switch(direction) {
            case UP -> entity.getHitbox().y -= entity.getSpeed();
            case LEFT -> entity.getHitbox().x -= entity.getSpeed();
            case DOWN -> entity.getHitbox().y += entity.getSpeed();
            case RIGHT -> entity.getHitbox().x += entity.getSpeed();
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        Entity[][] objects = scene.getGameObjects();
        for(int i = 0; i < objects[1].length; i++) {
            Entity object = objects[scene.currentMap][i];
            if(object != null) {
                // Get entity's hitbox position
                entityPosition(entity);

                // Get the object's hitbox position
                object.getHitbox().x = object.getWorldX() + object.getHitbox().x;
                object.getHitbox().y = object.getWorldY() + object.getHitbox().y;

                entityDirection(entity, entity.getDirection());

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
    public int checkEntity(Entity entity, Entity[][] targets) {
        int index = 999;

        // Use a temporal direction when it's being knockBacked
        String direction = entity.getDirection();
        if(entity.isKnockBack()) direction = entity.getKnockBackDirection();

        for(int i = 0; i < targets[1].length; i++) {
            Entity target = targets[scene.currentMap][i];
            if(target != null) {
                // Get entity's hitbox position
                entityPosition(entity);

                // Get the object's hitbox position
                target.getHitbox().x = target.getWorldX() + target.getHitbox().x;
                target.getHitbox().y = target.getWorldY() + target.getHitbox().y;

                entityDirection(entity, direction);

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

    public boolean checkPlayer(Entity entity) {
        boolean interactPlayer = false;
        // Get entity's hitbox position
        entityPosition(entity);

        Player player = scene.getPlayer();
        // Get the object's hitbox position
        player.getHitbox().x = player.getWorldX() + player.getHitbox().x;
        player.getHitbox().y = player.getWorldY() + player.getHitbox().y;

        entityDirection(entity, entity.getDirection());

        if(entity.getHitbox().intersects(player.getHitbox())) {
            entity.setCollision(true);
            interactPlayer = true;
        }

        entity.getHitbox().x = entity.getHitboxDefaultX();
        entity.getHitbox().y = entity.getHitboxDefaultY();
        player.getHitbox().x = player.getHitboxDefaultX();
        player.getHitbox().y = player.getHitboxDefaultY();

        return interactPlayer;
    }
}
