package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;

public class ConnectionAccept extends Message {
    private static final short code = 103;

    public ConnectionAccept() {

    }

    public ConnectionAccept(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void exec(GameModel model) {

    }

    @Override
    public void onRecieve(GameClient client) {
        client.connectionAccept(playerId);
    }

    @Override
    public void onRecieve(GameServer server) {
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        ConnectionAccept m = new ConnectionAccept();
        m.playerId = in.readInt();
        return m;
    }

    public short getCode() {
        return code;
    }
}
