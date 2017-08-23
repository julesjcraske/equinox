package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.*;

import Core.*;
import Datastructures.*;
import Game.*;

public class NewEntity extends Message {
    private static final short code = 202;

    private int id, template, team, sector;
    private double x, y, dx, dy, rotation;

    public NewEntity() {
        
    }

    public NewEntity(Entity e) {
        playerId = e.getOwner();
        id = e.getId();
        team = e.getTeamNo();
        template = e.getName().hashCode();
        sector = e.getSectorID();
        x = e.getX();
        y = e.getY();
        dx = e.getSpeed().getX();
        dy = e.getSpeed().getY();
        rotation = e.getRotation();
    }

    public void exec(GameModel model) {
        createEntity().addAsRemoteEntity();
    }

    public void onRecieve(GameClient client) {

    }

    public void onRecieve(GameServer server) {
        server.broadcast(this, playerId);
    }

    private Entity createEntity() {
        Entity e = Templates.getEntity(template);
        e.setOwner(playerId);
        e.setId(id);
        e.setTeam(team);
        e.setSector(sector);
        e.setLocation(x, y);
        e.getSpeed().setXY(dx, dy);
        e.setRotation(rotation);
        return e;
    }

    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(id);
        out.writeInt(template);
        out.writeInt(team);
        out.writeInt(sector);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(dx);
        out.writeDouble(dy);
        out.writeDouble(rotation);
    }

    public NewEntity read(ObjectInputStream in) throws IOException {
        NewEntity e = new NewEntity();
        e.playerId = in.readInt();
        e.id = in.readInt();
        e.template = in.readInt();
        e.team = in.readInt();
        e.sector = in.readInt();
        e.x = in.readDouble();
        e.y = in.readDouble();
        e.dx = in.readDouble();
        e.dy = in.readDouble();
        e.rotation = in.readDouble();
        return e;
    }

    public short getCode() {
        return code;
    }
}
