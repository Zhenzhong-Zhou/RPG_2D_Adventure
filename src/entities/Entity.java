package entities;

import main.Scene;
import tile_interactive.InteractiveTile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static utilities.Constants.AudioManager.RECEIVED_DAMAGE;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.EntityConstant.MONSTER;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;

public abstract class Entity {
    protected final ArrayList<Entity> inventory = new ArrayList<>();
    protected final int maxInventorySize = 25;
    protected Scene scene;
    protected int worldX, worldY, speed;
    protected BufferedImage up1, up2, left1, left2, down1, down2, right1, right2;
    protected BufferedImage attackUp1, attackUp2, attackLeft1, attackLeft2, attackDown1, attackDown2, attackRight1, attackRight2;
    protected String direction = DOWN;
    protected int spriteCounter;
    protected int spriteNum = 1;
    protected int animationSpeed = 20;
    protected Rectangle hitbox;
    protected int hitboxDefaultX, hitboxDefaultY;
    protected boolean collision;
    protected int actionLockCounter;
    protected String[] dialogues = new String[20];
    protected int dialogueIndex = 0;
    protected String objectName;
    protected int defaultSpeed;
    protected int maxLives, life;
    protected int maxMana, mana;
    protected int ammo;
    protected boolean invincible;
    protected int invincibleCounter;
    protected int entityType;
    protected boolean attacking;
    protected Rectangle attackBox;
    protected boolean alive = true;
    protected boolean dead;
    protected int deadCounter;
    protected boolean hpBarOn;
    protected int hpBarCounter;
    protected int level;
    protected int strength;
    protected int dexterity;
    protected int attack;
    protected int defense;
    protected int exp;
    protected int nextLevelExp;
    protected int coin;
    protected int value;
    protected Entity currentWeapon;
    protected Entity currentShield;
    protected Entity currentLight;
    protected Entity attacker;
    protected Projectile projectile;
    protected int attackValue;
    protected int defenseValue;
    protected String description = "";
    protected int useCost;
    protected int shotAvailableCounter;
    protected int price;
    protected boolean onPath;
    protected boolean knockBack;
    protected int knockBackCounter;
    protected int knockBackPower;
    protected boolean stackable;
    protected int amount = 1;
    protected int lightRadius;
    protected String knockBackDirection;

    public Entity(Scene scene) {
        this.scene = scene;

        hitbox = new Rectangle(0, 0, TILE_SIZE, TILE_SIZE);
        attackBox = new Rectangle();
    }

    protected void setAction() {
        getRandomDirection();
    }

    protected void damageReaction() {
    }

    public void speak() {
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        } else {
            scene.getGui().setCurrentDialogue(dialogues[dialogueIndex]);
            dialogueIndex++;
        }
        switch(scene.getPlayer().direction) {
            case UP -> direction = DOWN;
            case LEFT -> direction = RIGHT;
            case DOWN -> direction = UP;
            case RIGHT -> direction = LEFT;
        }
    }

    public boolean use(Entity entity) {
        return false;
    }

    public void checkDrop() {
    }

    public void interact() {
    }

    public void dropItem(Entity droppedItem) {
        for(int i = 0; i < scene.getGameObjects()[1].length; i++) {
            Entity[] objects = scene.getGameObjects()[scene.currentMap];
            if(objects[i] == null) {
                objects[i] = droppedItem;
                objects[i].worldX = worldX; // the dead monster's worldX
                objects[i].worldY = worldY;
                break;
            }
        }
    }

    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLives = generator.getParticleMaxLives();

        Particle particle1 = new Particle(scene, target, color, size, speed, maxLives, - 2, - 1);
        Particle particle2 = new Particle(scene, target, color, size, speed, maxLives, 2, - 1);
        Particle particle3 = new Particle(scene, target, color, size, speed, maxLives, - 2, 1);
        Particle particle4 = new Particle(scene, target, color, size, speed, maxLives, 2, 1);
        scene.getParticleArrayList().add(particle1);
        scene.getParticleArrayList().add(particle2);
        scene.getParticleArrayList().add(particle3);
        scene.getParticleArrayList().add(particle4);
    }

    protected void checkCollision() {
        collision = false;
        scene.getCollisionDetection().checkTile(this);

        scene.getCollisionDetection().checkObject(this, false);
        scene.getCollisionDetection().checkEntity(this, scene.getNPCs());
        scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
        scene.getCollisionDetection().checkEntity(this, scene.getInteractiveTiles());
        boolean interactPlayer = scene.getCollisionDetection().checkPlayer(this);

        if(this.entityType == MONSTER && interactPlayer) {
            damagePlayer(attack);
        }
    }

    public void update() {
        if(knockBack) {
            checkCollision();
            if(collision) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
            Direction(knockBackDirection);

            knockBackCounter++;
            if(knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        } else if(attacking) {
            attacking();
        } else {
            setAction();
            checkCollision();
            playerCanMove();
            updateAnimation();
        }

        invincibleCounter();

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    private void Direction(String direction) {
        if(! collision) {
            switch(direction) {
                case UP -> worldY -= speed;
                case LEFT -> worldX -= speed;
                case DOWN -> worldY += speed;
                case RIGHT -> worldX += speed;
            }
        }
    }

    protected void attacking() {
        spriteCounter++;
        if(spriteCounter <= 5) spriteNum = 1;
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // Save the current worldX, worldY, hitbox
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int hitboxWidth = hitbox.width;
            int hitboxHeight = hitbox.height;

            // Adjust player's worldX/Y for the attackBox
            switch(direction) {
                case UP -> worldY -= attackBox.height;
                case LEFT -> worldX -= attackBox.width;
                case DOWN -> worldY += attackBox.height;
                case RIGHT -> worldX += attackBox.width;
            }

            // attackBox becomes hitbox
            hitbox.width = attackBox.width;
            hitbox.height = attackBox.height;

            if(entityType == MONSTER) {
                if(scene.getCollisionDetection().checkPlayer(this)) {
                    damagePlayer(attack);
                }
            } else {
                Player player = scene.getPlayer();
                // Check monster collision with the updated worldX/Y and hitbox
                int monsterIndex = scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
                player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                // CHECK INTERACTIVE TILE COLLISION
                int interactiveTileIndex = scene.getCollisionDetection().checkEntity(this, scene.getInteractiveTiles());
                player.damageInteractiveTile(interactiveTileIndex);

                // CHECK PROJECTILE COLLISION
                int projectileIndex = scene.getCollisionDetection().checkEntity(this, scene.getProjectiles());
                player.damageProjectile(projectileIndex);
            }

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
        }
        if(spriteCounter > 35) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    protected void damagePlayer(int attack) {
        if(! scene.getPlayer().invincible) {
            // Player get damaged
            scene.getAudioManager().playEffect(RECEIVED_DAMAGE);
            int damage = attack - scene.getPlayer().defense;
            if(damage < 0) {
                damage = 0;
            }
            scene.getPlayer().life -= damage;
            scene.getGui().addMessage("-" + damage + " damage!");
            scene.getPlayer().invincible = true;
        }
    }

    protected void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }

    protected void playerCanMove() {
        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        Direction(direction);
    }

    protected void updateAnimation() {
        spriteCounter++;
        if(spriteCounter >= animationSpeed) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    protected void invincibleCounter() {
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        drawAnimation(graphics2D);
    }

    protected BufferedImage animate() {
        BufferedImage image = null;

        int screenX = scene.getPlayer().getScreenX();
        int screenY = scene.getPlayer().getScreenY();
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) {
            case UP -> {
                if(! attacking) {
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;
                }
                if(attacking) {
                    tempScreenY = screenY - TILE_SIZE;
                    if(spriteNum == 1) image = attackUp1;
                    if(spriteNum == 2) image = attackUp2;
                }
            }
            case LEFT -> {
                if(! attacking) {
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                }
                if(attacking) {
                    tempScreenX = screenX - TILE_SIZE;
                    if(spriteNum == 1) image = attackLeft1;
                    if(spriteNum == 2) image = attackLeft2;
                }
            }
            case DOWN -> {
                if(! attacking) {
                    if(spriteNum == 1) image = down1;
                    if(spriteNum == 2) image = down2;
                }
                if(attacking) {
                    if(spriteNum == 1) image = attackDown1;
                    if(spriteNum == 2) image = attackDown2;
                }
            }
            case RIGHT -> {
                if(! attacking) {
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                }
                if(attacking) {
                    if(spriteNum == 1) image = attackRight1;
                    if(spriteNum == 2) image = attackRight2;
                }
            }
        }
        return image;
    }

    private void deadAnimation(Graphics2D graphics2D) {
        deadCounter++;
        int i = 5;
        if(deadCounter <= i) changeAlpha(graphics2D, 0f);
        if(deadCounter > i && deadCounter <= i * 2) changeAlpha(graphics2D, 1f);
        if(deadCounter > i * 2 && deadCounter <= i * 3) changeAlpha(graphics2D, 0f);
        if(deadCounter > i * 3 && deadCounter <= i * 4) changeAlpha(graphics2D, 1f);
        if(deadCounter > i * 4 && deadCounter <= i * 5) changeAlpha(graphics2D, 0f);
        if(deadCounter > i * 5 && deadCounter <= i * 6) changeAlpha(graphics2D, 1f);
        if(deadCounter > i * 6 && deadCounter <= i * 7) changeAlpha(graphics2D, 0f);
        if(deadCounter > i * 7 && deadCounter <= i * 8) changeAlpha(graphics2D, 1f);

        if(deadCounter > i * 8) {
            alive = false;
        }
    }

    private void changeAlpha(Graphics2D graphics2D, float alphaValue) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    private void drawHealthBar(Graphics2D graphics2D, int screenX, int screenY) {
        if(entityType == 2 && hpBarOn) {
            double oneScale = (double) TILE_SIZE / maxLives;
            double hpValue = oneScale * life;

            graphics2D.setColor(new Color(35, 35, 35));
            graphics2D.fillRect(screenX - 1, screenY - 11, TILE_SIZE + 2, 12);

            graphics2D.setColor(new Color(255, 0, 30));
            graphics2D.fillRect(screenX, screenY - 10, (int) hpValue, 10);

            hpBarCounter++;
            if(hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }
    }

    protected void drawAnimation(Graphics2D graphics2D) {
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

        BufferedImage image = animate();

        if(worldX + TILE_SIZE > left && worldX - TILE_SIZE < right && worldY + TILE_SIZE > up && worldY - TILE_SIZE < down) {
            // Monster HP Bar
            drawHealthBar(graphics2D, screenX, screenY);

            if(invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                // DISABLE TRANSPARENT WITH INTERACTIVE TILE
                if(! (this instanceof InteractiveTile)) {
                    changeAlpha(graphics2D, 0.4f);
                }
            }
            if(dead) deadAnimation(graphics2D);

            graphics2D.drawImage(image, screenX, screenY, null);
            changeAlpha(graphics2D, 1f);
        } else if(playerScreenX > playerWorldX ||       //TODO: need to fix later
                playerScreenY > playerWorldY ||
                rightOffset > WORLD_WIDTH - playerWorldX ||
                bottomOffset > WORLD_HEIGHT - playerWorldY) {
            if(invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(graphics2D, 0.4f);
            }
            if(dead) deadAnimation(graphics2D);

            graphics2D.drawImage(image, screenX, screenY, null);
            changeAlpha(graphics2D, 1f);
        }

        // Draw hitbox
        //TODO: Delete it later
//        graphics2D.setColor(Color.BLUE);
//        graphics2D.drawRect(screenX + getHitbox().x, screenY + getHitbox().y, getHitbox().width, getHitbox().height);
    }

    protected void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + hitbox.x) / TILE_SIZE;
        int startRow = (worldY + hitbox.y) / TILE_SIZE;

        scene.getPathFinder().setNodes(startCol, startRow, goalCol, goalRow);

        if(scene.getPathFinder().search()) {
            // Next worldX & worldY
            int nextX = scene.getPathFinder().getPathList().get(0).getCol() * TILE_SIZE;
            int nextY = scene.getPathFinder().getPathList().get(0).getRow() * TILE_SIZE;
            // Entity's hitbox position
            int enLeftX = worldX + hitbox.x;
            int enRightX = worldX + hitbox.x + hitbox.width;
            int enTopY = worldY + hitbox.y;
            int enBottomY = worldY + hitbox.y + hitbox.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + TILE_SIZE) {
                direction = UP;
            } else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + TILE_SIZE) {
                direction = DOWN;
            } else if(enTopY >= nextY && enBottomY < nextY + TILE_SIZE) {
                // left or right
                if(enLeftX > nextX) {
                    direction = LEFT;
                }
                if(enLeftX < nextX) {
                    direction = RIGHT;
                }
            } else if(enTopY > nextY && enLeftX > nextX) {
                // up or left
                direction = UP;
                checkCollision();
                if(collision) {
                    direction = LEFT;
                }
            } else if(enTopY > nextY && enLeftX < nextX) {
                // up or right
                direction = UP;
                checkCollision();
                if(collision) {
                    direction = RIGHT;
                }
            } else if(enTopY < nextY && enLeftX > nextX) {
                // down or left
                direction = DOWN;
                checkCollision();
                if(collision) {
                    direction = LEFT;
                }
            } else if(enTopY < nextY && enLeftX < nextX) {
                // down or right
                direction = DOWN;
                checkCollision();
                if(collision) {
                    direction = RIGHT;
                }
            }

            // If reaches the goal, stop the search
            //TODO: disable for NPC following player
//            int nextCol = scene.getPathFinder().getPathList().get(0).getCol();
//            int nextRow = scene.getPathFinder().getPathList().get(0).getRow();
//            if(nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
//            }
        }
    }

    protected void checkAttackOrNot(int rate, int vertical, int horizontal) {
        Player player = scene.getPlayer();
        boolean targetInRange = false;
        int xDistance = getXDistance(player);
        int yDistance = getYDistance(player);

        switch(direction) {
            case UP -> {
                if(player.worldY < worldY && yDistance < vertical && xDistance < horizontal) targetInRange = true;
            }
            case DOWN -> {
                if(player.worldY > worldY && yDistance < vertical && xDistance < horizontal) targetInRange = true;
            }
            case LEFT -> {
                if(player.worldX < worldX && xDistance < vertical && yDistance < horizontal) targetInRange = true;
            }
            case RIGHT -> {
                if(player.worldX > worldX && xDistance < vertical && yDistance < horizontal) targetInRange = true;
            }
        }
        if(targetInRange) {
            // Check if it initiates an attack
            int i = new Random().nextInt(rate);
            if(i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }
    }

    protected void checkStartChasingOrNot(Entity target, int distance, int rate) {
        if(getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = true;
            }
        }
    }

    protected void checkStopChasingOrNot(Entity target, int distance, int rate) {
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = false;
            }
        }
    }

    protected void getRandomDirection() {
        actionLockCounter++;
        if(actionLockCounter == 180) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;    // pick up a number from 1 to 100;
            if(i <= 25) direction = UP;
            if(i > 25 && i <= 50) direction = DOWN;
            if(i > 50 && i <= 75) direction = LEFT;
            if(i > 75) direction = RIGHT;
            actionLockCounter = 0;
        }
    }

    protected void checkShootOrNot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate);
        if(i == 0 && ! projectile.isAlive() && shotAvailableCounter == shotInterval) {
            projectile.set(worldX, worldY, direction, true, this);

            // CHECK VACANCY
            for(int x = 0; x < scene.getProjectiles()[1].length; x++) {
                if(scene.getProjectiles()[scene.currentMap][x] == null) {
                    scene.getProjectiles()[scene.currentMap][x] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;
        }
    }

    protected int getDetected(Entity user, Entity[][] targets, String targetName) {
        int index = 999;

        // Check the surrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch(user.direction) {
            case UP -> nextWorldY = user.getTopY() - 1;
            case LEFT -> nextWorldX = user.getLeftX() - 1;
            case DOWN -> nextWorldY = user.getBottomY() + 1;
            case RIGHT -> nextWorldX = user.getRightX() + 1;
        }

        int col = nextWorldX / TILE_SIZE;
        int row = nextWorldY / TILE_SIZE;

        for(int i = 0; i < targets[1].length; i++) {
            Entity target = targets[scene.currentMap][i];
            if(target != null) {
                if(target.getCol() == col && target.getRow() == row && target.getObjectName().equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int getXDistance(Entity target) {
        return Math.abs(worldX - target.worldX);
    }

    public int getYDistance(Entity target) {
        return Math.abs(worldY - target.worldY);
    }

    public int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / TILE_SIZE;
    }

    public int getGoalCol(Entity target) {
        return (target.worldX + target.hitbox.x) / TILE_SIZE;
    }

    public int getGoalRow(Entity target) {
        return (target.worldY + target.hitbox.y) / TILE_SIZE;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public int getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public int getEntityType() {
        return entityType;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getHitboxDefaultX() {
        return hitboxDefaultX;
    }

    public int getHitboxDefaultY() {
        return hitboxDefaultY;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public String getObjectName() {
        return objectName;
    }

    public BufferedImage getDown1() {
        return down1;
    }

    public BufferedImage getLeft1() {
        return left1;
    }

    public BufferedImage getDown2() {
        return down2;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void lostLife() {
        this.life--;
    }

    public void gainLife(int value) {
        this.life += value;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void subtractMana(int useCost) {
        this.mana -= useCost;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void pickupMana(int value) {
        this.mana += value;
    }

    public int getAmmo() {
        return ammo;
    }

    public void subtractAmmo(int useCost) {
        this.ammo -= useCost;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return dead;
    }

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getExp() {
        return exp;
    }

    public int getNextLevelExp() {
        return nextLevelExp;
    }

    public int getCoin() {
        return coin;
    }

    public void pickupCoin(int value) {
        coin += value;
    }

    public Entity getCurrentWeapon() {
        return currentWeapon;
    }

    public Entity getCurrentShield() {
        return currentShield;
    }

    public String getDescription() {
        return description;
    }

    public Color getParticleColor() {
        return null;
    }

    public int getParticleSize() {
        return 0;
    }

    public int getParticleSpeed() {
        return 0;
    }

    public int getParticleMaxLives() {
        return 0;
    }

    public ArrayList<Entity> getInventory() {
        return inventory;
    }

    public int getPrice() {
        return price;
    }

    public void buyItem(int price) {
        this.coin -= price;
    }

    public void sellItem(int price) {
        this.coin += price;
    }

    public int getLeftX() {
        return worldX + hitbox.x;
    }

    public int getRightX() {
        return worldX + hitbox.x + hitbox.width;
    }

    public int getTopY() {
        return worldY + hitbox.y;
    }

    public int getBottomY() {
        return worldY + hitbox.y + hitbox.height;
    }

    public int getCol() {
        return (worldX + hitbox.x) / TILE_SIZE;
    }

    public int getRow() {
        return (worldY + hitbox.y) / TILE_SIZE;
    }

    public int getAmount() {
        return amount;
    }

    public int reduceAmount() {
        return amount--;
    }

    public Entity getCurrentLight() {
        return currentLight;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public boolean isKnockBack() {
        return knockBack;
    }

    public String getKnockBackDirection() {
        return knockBackDirection;
    }
}
