package Vessel.Ship;

/**
 *
 * @author Julian Craske
 */

import Core.Entity;
import Asteroids.*;
import Vessel.*;
import Commodities.*;

public class Miner extends SpaceShip {
    private static final double extractionRate = 0.2;
    private static final double miningDistance = 200;

    private int capacity = 60;
    private double load = 0;

    public Miner(Shield shield) {
        super("Miner", Vessel.UTILITY, "Generates credits for the team by mining helium asteroids.", 1000, "viper", 1.7, 250, 2000, 1, 0.2, 6, 0, shield);
    }

    public boolean mine(HeliumAsteroid roid) {
        if(load < capacity) {
            if(load + extractionRate < capacity) {
                load += roid.extractHelium(extractionRate);
            } else {
                load += roid.extractHelium(capacity - load);
            }
        }
        if(roid.isEmpty())  return false;
        else                return true;
    }

    @Override
    public void dock() {
        super.dock();
        load = 0;
    }

    public boolean isFull() {
        return load == capacity;
    }

    @Override
    public boolean isShip() {
        return true;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public Miner clone() {
        return new Miner(getShield());
    }

    public static double getMiningDistance() {
        return miningDistance;
    }
}
