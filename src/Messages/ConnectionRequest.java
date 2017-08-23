package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;

public class ConnectionRequest extends Message {
    public static final short code = 102;

    private String playerName;

    public ConnectionRequest() {

    }

    public ConnectionRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void exec(GameModel model) {

    }

    @Override
    public void onRecieve(GameClient client) {

    }

    @Override
    public void onRecieve(GameServer server) {

    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeUTF(playerName);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        ConnectionRequest c = new ConnectionRequest();
        c.playerId = in.readInt();
        c.playerName = in.readUTF();
        return c;
    }

    @Override
    public short getCode() {
        return code;
    }
}
