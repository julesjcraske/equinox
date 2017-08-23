package Game;

/**
 *
 * @author Julian Craske
 */

import Messages.*;

public interface GameServer extends GameModel {
    public void broadcast(Message message);

    public void broadcast(Message message, int playerId);

    public void send(Message message, int playerId);

    public void resetTimeout(int playerId);

    public void removePlayer(int playerId);

    public int getServerId();
}
