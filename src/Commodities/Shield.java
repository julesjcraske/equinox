package Commodities;

import Vessel.Ship.SpaceShip;
import Vessel.*;

public class Shield extends SubSystem
{
    private static int downtime = 100;

    private int counter = 0;
    private double shield;
    private int maxShield;
    private double regenRate;
    private boolean active = true;
    
    public Shield(String name, String description, double mass, int price, int maxShield, double regenRate)
    {
        super(name, description, mass, price);
        shield = maxShield;
        this.maxShield = maxShield;
        this.regenRate = regenRate;
    }
        
    public double damage(Vessel vessel, double damage) {
        if(active) {
            double excess = 0;
            if(shield < damage) {
                excess = damage - shield;
                shield = 0;
                vessel.playSoundEffectIfFocus("noshld.wav");
                active = false;
                counter = 0;
            } else {
                shield -= damage;
            }
            return excess;
        } else {
            return damage;
        }
    }
    
    public void tick(Vessel vessel) {
        if(active) {
            if(shield < maxShield) {
                shield += regenRate;
                if(shield > maxShield) {
                    shield = maxShield;
                }
            }
        } else {
            counter++;
            if(counter > downtime) {
                active = true;
            }
        }
    } 
    
    public double getShield() {
        return shield;
    }

    public void setShield(double percentage) {
        shield = maxShield * percentage;
        if(shield > maxShield) {
            shield = maxShield;
        }
    }
       
    public int getMaxShield() {
       return maxShield;
    }
    
    public double getRegenRate() {
        return regenRate;
    }
    
    public Shield copy() {
        return new Shield(getName(), getDescription(), getMass(), getPrice(), maxShield, regenRate);
    }

    @Override
    public boolean equip(Vessel ship) {
        return ship.mountShield(this);
    }

    @Override
    public boolean unequip(Vessel ship) {
        return ship.unmountShield(this);
    }

    @Override
    public boolean activate(Vessel vessel, double positionAngle, double positionLength) {
        return false;
    }

    @Override
    public boolean deactivate(Vessel vessel, double positionAngle, double positionLength) {
        return false;
    }
}
