package Client;

import Display.MenuListener;
import Game.*;
import Display.*;
import Display.Menus.*;
import Player.Controls;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;

public class GUI implements ClientListener, MenuListener {
    private GameClient client;

    public static JFrame window;
    private Container content;

    private MainMenu mainMenu;
    private LinkedList<Screen> screenStack = new LinkedList<Screen>();
    
    public GUI(GameClient c)
    {
        this.client = c;
        client.addClientListener(this);
        Screen.setMenuListener(this);

        window = new JFrame("Equinox");
        
        createGUI();
        createMenu();

        showScreen(new StartScreen(client));
    }

    private void createGUI() {
        content = window.getContentPane();
        content.setLayout(new BorderLayout());

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //window.setUndecorated(true);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //window.setBounds(0, 0, screenSize.width, screenSize.height);
        Dimension size = new Dimension(1050, 850);
        window.setMinimumSize(new Dimension(800, 600));
        window.setPreferredSize(size);
        window.setSize(size);
        window.setVisible(true);
    }

    public void showScreen(Screen screen) {
        content.removeAll();
        screenStack.addLast(screen);
        content.add(screen, BorderLayout.CENTER);
        window.validate();
        screen.update();
        window.repaint();
    }

    public void back() {
        if(screenStack.size() > 1) {
            screenStack.removeLast();
            showScreen(screenStack.getLast());
        }
    }

    public void showMainMenu() {
        showScreen(new MainMenu(client));
    }

    public void showGameLobby() {
        showScreen(new GameLobby(client));
    }

    public void showShipSelect() {
        showScreen(new ShipSelect(client));
    }

    public void showFlightView() {
        showScreen(new FlightView(client));
        window.requestFocus();
    }
    
    private void createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu clientMenu = new JMenu("Client");
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.quit();
            }
        });
        clientMenu.add(quit);
        bar.add(clientMenu);
        window.setJMenuBar(bar);
    }

    private void resize() {
        int x = window.getWidth();
        int y = window.getHeight();
        window.pack();
        window.setSize(x, y);
    }

    public void update() {
        screenStack.getLast().update();
    }

    public void onMessageShow() {

    }

    public void onConnectionAttempt() {
//        Container connecting = new JPanel();
//        connecting.add(new JLabel("Awaiting reply from server..."));
//        showScreen(connecting);
    }

    public void onConnectionConfirm() {
        showGameLobby();
    }
}
