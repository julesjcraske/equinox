package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;
import Player.*;

public class Pong extends Message {
    private static final short code = 902;

    private long time;

    public Pong() {

    }

    public Pong(long time) {
        this.time = time;
    }

    public void exec(GameModel model) {
        
    }

    @Override
    public void onRecieve(GameClient client) {
        client.getPlayer().getInfo().setLatency(System.currentTimeMillis() - time);
    }

    @Override
    public void onRecieve(GameServer server) {
        server.resetTimeout(playerId);
        PlayerInfo info = server.getPlayer(playerId);
        if(info != null) {
            info.setLatency(System.currentTimeMillis() - time);
        }
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeLong(time);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        Pong p = new Pong();
        p.playerId = in.readInt();
        p.time = in.readLong();
        return p;
    }

    public short getCode() {
        return code;
    }
}
