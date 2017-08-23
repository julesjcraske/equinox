package Commodities;

import Vessel.Ship.SpaceShip;
import Core.*;
import Vessel.*;

public class Afterburner extends SubSystem
{
    private static final int cloudsPerAct = 2;

    private double acceleration;
    private int fuelCapacity;
    private double fuel;
    private double rechargeRate;
    private boolean maxedOut = false;
    private boolean inUse = false;    
    
    public Afterburner(String name, String description, double mass, int price, double acceleration, int fuelCapacity, double rechargeRate)
    {
        super(name, description, mass, price);
        this.acceleration = acceleration;
        this.fuelCapacity = fuelCapacity;
        fuel = fuelCapacity;
        this.rechargeRate = rechargeRate;
    }
    
    public boolean activate(Vessel vessel, double positionAngle, double positionLength) {
        if(fuel > 1 && !maxedOut) {
            vessel.increaseSpeed(vessel.getRotation(), acceleration);
            fuel--;
            inUse = true;
            return true;
        } else {
            maxedOut = true;
            inUse = false;
            return false;
        }        
    }
    
    public boolean deactivate(Vessel vessel, double positionAngle, double positionLength) {
        maxedOut = false;
        inUse = false;
        return true;
    }
    
    public void tick(Vessel vessel) {
        if(inUse) {
            for(int i = 0; i < cloudsPerAct; i++) {
                AfterburnerCloud cloud = new AfterburnerCloud();
                cloud.initialise(vessel);
                cloud.createAsVisualEffect();
                vessel.playSoundEffectIfFocus("afterburner.wav");
            }
        } else if(fuel < fuelCapacity) {
            fuel += rechargeRate;
        }
        if(fuel > fuelCapacity)     fuel = fuelCapacity;
    }
    
    @Override
    public boolean equip(Vessel ship) {
        return ship.mountAfterburner(this);
    }
    
    @Override
    public boolean unequip(Vessel ship) {
        return ship.unmountAfterburner(this);
    }

    public double getFuel() {
        return fuel;
    }
    
    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean b) {
        inUse = b;
    }
    
    public Afterburner copy() {
        return new Afterburner(getName(), getDescription(), getMass(), getPrice(), acceleration, fuelCapacity, rechargeRate);
    }
    
    class AfterburnerCloud extends Entity {
        private static final int maxLife = 3;
        private static final double spread = 70;
        private static final double originSpread = 26;
        private static final double cloudVelocity = 2.4;

        private int life = 0;
        
        AfterburnerCloud() {
            super("Afterburner Cloud", "afterburnerCloud", 0);
        }
        
        public void act() {
            if(life < maxLife) {
                move();
                life++;
            }
            else {
                destroy();
            }
        }
        
        public void initialise(Vessel origin)
        {
            if(origin != null) {
                double originAngle = Math.random() * originSpread - originSpread / 2;
                double dx = origin.getImage().getWidth() / 2 * Math.cos(Math.toRadians(180 + originAngle + origin.getRotation()));
                double dy = origin.getImage().getWidth() / 2 * Math.sin(Math.toRadians(180 + originAngle + origin.getRotation()));
                setLocation(origin.getX() + dx, origin.getY() + dy);
                setRotation(origin.getRotation() + 180 + (Math.random() - 0.5) * spread);
                increaseSpeed(getRotation(), cloudVelocity);
                increaseSpeed(origin.getSpeed());
                setSector(origin.getSectorID());
            }
        }

        public AfterburnerCloud clone() {
            return new AfterburnerCloud();
        }
    }
}
