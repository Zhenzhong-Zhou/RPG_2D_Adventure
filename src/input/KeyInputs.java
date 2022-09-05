package input;

import main.Scene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameState.*;
import static utilities.Constants.AudioManager.*;

public class KeyInputs implements KeyListener {
    private final Scene scene;
    private boolean upPressed, leftPressed, downPressed, rightPressed, enterPressed, shotPressed;
    private boolean displayDebugInfo;

    public KeyInputs(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(gameState) {
            case MENU -> menu(e);
            case PLAY -> play(e);
            case SETTINGS -> settings(e);
            case PAUSE -> pause(e);
            case DIALOGUE -> dialogue(e);
            case CHARACTER -> character(e);
            case OPTIONS -> options(e);
            case DEAD -> dead(e);
            case TRADE -> trade(e);
        }
    }

    private void menu(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                scene.getGui().decrementCommandNum();
                if(scene.getGui().getCommandNum() < 0) {
                    scene.getGui().setCommandNum(3);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                scene.getGui().incrementCommandNum();
                if(scene.getGui().getCommandNum() > 3) {
                    scene.getGui().setCommandNum(0);
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch(scene.getGui().getCommandNum()) {
                    case 0 -> {
                        gameState = PLAY;
                        scene.getAudioManager().playMusic(START);
                        scene.restart();
                    }
                    case 1 -> System.out.println("LOAD GAME");
                    // TODO: if crash remove this part
                    case 2 -> {
                        gameState = SETTINGS;
                    }
                    case 3 -> System.exit(0);
                }
            }
        }
    }

    private void play(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_F -> shotPressed = true;
            case KeyEvent.VK_H -> displayDebugInfo = ! displayDebugInfo;
            case KeyEvent.VK_R -> {
                switch(scene.currentMap) {
                    //TODO: loadMap
                    case 0:
                        System.out.println("Refresh map! 0");
                    case 1:
                        System.out.println("Refresh map! 1");
                }

            }
            case KeyEvent.VK_P -> gameState = PAUSE; //TODO: Combine with ESC
            case KeyEvent.VK_C -> gameState = CHARACTER;
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_ESCAPE -> gameState = OPTIONS;
        }
    }

    private void settings(KeyEvent e) {
        cursor(e);
    }

    private void pause(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_P) gameState = PLAY;
    }

    private void dialogue(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) gameState = PLAY;
    }

    private void character(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_C -> gameState = PLAY;
            case KeyEvent.VK_ENTER -> scene.getPlayer().selectItem();
        }
        playerInventory(e);
    }

    private void options(KeyEvent e) {
        cursor(e);
    }

    private void dead(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                scene.getGui().decrementCommandNum();
                scene.getAudioManager().playEffect(CURSOR);
                if(scene.getGui().getCommandNum() < 0) {
                    scene.getGui().setCommandNum(1);
                }
                scene.getAudioManager().playEffect(CURSOR);
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                scene.getGui().incrementCommandNum();
                scene.getAudioManager().playEffect(CURSOR);
                if(scene.getGui().getCommandNum() > 1) {
                    scene.getGui().setCommandNum(0);
                }
                scene.getAudioManager().playEffect(CURSOR);
            }
            case KeyEvent.VK_ENTER -> {
                switch(scene.getGui().getCommandNum()) {
                    case 0 -> {
                        gameState = PLAY;
                        scene.retry();
                        scene.getAudioManager().playMusic(START);
                    }
                    case 1 -> {
                        gameState = MENU;
                        scene.getAudioManager().playMusic(MAIN_MENU);
                        scene.restart();
                        scene.getGui().setCommandNum(- 1);
                    }
                }
            }
        }
    }

    private void trade(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) enterPressed = true;
        switch(scene.getGui().getSubState()) {
            case 0 -> {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        scene.getGui().decrementCommandNum();
                        if(scene.getGui().getCommandNum() < 0) {
                            scene.getGui().setCommandNum(2);
                        }
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        scene.getGui().incrementCommandNum();
                        scene.getAudioManager().playEffect(CURSOR);
                        if(scene.getGui().getCommandNum() > 2) {
                            scene.getGui().setCommandNum(0);
                        }
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                }
            }
            case 1 -> {
                npcInventory(e);
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) scene.getGui().setSubState(0);
            }
            case 2 -> {
                playerInventory(e);
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) scene.getGui().setSubState(0);
            }
        }
    }

    private void cursor(KeyEvent e) {
        int maxCommandNum = 0;
        switch(scene.getGui().getSubState()) {
            case 0 -> maxCommandNum = 7;
            case 3 -> maxCommandNum = 1;
        }
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> gameState = PLAY;
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                scene.getGui().decrementCommandNum();
                scene.getAudioManager().playEffect(CURSOR);
                if(scene.getGui().getCommandNum() < 0) {
                    scene.getGui().setCommandNum(maxCommandNum);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                scene.getGui().incrementCommandNum();
                scene.getAudioManager().playEffect(CURSOR);
                if(scene.getGui().getCommandNum() > maxCommandNum) {
                    scene.getGui().setCommandNum(0);
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if(scene.getGui().getSubState() == 0) {
                    if(scene.getGui().getCommandNum() == 1 && scene.getAudioManager().getVolumeBGMScale() > 0) {
                        scene.getAudioManager().decreaseBGMVolume();
                        scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                    if(scene.getGui().getCommandNum() == 3 && scene.getAudioManager().getVolumeSEScale() > 0) {
                        scene.getAudioManager().decreaseSEVolume();
                        scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if(scene.getGui().getSubState() == 0) {
                    if(scene.getGui().getCommandNum() == 1 && scene.getAudioManager().getVolumeBGMScale() < 5) {
                        scene.getAudioManager().increaseBGMVolume();
                        scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                    if(scene.getGui().getCommandNum() == 3 && scene.getAudioManager().getVolumeSEScale() < 5) {
                        scene.getAudioManager().increaseSEVolume();
                        scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                        scene.getAudioManager().playEffect(CURSOR);
                    }
                }
            }
        }
    }

    private void playerInventory(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if(scene.getGui().getPlayerSlotRow() != 0) {
                    scene.getGui().playerSlotRowDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if(scene.getGui().getPlayerSlotCol() != 0) {
                    scene.getGui().playerSlotColDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if(scene.getGui().getPlayerSlotRow() != 4) {
                    scene.getGui().playerSlotRowIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if(scene.getGui().getPlayerSlotCol() != 4) {
                    scene.getGui().playerSlotColIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
        }
    }

    private void npcInventory(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if(scene.getGui().getNpcSlotRow() != 0) {
                    scene.getGui().npcSlotRowDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if(scene.getGui().getNpcSlotCol() != 0) {
                    scene.getGui().npcSlotColDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if(scene.getGui().getNpcSlotRow() != 4) {
                    scene.getGui().npcSlotRowIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if(scene.getGui().getNpcSlotCol() != 4) {
                    scene.getGui().npcSlotColIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_ENTER -> enterPressed = false;
            case KeyEvent.VK_F -> shotPressed = false;
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isDisplayDebugInfo() {
        return displayDebugInfo;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public boolean isShotPressed() {
        return shotPressed;
    }
}
