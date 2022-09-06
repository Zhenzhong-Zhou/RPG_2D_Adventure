package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class Window extends JFrame {
    public Window(Scene scene) {
        setTitle("The Legend of Zelda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(scene);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                scene.requestFocusInWindow();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                scene.windowFocusLost();
            }
        });
    }
}
