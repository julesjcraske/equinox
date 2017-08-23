package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;

public class Ping extends Message {
    private static final short code = 901;

    private long time;

    public Ping() {
        time = System.currentTimeMillis();
    }

    public void exec(GameModel model) {

    }

    @Override
    public void onRecieve(GameClient client) {
        client.deliver(new Pong(time));
    }

    @Override
    public void onRecieve(GameServer server) {
        server.send(new Pong(time), playerId);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeLong(time);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        Ping p = new Ping();
        p.playerId = in.readInt();
        p.time = in.readLong();
        return p;
    }

    public short getCode() {
        return code;
    }
}
