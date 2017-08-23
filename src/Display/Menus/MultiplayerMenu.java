package Display.Menus;

/**
 *
 * @author Julian Craske
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import Game.*;
import Display.*;
import java.awt.event.ActionListener;

public class MultiplayerMenu extends Screen {
    private GameClient client;

    private JTextField host = new JTextField(30);
    private JTextField port = new JTextField(5);
    private JButton connect = new JButton("Connect");

    public MultiplayerMenu(GameClient c) {
        client = c;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Multiplayer");
        title.setFont(title.getFont().deriveFont(Font.PLAIN, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        Container main = new JPanel();
        main.add(new JLabel("Host: "));
        main.add(host);
        main.add(new JLabel("Port: "));
        port.setText(Server.Server.getDefaultPort() + "");
        main.add(port);

        main.add(connect);
        add(main, BorderLayout.CENTER);

        addConnectListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String h = host.getText();
                    int p = Integer.parseInt(port.getText());
                    client.connect(h, p);
                    //place cant connect msg

                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(port, "Please enter a valid port.");
                }
            }
        });
    }

    private void addConnectListener(ActionListener listener) {
        host.addActionListener(listener);
        host.addActionListener(listener);
        connect.addActionListener(listener);
    }

    public void update() {

    }
}
