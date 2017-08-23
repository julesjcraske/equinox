package Display.Menus;

/**
 *
 * @author Julian Craske
 */


import Display.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;

import Game.*;
import Player.*;
import java.awt.event.ActionListener;

public class GameLobby extends Screen {
    private final GameClient client;

    private Container blue;
    private Container yellow;
    private final JButton joinBlue;
    private final JButton joinYellow;
    private JLabel bluePlayers;
    private JLabel yellowPlayers;

    public GameLobby(GameClient c) {
        setLayout(new BorderLayout());
        this.client = c;

        JLabel title = new JLabel("Team Select");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.PLAIN, 50));
        add(title, BorderLayout.NORTH);

        Container content = new JPanel(new GridLayout(1, 2));
        add(content, BorderLayout.CENTER);

        blue = new JPanel(new FlowLayout());
        blue.setBackground(Color.blue);
        content.add(blue, JSplitPane.LEFT);

        yellow = new JPanel(new FlowLayout());
        yellow.setBackground(Color.yellow);
        content.add(yellow, JSplitPane.RIGHT);

        joinBlue = new JButton("Join Blue");
        joinBlue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.getPlayer().joinTeam(1);
            }
        });
        joinYellow = new JButton("Join Yellow");
        joinYellow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.getPlayer().joinTeam(2);
            }
        });

        blue.add(joinBlue);
        yellow.add(joinYellow);

        bluePlayers = new JLabel();
        yellowPlayers = new JLabel();
        blue.add(bluePlayers);
        yellow.add(yellowPlayers);
        
        update();
    }

    public void update() {
        String b = "";
        String y = "";
        String u = "";
        for(PlayerInfo info : client.getPlayers()) {
            if(info.getTeam() == 1) {
                b += info.getName() + "\n";
            } else if(info.getTeam() == 2) {
                y += info.getName() + "\n";
            } else {
                u += info.getName() + "\n";
            }
        }
        bluePlayers.setText(b);
        yellowPlayers.setText(y);
    }
}
