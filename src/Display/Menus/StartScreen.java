package Display.Menus;

/**
 *
 * @author Julian Craske
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Game.*;
import Display.*;

public class StartScreen extends Screen {
    private GameClient client;

    private JTextField pilotName = new JTextField(15);
    private JButton play = new JButton("Play");

    public StartScreen(GameClient c) {
        client = c;
        setLayout(new BorderLayout());
        JLabel image = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("src\\images\\startScreen.jpg")));
        add(image, BorderLayout.CENTER);

        Container bottom = new JPanel();
        bottom.add(new JLabel("Enter Pilot Name: "));
        bottom.add(pilotName);
        bottom.add(play);
        add(bottom, BorderLayout.SOUTH);

        ActionListener go = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.setPlayerName(pilotName.getText());
                showScreen(new MainMenu(client));
            }
        };
        pilotName.addActionListener(go);
        play.addActionListener(go);
    }

    public void update() {
        pilotName.requestFocus();
    }

    public JTextField getPlayerTextField() {
        return pilotName;
    }
}
