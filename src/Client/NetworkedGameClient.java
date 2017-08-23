package Client;

/**
 *
 * @author Julian Craske
 */

import Game.*;
import java.util.*;
import java.io.*;
import java.net.*;

import Messages.*;
import Player.*;
import jnet.*;
import Core.*;
import Sound.*;

public class NetworkedGameClient extends NetworkedGame implements GameClient {
    private static final Timer timer = new Timer();
    private static final long timeout = 10000;
    
    private Player player;
    private UDPClient connection;
    private Collection<ClientListener> listeners = new LinkedList<ClientListener>();

    private boolean connected = false;
    private boolean running = true;

    public NetworkedGameClient() {
        super();
        connection = new UDPClient();

        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    deliver(new Ping());
                }
            }, 5000, 5000);

        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    connection.recordBytesPerSecond();
                }
            }, 1000, 1000);
    }

    public void sendEntityUpdate(Entity e) {
        deliver(new EntityUpdate(e));
    }

    public void connect(String host, int port) {
        try {
            disconnect();
            showMessage("Connecting to " + host + ":" + port);
            connection.connect(host, port);
            connection.send(new ConnectionRequest(player.getName()));
            for(ClientListener l : listeners) {
                l.onConnectionAttempt();
            }
        } catch (UnknownHostException ex) {
            showMessage("Could not resolve hostname or address.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addLocalEntity(Entity e) {
        super.addLocalEntity(e);
        deliver(new NewEntity(e));
    }

    public void deliver(Message m) {
        if(connected) {
            m.setPlayerId(getPlayerId());
            try {
                connection.send(m);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void send(Message m, int destination) {
        deliver(m);
    }

    public void updateFromServer() {
        try {
            DatagramPacket p = connection.recieve();
            ObjectInputStream in = UDPUtil.getInputStream(p.getData());
            short code = in.readShort();
            Message m = Message.readMessage(code, in);
            m.onRecieve(this);
            m.exec(this);
        } catch (PortUnreachableException e) {
            showMessage("Port unreachable: " + e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        connection.disconnect();
        connected = false;
    }

    public void quit() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isConnected() {
        return connected;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getFocus() {
        return player.getPlayerShip();
    }

    public void connectionAccept(int playerId) {
        setPlayerId(playerId);
        connected = true;
        showMessage("Connected");
        for(ClientListener l : listeners) {
            l.onConnectionConfirm();
        }
    }

    public int getBytesPerSecond() {
        return connection.getBytesPerSecond();
    }

    public void setPlayerName(String name) {
        player = new Player(name);
        Entity.setSoundListener(new EffectPlayer(player));
    }

    public Timer getTimer() {
        return timer;
    }

    public void addClientListener(ClientListener listener) {
        listeners.add(listener);
    }
}
