package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Player.PlayerInfo;
import Game.*;

public class PlayerInfoMessage extends Message {
    private static final short code = 903;
    private static final String emptyPlayer = "";

    private PlayerInfo info;

    public PlayerInfoMessage() {

    }

    public PlayerInfoMessage(PlayerInfo info) {
        this.info = info;
    }

    public PlayerInfoMessage(int playerId) {
         this.playerId = playerId;
         info = new PlayerInfo(emptyPlayer);
    }

    public PlayerInfoMessage(int playerId, PlayerInfo info) {
        this.playerId = playerId;
        this.info = info;
    }

    public boolean isEmptyPlayer() {
        return info.getName().equals(emptyPlayer);
    }

    @Override
    public void exec(GameModel model) {
        if(!isEmptyPlayer()) {
            model.updatePlayerInfo(playerId, info);
        } else {
            model.deletePlayerInfo(playerId);
        }
    }

    public void onRecieve(GameClient client) {

    }

    @Override
    public void onRecieve(GameServer server) {
        server.broadcast(this, playerId);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeUTF(info.getName());
        out.writeInt(info.getTeam());
        out.writeInt(info.getKills());
        out.writeInt(info.getDeaths());
        out.writeInt(info.getScore());
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        PlayerInfoMessage m = new PlayerInfoMessage();
        m.playerId = in.readInt();
        PlayerInfo i = new PlayerInfo(in.readUTF(), in.readInt(), in.readInt(), in.readInt(), in.readInt());
        m.info = i;
        return m;
    }

    @Override
    public short getCode() {
        return code;
    }
}
