package Messages;

/**
 *
 * @author Julian Craske
 */

import java.io.*;

import Core.Entity;
import Game.*;

public class EntityUpdate extends Message {
    private static final short code = 201;

    private int hash, image, sector;
    private double x, y, rotation;
    private double dx, dy;
    private double healthPercentage, shieldPercentage;
    private double signiture;
    private boolean thrusting, cloaked, boosting;

    public EntityUpdate() {

    }

    public EntityUpdate(Entity e) {
        this.playerId = e.getOwner();
        this.hash = e.hashCode();
        this.image = e.getImageNo();
        this.sector = e.getSectorID();
        this.x = e.getX();
        this.y = e.getY();
        this.rotation = e.getRotation();
        this.dx = e.getSpeed().getX();
        this.dy = e.getSpeed().getY();
        this.healthPercentage = e.getHealthPercentage();
        this.shieldPercentage = e.getShieldPercentage();
        this.signiture = e.getSigniture();
        this.thrusting = e.isThrusting();
        this.cloaked = e.isCloaked();
        this.boosting = e.isBoosting();
    }

    public void apply(Entity e) {
        if(e != null) {
            e.setImageNo(image);
            e.setSector(sector);
            e.setLocation(x,y);
            e.getSpeed().setXY(dx, dy);
            e.setRotation(rotation);
            e.setHealthPercentage(healthPercentage);
            e.setShieldPercentage(shieldPercentage);
            e.setSigniture(signiture);
            e.setThrusting(thrusting);
            e.setCloaked(cloaked);
            e.setBoosting(boosting);
        }
    }

    public EntityUpdate read(ObjectInputStream in) throws IOException {
        EntityUpdate e = new EntityUpdate();
        e.playerId = in.readInt();
        e.hash = in.readInt();
        e.image = in.readInt();
        e.sector = in.readInt();
        e.x = in.readDouble();
        e.y = in.readDouble();
        e.dx = in.readDouble();
        e.dy = in.readDouble();
        e.rotation = in.readDouble();
        e.healthPercentage = in.readDouble();
        e.shieldPercentage = in.readDouble();
        e.signiture = in.readDouble();
        e.thrusting = in.readBoolean();
        e.cloaked = in.readBoolean();
        e.boosting = in.readBoolean();
        return e;
    }

    public void write(ObjectOutputStream out) throws IOException {
        out.writeShort(code);
        out.writeInt(playerId);
        out.writeInt(hash);
        out.writeInt(image);
        out.writeInt(sector);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(dx);
        out.writeDouble(dy);
        out.writeDouble(rotation);
        out.writeDouble(healthPercentage);
        out.writeDouble(shieldPercentage);
        out.writeDouble(signiture);
        out.writeBoolean(thrusting);
        out.writeBoolean(cloaked);
        out.writeBoolean(boosting);
    }

    public void exec(GameModel model) {
        apply(model.getEntity(hash));
    }

    public void onRecieve(GameClient client) {

    }

    public void onRecieve(GameServer server) {
        server.broadcast(this, playerId);
    }

    public short getCode() {
        return code;
    }
}
