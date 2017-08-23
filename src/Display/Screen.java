package Display;

/**
 *
 * @author Julian Craske
 */

import javax.swing.*;

public abstract class Screen extends JPanel {
    private static MenuListener menuListener;

    public abstract void update();

    protected static void showScreen(Screen screen) {
        menuListener.showScreen(screen);
    }

    public static void setMenuListener(MenuListener listener) {
        menuListener = listener;
    }
}
