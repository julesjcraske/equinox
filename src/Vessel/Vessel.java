package Vessel;

/**
 *
 * @author Julian Craske
 */

import Commodities.Afterburner;
import Commodities.Cloak;
import Commodities.Weapon;
import Core.*;
import Commodities.Shield;
import Projectiles.*;

public abstract class Vessel extends Entity implements Destroyable {
    public static short FIGHTER = 0;
    public static short CORVETTE = 1;
    public static short FRIGATE = 2;
    public static short CAPITAL = 3;
    public static short UTILITY = 4;

    protected static int SHIP = 0;
    protected static int SHIP_WITH_THRUST = 1;

    private static double hyperspaceAcceleration = 3.2;
    private static double hyperspaceJumpBarrier = 200;

    private static double sideThrust = 0.6;
    private static double backThrust = 0.4;

    private Pilot pilot;
    private Shield shield;
    private Entity target = null;

    private boolean thrusting = false;
    private boolean hyperspacing = false;
    private int hyperspaceDestination;

    private double baseSigniture = 1;
    private int maxHull;
    private double hull;
    private short hullType;

    private double acceleration;
    private double maxSpeed;

    public Vessel(String name, String imageFilePath, double turnRate, short hullType, int maxHitPoints,
            int scanRange, double baseSigniture, double acceleration, double maxSpeed) {

        super(name, imageFilePath, turnRate);
        this.hullType = hullType;
        this.hull = maxHitPoints;
        this.maxHull = maxHitPoints;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        setScanRange(scanRange);
        this.baseSigniture = baseSigniture;
        setSigniture(baseSigniture);
        setExplosion("explosion");
    }
    
    public void act() {   
        systemTick();
        move();
        if(isHyperspacing()) {
            hyperspace();
        } else {
            drag();
            thrust(thrusting);
            if(pilot != null) {
                pilot.control(this);
            }
        }
    }
    
    protected void systemTick() {
        if(shield != null) {
            shield.tick(this);
        }
    }

    public void fire() {

    }

    public Vector getAttackVector(Entity target) {
        return getSpeed();
    }

    public double getWeaponRange(Entity target) {
        return 0;
    }

    @Override
    public void drag() {
        if(getSpeed().getLength() != 0) {
            decreaseSpeed(getAcceleration() * (1 - Math.exp(-getSpeed().getLength() / getMaxSpeed())));
        }
    }

    @Override
    public void setThrusting(boolean b) {
        thrusting = b;
    }

    @Override
    public boolean isThrusting() {
        return thrusting;
    }

    private void thrust(boolean b) {
        b = b || isBoosting();
        thrust(b, getAcceleration());
    }

    private void thrust(boolean b, double acceleration) {
        if(b) {
            increaseSpeed(getRotation(), acceleration);
            image = SHIP_WITH_THRUST;
        }
        else {
            image = SHIP;
        }
    }

    public void thrustLeft() {
        increaseSpeed((getRotation() + 270) % 360, getAcceleration() * sideThrust);
    }

    public void thrustRight() {
        increaseSpeed((getRotation() + 90) % 360, getAcceleration() * sideThrust);
    }

    public void thrustReverse() {
        increaseSpeed((getRotation() + 180) % 360, getAcceleration() * backThrust);
    }

    protected void hyperspace() {
        double angle = Sector.getSector(getSectorID()).getAngleToSector(Sector.getSector(hyperspaceDestination));
        if(turnTowardAngle(angle) == Entity.STRAIGHT) {
            thrust(true, hyperspaceAcceleration);
        }
        if(getSpeed().getLength() > hyperspaceJumpBarrier) {
            setSector(hyperspaceDestination);
            Core.Vector v = new Core.Vector(angle + 180 + (Math.random()-0.5) * 25 , Sector.getSector(getSectorID()).getHyperspaceDistance());
            setLocation(v.getX(), v.getY());
            hyperspacing = false;
        }
    }

    public void hyperspace(int sectorId) {
        hyperspacing = true;
        hyperspaceDestination = sectorId;
    }

    public boolean isHyperspacing() {
        return hyperspacing;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }
    
    @Override
    public void setTarget(Entity target) {
        this.target = target;
    }

    @Override
    public Entity getTarget() {
        return target;
    }

    public void turnTurretsTowardAngle(double angle) {

    }

    public void fireTurrets() {

    }

    public void fireMissiles() {

    }

    public boolean toggleCloak() {
        return false;
    }

    public void fireAfterburner(boolean b) {

    }

    public Shield getShield() {
        return shield;
    }

    public boolean mountShield(Shield s) {
        if(shield == null) {
            shield = s;
            return true;
        }
        return false;
    }

    public boolean unmountShield(Shield s) {
        if(shield == s) {
            shield = null;
            return true;
        }
        return false;
    }

    public void dock() {
        setSector(-1);
        playSoundEffectIfFocus("landed.wav");
    }

    public boolean isDocked() {
        return getSectorID() == -1;
    }

    public double getBaseSigniture() {
        return baseSigniture;
    }

    public short getHullType() {
        return hullType;
    }

    public double getHitPoints() {
        return hull;
    }

    public int getMaxHitPoints() {
        return maxHull;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    @Override
    public void destroy() {
        hull = 0;
        super.destroy();
        explode();
    }

    /**
     * Destroyable methods
     */

    public void hit(double damage) {
        if(shield != null) {
            damage = shield.damage(this, damage);
        }
        if(damage > 0) {
            hull -= damage;
            playSoundEffect("hitmearmor1.wav");
        } else {
            playSoundEffect("hitmeshield1.wav");
        }
        if(hull <= 0) {
            destroy();
        }
    }

    public void repair(double damage) {
        hull += damage;
        if(hull > maxHull) hull = maxHull;
    }

    @Override
    public void setHealthPercentage(double percentage) {
        hull = maxHull * percentage;
        if(hull <= 0) {
            destroy();
        } else if(hull > maxHull) {
            hull = maxHull;
        }
    }

    @Override
    public double getHealthPercentage() {
        return hull / maxHull;
    }

    @Override
    public double getRemainingHitPoints() {
        return super.getRemainingHitPoints() + (shield != null? shield.getShield() : 0);
    }

    @Override
    public double getShieldPercentage() {
        if(shield != null) {
            return shield.getShield() / shield.getMaxShield();
        }
        return 0;
    }

    public double getShieldPoints() {
        if(shield != null) {
            return shield.getShield();
        }
        return 0;
    }

    public int getMaxShieldPoints() {
        if(shield != null) {
            return shield.getMaxShield();
        }
        return 0;
    }

    @Override
    public void setShieldPercentage(double percentage) {
        if(shield != null) {
            shield.setShield(percentage);
        }
    }

    public boolean isDestroyable() {
        return true;
    }

    public boolean unmountWeapon(Weapon w) {
        return false;
    }

    public boolean mountWeapon(Weapon w) {
        return false;
    }

    public boolean mountCloak(Cloak aThis) {
        return false;
    }

    public boolean unmountCloak(Cloak aThis) {
        return false;
    }

    public boolean unmountAfterburner(Afterburner aThis) {
        return false;
    }

    public boolean mountAfterburner(Afterburner aThis) {
        return false;
    }
}
