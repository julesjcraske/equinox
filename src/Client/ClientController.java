package Client;

/**
 *
 * @author Julian Craske
 */


import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;

import Core.*;
import Game.*;
import Player.*;
import Messages.*;
import Display.*;
import Sound.MusicPlayer;

public class ClientController implements EntityListener, PlayerListener {
    private static long gameRate = 20;
    private static java.util.Timer timer = new java.util.Timer();
    
    private GameClient client;
    private GUI gui;

    public ClientController() {
        Entity.addEntityListener(this);        
        Player.setPlayerListener(this);   
 
        client = new NetworkedGameClient();
        gui = new GUI(client);

        gui.window.addKeyListener(Controls.getKeyListener());
        gui.window.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String init = "Enter message";
                    String text = JOptionPane.showInputDialog(gui, init);
                    gui.window.requestFocus();
                    if(text != null && !text.equals("") && !text.equals(init)) {
                        client.deliver(new ChatMessage(text));
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_F1) {
                    if(SpaceView.hasOverlay()) {
                        SpaceView.setOverlay(null);
                    } else {
                        SpaceView.setOverlay(new ScoreBoard(client.getPlayers()));
                    }
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });

        new LocalThread().start();
        new UpdateThread().start();
        //new MusicThread().start();
    }

    private class LocalThread extends Thread {
        private LocalThread() {
            super("Client Gameplay");
        }

        @Override
        public void run() {
            long lastStep = 0;
            long time = 0;
            while(client.isRunning()) {
                if((time = System.currentTimeMillis()) > lastStep + (1000 / gameRate)) {
                    lastStep = time;
                    client.runOneStep();
                    gui.update();
                }                
                yield();
            }
            gui.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            if(client.isConnected()) {
                client.deliver(new ConnectionClose());
                client.disconnect();
            }

            System.exit(0);
        }
    }

    private class UpdateThread extends Thread {
        private UpdateThread() {
            super("Client UDP Reciever");
        }

        @Override
        public void run() {
            while(client.isRunning()) {
                client.updateFromServer();
                yield();
            }
        }
    }

    private class MusicThread extends Thread {
        MusicPlayer musicPlayer = new MusicPlayer();
        String[] playlist = {"src\\sounds\\All Falls Down.wav"};

        public MusicThread() {
            super("Music Thread");
        }

        @Override
        public void run() {
            while(true) {
                for(String filepath : playlist) {
                    musicPlayer.playAudioFile(filepath);
                }
            }
        }
    }

    public static void main(String[] args) {
        new ClientController();
    }

    public void onCreation(Entity e) {

    }

    public void onRemoteReceipt(Entity e) {

    }

    public void onDestruction(Entity e) {
        if(client.getPlayer().getPlayerShip() == e && client.getPlayer().isInFlight()) {
            client.getPlayer().increaseDeaths();
            timer.schedule(new TimerTask() {
                public void run() {
                    gui.back();
                }
            }, 2000);
        }
    }

    public void onVisualEffectCreation(Entity e) {

    }

    

    public void onPlayerChange(Player p) {
        client.updatePlayerInfo(client.getPlayerId(), p.getInfo());
        client.deliver(new PlayerInfoMessage(p.getInfo()));
    }

    public void onPlayerLaunched(Player p) {
        gui.showFlightView();
    }

    public void onPlayerJoinedTeam(Player p) {
        gui.showShipSelect();
    }

    public void onPlayerLand(Player p) {
        gui.back();
    }
}
