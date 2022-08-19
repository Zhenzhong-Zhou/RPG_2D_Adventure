package input;

import main.Scene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameState.*;
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
            case MENU -> {
               switch(e.getKeyCode()) {
                   case KeyEvent.VK_W, KeyEvent.VK_KP_UP -> {
                       scene.getGui().commandNum--;
                       if(scene.getGui().commandNum < 0) {
                           scene.getGui().commandNum = 3;
                       }
                   }
                   case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                       scene.getGui().commandNum++;
                       if(scene.getGui().commandNum > 3) {
                           scene.getGui().commandNum = 0;
                       }
                   }
                   case KeyEvent.VK_ENTER -> {
                       switch(scene.getGui().commandNum) {
                           case 0 -> {
                               gameState = PLAY;
                               scene.getAudioManager().playMusic(START);
                           }
                           case 1 -> System.out.println("LOAD GAME");
                           case 2 -> System.out.println("OPTIONS");
                           case 3 -> System.exit(0);
                       }
                   }
               }
            }
            case PLAY -> {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_W -> upPressed = true;
                    case KeyEvent.VK_A -> leftPressed = true;
                    case KeyEvent.VK_S -> downPressed = true;
                    case KeyEvent.VK_D -> rightPressed = true;
                    case KeyEvent.VK_H -> displayDebugInfo = ! displayDebugInfo;
                    case KeyEvent.VK_R -> System.out.println("Refresh map!");
                    case KeyEvent.VK_P -> gameState = PAUSE; //TODO: Combine with ESC
                    case KeyEvent.VK_ENTER -> enterPressed = true;
                }
            }
            case PAUSE -> {
                if(e.getKeyCode() == KeyEvent.VK_P) gameState = PLAY;
            }
            case DIALOGUE -> {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) gameState = PLAY;
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
