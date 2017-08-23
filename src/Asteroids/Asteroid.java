package Asteroids;

/**
 *
 * @author Julian Craske
 */

import Core.*;

public abstract class Asteroid extends Entity {
    public Asteroid(String name, String image, double turnRate) {
        super(name, image, turnRate);
    }

    @Override
    public boolean isAsteroid() {
        return true;        
    }
    
    public boolean isHeliumAsteroid() {
        return false;
    }    

    @Override
    public boolean isCircular() {
        return true;
    }
}
