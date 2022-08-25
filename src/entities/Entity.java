package entities;

import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilities.Constants.AudioManager.RECEIVED_DAMAGE;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.EntityConstant.MONSTER;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;

public abstract class Entity {
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
    protected Entity currentWeapon;
    protected Entity currentShield;
    protected Projectile projectile;
    protected int attackValue;
    protected int defenseValue;
    protected String description = "";
    protected int useCost;
    protected int shotAvailableCounter;

    public Entity(Scene scene) {
        this.scene = scene;

        hitbox = new Rectangle(0, 0, TILE_SIZE, TILE_SIZE);
        attackBox = new Rectangle();
    }

    protected void setAction() {
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

    protected void damageReaction() {
    }

    protected void speak() {
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

    public void use(Entity entity) {
    }

    public void update() {
        setAction();

        collision = false;
        scene.getCollisionDetection().checkTile(this);

        scene.getCollisionDetection().checkObject(this, false);
        scene.getCollisionDetection().checkEntity(this, scene.getNPCs());
        scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
        boolean interactPlayer = scene.getCollisionDetection().checkPlayer(this);

        if(this.entityType == MONSTER && interactPlayer) {
            damagePlayer(attack);
        }

        playerCanMove();
        updateAnimation();
        invincibleCounter();

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
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

    protected void playerCanMove() {
        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(! collision) {
            switch(direction) {
                case UP -> worldY -= speed;
                case LEFT -> worldX -= speed;
                case DOWN -> worldY += speed;
                case RIGHT -> worldX += speed;
            }
        }
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

        switch(direction) {
            case UP -> {
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
            }
            case LEFT -> {
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
            }
            case DOWN -> {
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
            }
            case RIGHT -> {
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
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
                changeAlpha(graphics2D, 0.4f);
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

    public Entity getCurrentWeapon() {
        return currentWeapon;
    }

    public Entity getCurrentShield() {
        return currentShield;
    }

    public String getDescription() {
        return description;
    }
}
