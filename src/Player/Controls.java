/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Player;

/**
 *
 * @author Julian Craske
 */

import java.util.*;
import java.awt.event.*;

public abstract class Controls {
    public static final int KEYBOARD = 1;
    public static final int MOUSE = 2;

    private static boolean[] keys = new boolean[525];
    public static boolean[] mouse = new boolean[4];
    public static int lastMouseX, lastMouseY;

    private static HashMap<Player.Command, ControlEvent> controls = new HashMap<Player.Command, ControlEvent>();
    private static Listener listener = new Listener();    

    static {
        defaultKeys();
    }

    public static void defaultKeys() {
        put(Player.Command.Left, KE(KeyEvent.VK_A));
        put(Player.Command.Right, KE(KeyEvent.VK_D));
        put(Player.Command.Thrust, KE(KeyEvent.VK_W));
        put(Player.Command.Reverse, KE(KeyEvent.VK_S));
        put(Player.Command.Fire, ME(MouseEvent.BUTTON1));
        put(Player.Command.FireMissiles, ME(MouseEvent.BUTTON3));
        put(Player.Command.NextTarget, KE(KeyEvent.VK_E));
        put(Player.Command.ClosestEnemy, KE(KeyEvent.VK_Q));
        put(Player.Command.TargetSelf, KE(KeyEvent.VK_X));
        put(Player.Command.Cloak, KE(KeyEvent.VK_K));
        put(Player.Command.Boost, KE(KeyEvent.VK_B));
        put(Player.Command.Land, KE(KeyEvent.VK_L));
        put(Player.Command.Hyperspace, KE(KeyEvent.VK_H));
    }
    
    public static ControlEvent ME(int eventCode) {
        return new ControlEvent(MOUSE, eventCode);
    }
    
    public static ControlEvent KE(int eventCode) {
        return new ControlEvent(KEYBOARD, eventCode);
    }
    
    public static void put(Player.Command command, ControlEvent e) {
        controls.put(command, e);
    }

    public static class ControlEvent {
        private int type;
        private int code;

        public ControlEvent(int type, int code) {
            this.type = type;
            this.code = code;
        }

        public boolean isMouseEvent() {
            return type == MOUSE;
        }

        public boolean isKeyboardEvent() {
            return type == KEYBOARD;
        }

        public boolean isActive() {
            if(isKeyboardEvent()) {
                return keys[code];
            } else if(isMouseEvent()) {
                return mouse[code];
            }
            return false;
        }
    }

    public static boolean[] getPlayerPacket() {
        Player.Command[] commands = Player.Command.values();
        boolean[] values = new boolean[commands.length];
        for(int i = 0; i < commands.length; i++) {
            try {
                values[i] = controls.get(commands[i]).isActive();
            } catch (NullPointerException ex) {
                values[i] = false;
                System.out.println(commands[i].name() + " is not bound");
            }
        }
        return values;
    }

    public static HashMap<Player.Command, ControlEvent> getControls() {
        return controls;
    }

    public static String getKey(Player.Command command) {
        if(controls.containsKey(command)) {
            ControlEvent ce = controls.get(command);
            if(ce.isKeyboardEvent()) {
                return KeyEvent.getKeyText(ce.code);
            }
            if(ce.isMouseEvent()) {
                if(ce.code == MouseEvent.BUTTON1) return "Left Mouse";
                if(ce.code == MouseEvent.BUTTON2) return "Middle Mouse";
                if(ce.code == MouseEvent.BUTTON3) return "Right Mouse";
            }
        }
        return "unassigned";
    }

    public static KeyListener getKeyListener() {
        return listener;
    }

    public static MouseListener getMouseListener() {
        return listener;
    }

    public static class Listener implements KeyListener, MouseListener {
        /**
        * Keyboard Manager Methods
        */
        public boolean isKeyDown(int key) {
            return keys[key];
        }

        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;
        }

        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
        }

        public void keyTyped(KeyEvent e) { }

        /**
         * Mouse Manager Methods
         */

        public void mouseClicked(MouseEvent e) { }

        public void mousePressed(MouseEvent e) {
            mouse[e.getButton()] = true;
        }

        public void mouseReleased(MouseEvent e) {
            mouse[e.getButton()] = false;
        }

        public void mouseEntered(MouseEvent e) { }

        public void mouseExited(MouseEvent e) { }
    }
}
