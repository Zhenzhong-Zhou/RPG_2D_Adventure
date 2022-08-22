package entities;

import input.KeyInputs;
import main.Scene;
import objects.Shield_Wood;
import objects.Sword_Normal;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.*;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;
import static utilities.LoadSave.*;

public class Player extends Entity {
    private final int screenX, screenY;
    private final KeyInputs keyInputs;
    private int standCounter = 0;
    private boolean attackCanceled;

    public Player(Scene scene, KeyInputs keyInputs) {
        super(scene);
        this.keyInputs = keyInputs;

        screenX = (SCENE_WIDTH / 2) - (TILE_SIZE / 2);
        screenY = (SCENE_HEIGHT / 2) - (TILE_SIZE / 2);

        initBox();
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    private void initBox() {
        hitbox = new Rectangle(8, 16, 32, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        attackBox.width = 36;
        attackBox.height = 36;
    }

    private void setDefaultValues() {
//        worldX = (MAX_WORLD_COL / 2 - 1) * TILE_SIZE;
//        worldY = (MAX_WORLD_ROW / 2 - 1) * TILE_SIZE;
        // TODO: not center
        worldX = 23 * TILE_SIZE;
        worldY = 21 * TILE_SIZE;
        speed = 2;
        direction = DOWN;

        // PLAYER STATUS
        level = 0;
        maxLives = 6;
        life = maxLives;
        strength = 1;   // The more strength he has, the more damage he gives.
        dexterity = 1;  // The more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new Sword_Normal(scene);
        currentShield = new Shield_Wood(scene);
        attack = getAttack();   // The total attack value is decided by strength and weapon.
        defense = getDefense(); // The total defense value is decided by dexterity and shield.
    }

    public int getAttack() {
        return attack = strength*currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity* currentShield.defenseValue;
    }

    private void getPlayerImage() {
        up1 = GetSpriteAtlas(UP_1_IMAGE);
        up2 = GetSpriteAtlas(UP_2_IMAGE);
        left1 = GetSpriteAtlas(LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(RIGHT_2_IMAGE);
    }

    private void getPlayerAttackImage() {
        attackUp1 = GetAttackImage(ATTACK_UP_1_IMAGE, TILE_SIZE,TILE_SIZE*2);
        attackUp2 = GetAttackImage(ATTACK_UP_2_IMAGE, TILE_SIZE,TILE_SIZE*2);
        attackLeft1 = GetAttackImage(ATTACK_LEFT_1_IMAGE, TILE_SIZE*2, TILE_SIZE);
        attackLeft2 = GetAttackImage(ATTACK_LEFT_2_IMAGE, TILE_SIZE*2, TILE_SIZE);
        attackDown1 = GetAttackImage(ATTACK_DOWN_1_IMAGE, TILE_SIZE,TILE_SIZE*2);
        attackDown2 = GetAttackImage(ATTACK_DOWN_2_IMAGE, TILE_SIZE,TILE_SIZE*2);
        attackRight1 = GetAttackImage(ATTACK_RIGHT_1_IMAGE, TILE_SIZE*2, TILE_SIZE);
        attackRight2 = GetAttackImage(ATTACK_RIGHT_2_IMAGE, TILE_SIZE*2, TILE_SIZE);
    }

    public void update() {
        if(attacking) {
            attacking();
        }
        updatePositions();
    }

    private void attacking() {
        spriteCounter++;
        if(spriteCounter<=10) spriteNum = 1;
        if(spriteCounter>10 && spriteCounter<=35) {
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

            // Check monster collision with the updated worldX/Y and hitbox
            int monsterIndex = scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
            damageMonster(monsterIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
        }
        if(spriteCounter>35) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    private void updatePositions() {
        if(keyInputs.isUpPressed() || keyInputs.isLeftPressed() || keyInputs.isDownPressed() || keyInputs.isRightPressed() || keyInputs.isEnterPressed()) {
            if(keyInputs.isUpPressed()) direction = UP;
            else if(keyInputs.isLeftPressed()) direction = LEFT;
            else if(keyInputs.isDownPressed()) direction = DOWN;
            else if(keyInputs.isRightPressed()) direction = RIGHT;

            checkCollision();
            playerCanMove();
            updateAnimation();
        } else {
            standCounter++;

            if(standCounter == animationSpeed) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        // This needs to be outside of key if statement!
        invincibleCounter();
    }

    public void playerCanMove() {
        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(! collision && ! keyInputs.isEnterPressed()) {
            switch(direction) {
                case UP -> worldY -= speed;
                case LEFT -> worldX -= speed;
                case DOWN -> worldY += speed;
                case RIGHT -> worldX += speed;
            }
        }
        checkEnterKey();
        keyInputs.setEnterPressed(false);
    }

    private void checkEnterKey() {
        if(keyInputs.isEnterPressed() && ! attackCanceled) {
            scene.getAudioManager().playEffect(SWING_WEAPON);
            attacking = true;
            spriteCounter = 0;
        }
        attackCanceled = false;
    }

    public void invincibleCounter() {
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void checkCollision() {
        // CHECK TILE COLLISION
        collision = false;
        scene.getCollisionDetection().checkTile(this);

        // CHECK OBJECT COLLISION
        int objectIndex = scene.getCollisionDetection().checkObject(this, true);
        collectObject(objectIndex);

        // CHECK NPC COLLISION
        int npcIndex = scene.getCollisionDetection().checkEntity(this, scene.getNPCs());
        interactNPC(npcIndex);

        // CHECK MONSTER COLLISION
        int monsterIndex = scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
        interactMonster(monsterIndex);

        // CHECK EVENT
        scene.getEventManager().checkEvent();
    }

    private void collectObject(int objectIndex) {
        if(objectIndex != 999) {

        }
    }

    private void interactNPC(int npcIndex) {
        if(keyInputs.isEnterPressed()) {
            if(npcIndex != 999) {
                attackCanceled = true;
                gameState = DIALOGUE;
                scene.getNPCs()[npcIndex].speak();
            }
        }
    }

    private void interactMonster(int monsterIndex) {
        if(monsterIndex != 999) {
            if(! invincible) {
                scene.getAudioManager().playEffect(RECEIVED_DAMAGE);
                int damage = scene.getMonsters()[monsterIndex].attack - scene.getPlayer().defense;
                if(damage < 0) {
                    damage = 0;
                }
                scene.getPlayer().life -= damage;
                scene.getGui().addMessage("-" + damage + " damage!");
                invincible = true;
            }
        }
    }

    private void damageMonster(int monsterIndex) {
        if(monsterIndex != 999) {
            Entity monster = scene.getMonsters()[monsterIndex];
            if(!monster.invincible) {
                scene.getAudioManager().playEffect(HIT_MONSTER);
                int damage = attack - monster.defense;
                if(damage < 0) {
                    damage = 0;
                }
                monster.life -= damage;
                scene.getGui().addMessage(damage + " damage!");
                monster.invincible = true;
                monster.damageReaction();
                if(monster.life <= 0) {
                    scene.getMonsters()[monsterIndex].dead = true;
                    scene.getGui().addMessage("killed the " + monster.getObjectName() + "!");
                    exp += monster.getExp();
                    scene.getGui().addMessage("Exp + " + monster.getExp());
                    checkLevelUp();
                }
            }
        }
    }

    private void checkLevelUp() {
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLives += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            scene.getAudioManager().playEffect(LEVEL_UP);
            gameState = DIALOGUE;
            scene.getGui().setCurrentDialogue("You are level " + level + " now!\nYou feel stronger!");
        }
    }

    public void draw(Graphics2D graphics2D) {
        drawAnimation(graphics2D);
    }

    public void drawAnimation(Graphics2D graphics2D) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) {
            case UP -> {
                if(!attacking) {
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
                if(!attacking) {
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
                if(!attacking) {
                    if(spriteNum == 1) image = down1;
                    if(spriteNum == 2) image = down2;
                }
                if(attacking) {
                    if(spriteNum == 1) image = attackDown1;
                    if(spriteNum == 2) image = attackDown2;
                }
            }
            case RIGHT -> {
                if(!attacking) {
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                }
                if(attacking) {
                    if(spriteNum == 1) image = attackRight1;
                    if(spriteNum == 2) image = attackRight2;
                }
            }
        }

        if(screenX > worldX) tempScreenX = worldX;
        if(screenY > worldY) tempScreenY = worldY;

        int rightOffset = SCENE_WIDTH - screenX;
        if(rightOffset > WORLD_WIDTH - worldX) tempScreenX = SCENE_WIDTH - (WORLD_WIDTH - worldX);

        int bottomOffset = SCENE_HEIGHT - screenY;
        if(bottomOffset > WORLD_HEIGHT - worldY) tempScreenY = SCENE_HEIGHT - (WORLD_HEIGHT - worldY);

        if(invincible) graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

        graphics2D.drawImage(image, tempScreenX, tempScreenY, null);

        // REST ALPHA
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG: Draw hitbox
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(tempScreenX + hitbox.x, tempScreenY + hitbox.y, hitbox.width, hitbox.height);

        // DEBUG: Invincible counter
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.PLAIN, 25F));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Invincible: " + invincibleCounter, 10, 550);
    }

    public void resetDirectionBoolean() {
        keyInputs.setUpPressed(false);
        keyInputs.setLeftPressed(false);
        keyInputs.setDownPressed(false);
        keyInputs.setRightPressed(false);
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setAttackCanceled(boolean attackCanceled) {
        this.attackCanceled = attackCanceled;
    }
}
