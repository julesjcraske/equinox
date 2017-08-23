package Messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Julian Craske
 */

import Game.*;

public class ChatMessage extends Message {
    public static final short code = 904;

    private String text;

    public ChatMessage() {

    }

    public ChatMessage(String text) {
        this.text = text;
    }

    @Override
    public void exec(GameModel model) {
        model.showMessage(model.getPlayerName(playerId) + ": " + text);
    }

    @Override
    public void onRecieve(GameClient client) {

    }

    @Override
    public void onRecieve(GameServer server) {
        server.broadcast(this);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeUTF(text);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        ChatMessage m = new ChatMessage();
        m.playerId = in.readInt();
        m.text = in.readUTF();
        return m;
    }

    public short getCode() {
        return code;
    }
}
