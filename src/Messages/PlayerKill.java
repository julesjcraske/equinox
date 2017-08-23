package Messages;

/**
 *
 * @author jc271
 */

import java.io.*;

import Game.*;
import Player.PlayerHolder;

public class PlayerKill extends Message {
    private static final short code = 301;

    private int killer;

    public PlayerKill() {

    }

    public PlayerKill(int killer) {
        this.killer = killer;
    }

    public String getDescription(PlayerHolder h) {
        String killer = h.getPlayerName(this.killer);
        String victim = h.getPlayerName(playerId);
        if(killer.equalsIgnoreCase("Server")) {
            return victim + " has died";
        } else {
            return victim + " was killed by " + killer;
        }
    }

    public void exec(GameModel model) {
        model.showMessage(getDescription(model));
    }

    public void onRecieve(GameClient client) {
        if (client.getPlayerId() == killer) {
            client.getPlayer().increaseKills();
        }
    }

    public void onRecieve(GameServer server) {
        server.broadcast(this, playerId);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(killer);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        PlayerKill kill = new PlayerKill();
        kill.playerId = in.readInt();
        kill.killer = in.readInt();
        return kill;
    }

    public short getCode() {
        return code;
    }
}
