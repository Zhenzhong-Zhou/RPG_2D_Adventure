package input;

import main.Scene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameState.*;
import static utilities.Constants.AudioManager.CURSOR;
import static utilities.Constants.AudioManager.START;

public class KeyInputs implements KeyListener {
    private final Scene scene;
    private boolean upPressed, leftPressed, downPressed, rightPressed, enterPressed;
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
                    }
                    case 1 -> System.out.println("LOAD GAME");
                    // TODO: if crash remove this part
                    case 2 -> {
                        gameState = SETTINGS;
                        scene.getOptions();
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
            case KeyEvent.VK_H -> displayDebugInfo = ! displayDebugInfo;
            case KeyEvent.VK_R -> System.out.println("Refresh map!");
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
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if(scene.getGui().getSlotRow() != 0) {
                    scene.getGui().slotRowDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if(scene.getGui().getSlotCol() != 0) {
                    scene.getGui().slotColDecrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if(scene.getGui().getSlotRow() != 4) {
                    scene.getGui().slotRowIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if(scene.getGui().getSlotCol() != 4) {
                    scene.getGui().slotColIncrease();
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
        }
    }

    private void options(KeyEvent e) {
        cursor(e);
    }

    private void cursor(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> gameState = PLAY;
            case KeyEvent.VK_ENTER -> enterPressed = true;
        }
        int maxCommandNum = 0;
        switch(scene.getGui().getSubState()) {
            case 0 -> maxCommandNum = 7;
            case 3 -> maxCommandNum = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
            scene.getGui().decrementCommandNum();
            scene.getAudioManager().playEffect(CURSOR);
            if(scene.getGui().getCommandNum() < 0) {
                scene.getGui().setCommandNum(maxCommandNum);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            scene.getGui().incrementCommandNum();
            scene.getAudioManager().playEffect(CURSOR);
            if(scene.getGui().getCommandNum() > maxCommandNum) {
                scene.getGui().setCommandNum(0);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(scene.getGui().getSubState() == 0) {
                if(scene.getGui().getCommandNum() == 1 && scene.getAudioManager().getVolumeScale() > 0) {
                    scene.getAudioManager().decreaseVolume();
                    scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                    scene.getAudioManager().playEffect(CURSOR);
                }
                if(scene.getGui().getCommandNum() == 3 && scene.getAudioManager().getVolumeScale() > 0) {
                    scene.getAudioManager().decreaseVolume();
                    scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                    scene.getAudioManager().playEffect(CURSOR);
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(scene.getGui().getSubState() == 0) {
                if(scene.getGui().getCommandNum() == 1 && scene.getAudioManager().getVolumeScale() < 5) {
                    scene.getAudioManager().increaseVolume();
                    scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
                    scene.getAudioManager().playEffect(CURSOR);
                }
                if(scene.getGui().getCommandNum() == 3 && scene.getAudioManager().getVolumeScale() < 5) {
                    scene.getAudioManager().increaseVolume();
                    scene.getAudioManager().setVolume(scene.getAudioManager().getVolume());
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

    public void setDisplayDebugInfo(boolean displayDebugInfo) {
        this.displayDebugInfo = displayDebugInfo;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }
}
