package Vessel.Structure;

/**
 *
 * @author Julian Craske
 */

import Core.Vector;
import Vessel.Dockable;
import Vessel.Ship.*;

import java.awt.geom.Point2D;

public class Base extends Structure implements Dockable {
    private static double undockVelocity = 4;

    private SpaceShip[] suppliedShips;

    public Base(String name, String image, int maxHitPoints, int scanRange, double signiture, SpaceShip ... suppliedShips) {
        super(name, image, maxHitPoints, scanRange, signiture);
        this.suppliedShips = suppliedShips;
    }

    @Override
    public boolean isBase() {
        return true;
    }

    public SpaceShip[] getSuppliedShips() {
        return suppliedShips;
    }

    @Override
    public boolean isDockable() {
        return true;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    public Point2D getLaunchLocation() {
        return getLocation();
    }

    public Vector getLaunchVector() {
        return new Vector(Math.random() * 360, undockVelocity);
    }

    public Base clone() {
        return new Base(getName(), getImagePath(), getMaxHitPoints(), getScanRange(), getBaseSigniture(), suppliedShips);
    }
}
