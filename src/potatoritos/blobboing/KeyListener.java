package potatoritos.blobboing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
    private boolean[] keyDown;

    public KeyListener() {
        keyDown = new boolean[228];
    }
    // Description: determines whether a key is being held
    // Parameters:
    //      key: the key code of the key
    // Return: true if the key is being held
    public boolean isKeyDown(int key) {
        return keyDown[key];
    }

    // Description: determines whether a key has been pressed
    //              during the current frame
    // Parameters:
    //      key: the key code of the key
    // Return: true if the key has been pressed
    public boolean isKeyDownFrame(int key) {
        if (keyDown[key]) {
            keyDown[key] = false;
            return true;
        }
        return false;
    }

    // Description: Indicates that a key has been pressed
    // Parameters:
    //      e: a KeyEvent
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() > 227) {
            return;
        }
        keyDown[e.getKeyCode()] = true;
    }

    // Description: Indicates that a key has been released
    // Parameters:
    //      e: a KeyEvent
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() > 227) {
            return;
        }
        keyDown[e.getKeyCode()] = false;
    }
}
