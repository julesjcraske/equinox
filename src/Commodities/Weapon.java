package Commodities;

/**
 * @author Julian Craske 
 */

import Vessel.Ship.SpaceShip;
import Vessel.*;
import Core.*;
import Projectiles.*;

public abstract class Weapon extends SubSystem
{
    private int gunFireDelay = 0;
    private int ammoRack;

    protected int minGunFireDelay;
    protected double barrelSpeed;
    protected double spread;
    protected int rackSize;
    protected int reloadRate;
    protected Projectile ammunition;
    protected String fireSound;
    
    public Weapon(String name, String fireSound, String description, double mass, int price,
            Projectile ammunition, int minGunFireDelay, double barrelSpeed,
            double spread, int rackSize, int reloadRate) {

        super(name, description, mass, price);
        this.ammunition = ammunition;
        this.minGunFireDelay = minGunFireDelay;
        this.barrelSpeed = barrelSpeed;
        this.spread = spread;
        this.rackSize = rackSize;
        this.reloadRate = reloadRate;
        this.fireSound = fireSound;
    }
    
    @Override
    public boolean equip(Vessel ship) {
       return ship.mountWeapon(this);
    }

    @Override
    public boolean unequip(Vessel ship) {
       return ship.unmountWeapon(this);
    }

    @Override
    public boolean activate(Vessel vessel, double positionAngle, double positionLength) {
        return false;
    }

    @Override
    public boolean deactivate(Vessel vessel, double positionAngle, double positionLength) {
        return false;
    }
    
    /**
     * Fire a bullet if the gun is ready.
     */
    public abstract Projectile fire(Entity origin, Entity target, double positionAngle, double positionLength);

    protected Projectile fire(Entity origin, Entity target, double angle, double positionAngle, double positionLength) {
        if(gunFireDelay >= minGunFireDelay && ammoRack > 0) {
            Projectile p = getProjectileType().clone();
            double dx = positionLength * Math.cos(Math.toRadians(positionAngle + origin.getRotation()));
            double dy = positionLength * Math.sin(Math.toRadians(positionAngle + origin.getRotation()));
            p.setLocation(origin.getX() + dx, origin.getY() + dy);
            p.setRotation(angle + ((Math.random() - 0.5) * spread * 2));
            p.increaseSpeed(p.getRotation(), barrelSpeed);
            p.initialise(origin);
            p.setTarget(target);
            p.create();
            ammoRack--;
            gunFireDelay = 0;
            origin.playSoundEffect(fireSound);
            return p;
        }
        return null;
    }
    
    public void tick(Vessel vessel) {
        gunFireDelay++;
        if(ammoRack == 0 && gunFireDelay >= reloadRate) {
            ammoRack = rackSize;
        }
    }

    public void reload() {
        ammoRack = 0;
    }
    
    public Projectile getProjectileType() {
        return ammunition;
    }

    public double getBarrelSpeed() {
        return barrelSpeed;
    }
    
    public abstract Weapon copy();
    
    public double getSpread() {
        return spread;
    }
    
    public void turnTowardAngle(double degrees) {
        
    }

    public boolean isTurret() {
        return false;
    }
}