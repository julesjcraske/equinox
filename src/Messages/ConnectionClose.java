/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Julian Craske
 */

import Game.*;

public class ConnectionClose extends Message {
    public static final short code = 104;

    @Override
    public void exec(GameModel model) {

    }

    @Override
    public void onRecieve(GameClient client) {

    }

    @Override
    public void onRecieve(GameServer server) {
        server.removePlayer(playerId);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        ConnectionClose m = new ConnectionClose();
        m.setPlayerId(in.readInt());
        return m;
    }

    @Override
    public short getCode() {
        return code;
    }
}
