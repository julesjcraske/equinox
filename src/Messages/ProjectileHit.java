package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.*;

import Projectiles.Destroyable;
import Core.*;
import Game.*;

public class ProjectileHit extends Message {
    private static final short code = 203;

    private int victimOwner, victimHash;
    private int projectileId;
    private double damage;

    public ProjectileHit() {

    }

    public ProjectileHit(Entity victim, Entity p, double damage) {
        this.victimOwner = victim.getOwner();
        this.victimHash = victim.hashCode();
        this.projectileId = p.getId();
        this.playerId = p.getOwner();
        this.damage = damage;
    }

    public void exec(GameModel model) {

    }

    public void onRecieve(GameClient client) {
        Entity e = client.getEntity(victimHash);
        if (e != null) {
            ((Destroyable) e).hit(damage);
            if (e.isDestroyed() && client.getPlayer().getPlayerShip() == e) {
                client.deliver(new PlayerKill(playerId));
            }
        }
    }

    public void onRecieve(GameServer server) {
        if(victimOwner == server.getServerId()) {
            Entity e = server.getEntity(victimHash);
            if (e != null) {
                ((Destroyable) e).hit(damage);
                server.broadcast(new EntityUpdate(e));
            }
        } else {
            server.send(this, victimOwner);
        }
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(projectileId);
        out.writeInt(victimOwner);
        out.writeInt(victimHash);
        out.writeDouble(damage);
    }

    @Override
    public Message read(ObjectInputStream in) throws IOException {
        ProjectileHit p = new ProjectileHit();
        p.playerId = in.readInt();
        p.projectileId = in.readInt();
        p.victimOwner = in.readInt();
        p.victimHash = in.readInt();
        p.damage = in.readDouble();
        return p;
    }

    public short getCode() {
        return code;
    }
}
