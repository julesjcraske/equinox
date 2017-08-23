package Asteroids;

/**
 *
 * @author Julian Craske
 */

import Core.*;

public class HeliumAsteroid extends Asteroid {
    private static final double baseHelium = 100;
    private static final double maxRecharge = 30;
    private static final double rechargeRate = 0.001;

    private double helium = baseHelium;
    
    public HeliumAsteroid() {
        super("Helium Asteroid", "darkPlanet", 0.3);
    }

    @Override
    public void act() {
        turnLeft();
        if(helium < maxRecharge) {
            helium += rechargeRate;
        }
    }

    public double extractHelium(double quantity) {
        if(helium > 0) {
            if(helium < quantity) {
                quantity = helium;
                helium = 0;
                return quantity;
            } else {
                helium -= quantity;
                return quantity;
            }
        } else {
            return 0;
        }
    }

    public boolean hasSufficientHelium() {
        return helium > maxRecharge;
    }

    public boolean isEmpty() {
        return helium == 0;
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }

    @Override
    public boolean isHeliumAsteroid() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public Entity clone() {
        return new HeliumAsteroid();
    }
}
