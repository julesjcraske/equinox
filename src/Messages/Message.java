package Messages;

/**
 *
 * @author jc271
 */

import Game.GameServer;
import Game.GameClient;
import java.io.*;
import java.util.*;

import Game.*;
import Server.*;

public abstract class Message implements Serializable {
    private static HashMap<Short, Message> types = new HashMap<Short, Message>();

    static {
        getLoaders();
    }

    private static void getLoaders() {
        types = new HashMap<Short, Message>();        

        addType(new ConnectionRequest());
        addType(new ConnectionAccept());
        addType(new ConnectionClose());
        addType(new EntityUpdate());
        addType(new NewEntity());        
        addType(new ProjectileHit());
        addType(new PlayerKill());
        addType(new Ping());
        addType(new Pong());
        addType(new ChatMessage());
        addType(new PlayerInfoMessage());
        addType(new TurretRequest());
        addType(new TurretResponse());
    }

    private static void addType(Message msg) {
        types.put(msg.getCode(), msg);
    }

    protected int playerId = 0;

    public abstract void exec(GameModel model);

    public abstract void onRecieve(GameClient client);

    public abstract void onRecieve(GameServer server);

    public abstract void write(ObjectOutputStream out) throws IOException;

    public abstract Message read(ObjectInputStream in) throws IOException;

    public abstract short getCode();

    public void setPlayerId(int id) {
        playerId = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static Message readMessage(short type, ObjectInputStream in) throws IOException {
        try {
            return types.get(type).read(in);
        } catch (NullPointerException ex) {
            System.out.println("Recieved unknown message code: " + type);
            return null;
        }
    }

    public static void main(String[] args) {
        for(Message m : types.values()) {
            System.out.println(m.getCode() + " " + m.getClass().getSimpleName());
        }
    }
}
