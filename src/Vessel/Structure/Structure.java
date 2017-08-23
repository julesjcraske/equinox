package Vessel.Structure;

/**
 *
 * @author Julian Craske
 */

import Vessel.Vessel;

public abstract class Structure extends Vessel {

    public Structure(String name, String image, int maxHitPoints, int scanRange, double signiture) {
        super(name, image, 0, Vessel.CAPITAL, maxHitPoints, scanRange, signiture, 0, 0);
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public boolean isBase() {
        return false;
    }

    @Override
    public boolean isStructure() {
        return true;
    }

    public boolean isDestroyable() {
        return false;
    }

    public int getSize() {
        return 5;
    }

    public void hit(double damage) {

    }
}
