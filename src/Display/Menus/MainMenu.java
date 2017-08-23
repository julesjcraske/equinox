package Display.Menus;

/**
 *
 * @author Julian Craske
 */

import Display.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import Game.*;
import Display.*;
import java.awt.event.ActionListener;

public class MainMenu extends Screen {
    private GameClient client;

    private JButton multiplayer = new JButton("Multiplayer");
    private JButton quit = new JButton("Quit");

    public MainMenu(GameClient c) {
        client = c;

        Container top = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel("Equinox");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(title);
        top.add(new JLabel());

        Container menu = new JPanel(new GridLayout(2, 1));
        menu.setMaximumSize(new Dimension(2000, 400));
        Container a = new JPanel();
        a.add(multiplayer);
        menu.add(a);
        Container b = new JPanel();
        b.add(quit);
        menu.add(b);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(menu, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.SOUTH);

        Font font = multiplayer.getFont().deriveFont(Font.PLAIN, 36);
        multiplayer.setFont(font);
        multiplayer.setHorizontalAlignment(SwingConstants.CENTER);
        quit.setFont(font);
        quit.setHorizontalAlignment(SwingConstants.CENTER);

        multiplayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showScreen(new MultiplayerMenu(client));
            }
        });
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.quit();
            }
        });
    }

    public void update() {

    }
}
