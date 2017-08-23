package Messages;

/**
 *
 * @author Julian Craske
 */

import Vessel.Ship.SpaceShip;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.*;

import Vessel.*;

public class TurretResponse extends Message {
    private static final short code = 402;

    private int turreteer;
    private int shipId;
    private boolean accepted;

    @Override
    public void exec(GameModel model) {

    }

    public void onRecieve(GameClient client) {
        if (accepted) {
            client.getPlayer().occupyTurret((SpaceShip) client.getEntity(shipId));
        } else {
            client.showMessage("Turret slots are full.");
        }
    }

    @Override
    public void onRecieve(GameServer server) {
        server.send(this, turreteer);
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(turreteer);
        out.writeInt(shipId);
        out.writeBoolean(accepted);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        TurretResponse m = new TurretResponse();
        m.playerId = in.readInt();
        m.turreteer = in.readInt();
        m.shipId = in.readInt();
        m.accepted = in.readBoolean();
        return m;
    }

    @Override
    public short getCode() {
        return code;
    }

}
