package entities;

import input.KeyInputs;
import main.Scene;
import objects.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GameState.*;
import static utilities.Constants.AudioManager.*;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.EntityConstant.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;
import static utilities.LoadSave.*;

public class Player extends Entity {
    private final int screenX, screenY;
    private final KeyInputs keyInputs;
    private int standCounter = 0;
    private boolean attackCanceled;
    private boolean lightUpdated;

    public Player(Scene scene, KeyInputs keyInputs) {
        super(scene);
        this.keyInputs = keyInputs;

        screenX = (SCENE_WIDTH / 2) - (TILE_SIZE / 2);
        screenY = (SCENE_HEIGHT / 2) - (TILE_SIZE / 2);

        initBox();
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    private void initBox() {
        hitbox = new Rectangle(8, 16, 32, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    private void setDefaultValues() {
//        worldX = (MAX_WORLD_COL / 2 - 1) * TILE_SIZE;
//        worldY = (MAX_WORLD_ROW / 2 - 1) * TILE_SIZE;
        // TODO: not center
        worldX = 23 * TILE_SIZE;
        worldY = 21 * TILE_SIZE;
        defaultSpeed = 4;
        speed = defaultSpeed;
        direction = DOWN;

        // PLAYER STATUS
        level = 0;
        maxLives = 6;
        life = maxLives;
        maxMana = 4;
        mana = maxMana;
        strength = 1;   // The more strength he has, the more damage he gives.
        dexterity = 10;  // The more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 5;
        coin = 500; // TODO: Remove
        currentWeapon = new Sword_Normal(scene);
        currentShield = new Shield_Wood(scene);
        projectile = new Fireball(scene);
        attack = getAttack();   // The total attack value is decided by strength and weapon.
        defense = getDefense(); // The total defense value is decided by dexterity and shield.
    }

    public int getAttack() {
        attackBox = currentWeapon.attackBox;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        up1 = GetSpriteAtlas(UP_1_IMAGE);
        up2 = GetSpriteAtlas(UP_2_IMAGE);
        left1 = GetSpriteAtlas(LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(RIGHT_2_IMAGE);
    }

    public void getSleepImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        left1 = image;
        left2 = image;
        down1 = image;
        down2 = image;
        right1 = image;
        right2 = image;
    }

    private void getPlayerAttackImage() {
        attackImages(SWORD, ATTACK_UP_1_IMAGE, ATTACK_UP_2_IMAGE, ATTACK_LEFT_1_IMAGE, ATTACK_LEFT_2_IMAGE, ATTACK_DOWN_1_IMAGE, ATTACK_DOWN_2_IMAGE, ATTACK_RIGHT_1_IMAGE, ATTACK_RIGHT_2_IMAGE);
        attackImages(AXE, AXE_UP_1_IMAGE, AXE_UP_2_IMAGE, AXE_LEFT_1_IMAGE, AXE_LEFT_2_IMAGE, AXE_DOWN_1_IMAGE, AXE_DOWN_2_IMAGE, AXE_RIGHT_1_IMAGE, AXE_RIGHT_2_IMAGE);
    }

    private void attackImages(int sword, String attackUp1Image, String attackUp2Image, String attackLeft1Image, String attackLeft2Image, String attackDown1Image, String attackDown2Image, String attackRight1Image, String attackRight2Image) {
        if(currentWeapon.entityType == sword) {
            attackUp1 = GetAttackImage(attackUp1Image, TILE_SIZE, TILE_SIZE * 2);
            attackUp2 = GetAttackImage(attackUp2Image, TILE_SIZE, TILE_SIZE * 2);
            attackLeft1 = GetAttackImage(attackLeft1Image, TILE_SIZE * 2, TILE_SIZE);
            attackLeft2 = GetAttackImage(attackLeft2Image, TILE_SIZE * 2, TILE_SIZE);
            attackDown1 = GetAttackImage(attackDown1Image, TILE_SIZE, TILE_SIZE * 2);
            attackDown2 = GetAttackImage(attackDown2Image, TILE_SIZE, TILE_SIZE * 2);
            attackRight1 = GetAttackImage(attackRight1Image, TILE_SIZE * 2, TILE_SIZE);
            attackRight2 = GetAttackImage(attackRight2Image, TILE_SIZE * 2, TILE_SIZE);
        }
    }

    public void resetDefaultPositions() {
//        worldX = (MAX_WORLD_COL / 2 - 1) * TILE_SIZE;
//        worldY = (MAX_WORLD_ROW / 2 - 1) * TILE_SIZE;
        // TODO: not center
        worldX = 23 * TILE_SIZE;
        worldY = 21 * TILE_SIZE;
        direction = DOWN;
    }

    public void restoreLifeAndMana() {
        life = maxLives;
        mana = maxMana;
        invincible = false;
    }

    private void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        // TODO: default items in player's bag
        inventory.add(new Axe(scene));
        inventory.add(new Lantern(scene));
    }

    public void update() {
        if(attacking) {
            attacking();
        }
        updatePositions();
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

        if(keyInputs.isShotPressed() && ! projectile.alive && shotAvailableCounter == 30 && projectile.hasEnergy(this)) {
            // SET DEFAULT COORDINATES, DIRECTION AND USER
            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT THE COST (MANA, AMMO ETC.)
            projectile.subtractEnergy(this);

            // CHECK VACANCY
            for(int i = 0; i < scene.getProjectiles()[1].length; i++) {
                if(scene.getProjectiles()[scene.currentMap][i] == null) {
                    scene.getProjectiles()[scene.currentMap][i] = projectile;
                    break;
                }
            }

            shotAvailableCounter = 0;

            scene.getAudioManager().playEffect(BURNING);
        }

        // This needs to be outside of key if statement!
        invincibleCounter();

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        if(life > maxLives) {
            life = maxLives;
        }
        if(mana > maxMana) {
            mana = maxMana;
        }
        if(life <= 0) {
            gameState = DEAD;
            scene.getGui().setCommandNum(- 1);
            scene.getAudioManager().stopSound();
            scene.getAudioManager().playEffect(DIE);
            scene.getAudioManager().playEffect(GAME_OVER);
        }
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

    public void checkCollision() {
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

        // CHECK INTERACTIVE TILE COLLISION
        int interactiveTileIndex = scene.getCollisionDetection().checkEntity(this, scene.getInteractiveTiles());
        contactInteractiveTile(interactiveTileIndex);

        // CHECK EVENT
        scene.getEventManager().checkEvent();
    }

    private void collectObject(int objectIndex) {
        if(objectIndex != 999) {
            Entity object = scene.getGameObjects()[scene.currentMap][objectIndex];
            // PICKUP ITEMS
            if(object.entityType == PICKUP) {
                object.use(this);
                scene.getGameObjects()[scene.currentMap][objectIndex] = null;
            }
            // OBSTACLE
            else if(object.entityType == OBSTACLE) {
                if(keyInputs.isEnterPressed()) {
                    attackCanceled = true;
                    object.interact();
                }
            }
            // INVENTORY ITEMS
            else {
                String text;
                if(canObtainItem(object)) {
                    scene.getAudioManager().playEffect(COIN);
                    text = "Pickup a " + object.getObjectName() + "!";
                } else {
                    text = "You bag is full!";
                }
                scene.getGui().addMessage(text);
                scene.getGameObjects()[scene.currentMap][objectIndex] = null;
            }
        }
    }

    private void interactNPC(int npcIndex) {
        if(keyInputs.isEnterPressed()) {
            if(npcIndex != 999) {
                attackCanceled = true;
                gameState = DIALOGUE;
                scene.getNPCs()[scene.currentMap][npcIndex].speak();
            }
        }
    }

    private void interactMonster(int monsterIndex) {
        if(monsterIndex != 999) {
            if(! invincible && ! scene.getMonsters()[scene.currentMap][monsterIndex].isDead()) {
                scene.getAudioManager().playEffect(RECEIVED_DAMAGE);
                int damage = scene.getMonsters()[scene.currentMap][monsterIndex].attack - scene.getPlayer().defense;
                if(damage < 0) {
                    damage = 0;
                }
                scene.getPlayer().life -= damage;
                scene.getGui().addMessage("-" + damage + " damage!");
                invincible = true;
            }
        }
    }

    protected void damageMonster(int monsterIndex, Entity attacker, int attack, int knockBackPower) {
        if(monsterIndex != 999) {
            Entity monster = scene.getMonsters()[scene.currentMap][monsterIndex];
            if(! monster.invincible) {
                scene.getAudioManager().playEffect(HIT_MONSTER);
                if(knockBackPower > 0) {
                    setKnockBack(monster, attacker, knockBackPower);
                }

                int damage = attack - monster.defense;
                if(damage < 0) {
                    damage = 0;
                }
                monster.life -= damage;
                scene.getGui().addMessage(damage + " damage!");
                monster.invincible = true;
                monster.damageReaction();
                if(monster.life <= 0) {
                    scene.getMonsters()[scene.currentMap][monsterIndex].dead = true;
                    scene.getGui().addMessage("killed the " + monster.getObjectName() + "!");
                    exp += monster.getExp();
                    scene.getGui().addMessage("Exp + " + monster.getExp());
                    checkLevelUp();
                }
            }
        }
    }

    private void contactInteractiveTile(int interactiveTileIndex) {
        if(interactiveTileIndex != 999) {

        }
    }

    protected void damageInteractiveTile(int interactiveTileIndex) {
        if(interactiveTileIndex != 999 && scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].destructible
                && scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].isCorrectItem(this) && ! scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].invincible) {
            scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].playEffect();
            scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].life--;
            scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].invincible = true;

            // Generate Particle
            generateParticle(scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex], scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex]);

            if(scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].life == 0) {
                scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex] = scene.getInteractiveTiles()[scene.currentMap][interactiveTileIndex].getDestroyedForm();
            }
        }
    }

    protected void damageProjectile(int projectileIndex) {
        if(projectileIndex != 999) {
            Entity projectile = scene.getProjectiles()[scene.currentMap][projectileIndex];
            projectile.alive = false;
            generateParticle(projectile, projectile);
        }
    }

    private void checkLevelUp() {
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
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

    public void selectItem() {
        int itemIndex = scene.getGui().getItemIndexOnSlot(scene.getGui().getPlayerSlotCol(), scene.getGui().getPlayerSlotRow());

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);
            //TODO: w/ or w/o weapon and shield -> change default values
            switch(selectedItem.entityType) {
                case SWORD, AXE -> {
                    currentWeapon = selectedItem;
                    attack = getAttack();
                    getPlayerAttackImage();
                }
                case SHIELD -> {
                    currentShield = selectedItem;
                    defense = getDefense();
                }
                case LIGHT -> {
                    if(currentLight == selectedItem) {
                        currentLight = null;
                    } else {
                        currentLight = selectedItem;
                    }
                    lightUpdated = true;
                }
                case CONSUMABLE -> {
                    if(selectedItem.use(this)) {
                        if(selectedItem.amount > 1) {
                            selectedItem.amount--;
                        } else {
                            inventory.remove(itemIndex);
                        }
                    }
                }
            }
        }
    }

    private int searchItemInInventory(String itemName) {
        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).getObjectName().equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;

        // CHECK IF STACKABLE
        if(item.stackable) {
            int index = searchItemInInventory(item.objectName);
            if(index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            // New item so need to check vacancy
            else {
                if(inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        }
        // NOT STACKABLE so check vacancy
        else {
            if(inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
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

    public boolean isLightUpdated() {
        return lightUpdated;
    }

    public void setLightUpdated(boolean lightUpdated) {
        this.lightUpdated = lightUpdated;
    }
}
