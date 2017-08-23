package Game;

import Player.*;
import Messages.*;
import Core.*;

/**
 *
 * @author Julian Craske
 */
public interface GameClient extends GameModel {
    public void setPlayerName(String s);

    public Player getPlayer();

    public int getPlayerId();
    
    public Entity getFocus();

    public void connectionAccept(int playerId);
    
    public void deliver(Message m);

    public boolean isLocal(Entity e);

    public int getBytesPerSecond();

    public void connect(String host, int port);

    public boolean isConnected();

    public void disconnect();

    public boolean isRunning();

    public void updateFromServer();

    public void addClientListener(ClientListener listener);
}
