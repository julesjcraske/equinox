package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;

public class TurretRequest extends Message {
    private static final short code = 401;

    private int turretOwner;
    private int shipId;

    @Override
    public void exec(GameModel model) {

    }

    public void onRecieve(GameClient client) {

    }

    @Override
    public void onRecieve(GameServer server) {
        server.send(this, turretOwner);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(turretOwner);
        out.writeInt(shipId);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        TurretRequest m = new TurretRequest();
        m.playerId = in.readInt();
        m.turretOwner = in.readInt();
        m.shipId = in.readInt();
        return m;
    }

    @Override
    public short getCode() {
        return code;
    }

}
