package Vessel.Ship;

import Vessel.*;
import Commodities.*;
import java.util.*;
import java.awt.image.*;

import Datastructures.*;
import Core.*;

public class SpaceShip extends Vessel
{
    private int model;
    private int price = 0;
    private String description;

    private Weapon[] weapons = new Weapon[10];
    private Cloak cloak;
    private Afterburner booster;
    
    public SpaceShip(String name, short hullType, String description, int price, String imageFilePath, double turnRate, int maxHitPoints, int scanRange, double signiture, double acceleration, double maxSpeed, int numberOfWeapons, Commodity ... equipment) {
        super(name, imageFilePath, turnRate, hullType, maxHitPoints, scanRange, signiture, acceleration, maxSpeed);
        init(hullType, description, price, imageFilePath, numberOfWeapons);
        for(Commodity e : equipment) {
            boolean equipped = e.copy().equip(this);
        }
    }

    public SpaceShip(String name, short hullType, String description, int price, String imageFilePath, double turnRate, int maxHitPoints, int scanRange, double signiture, double acceleration, double maxSpeed, int numberOfWeapons,  Collection<Commodity> equipment) {
        super(name, imageFilePath, turnRate, hullType, maxHitPoints, scanRange, signiture, acceleration, maxSpeed);
        init(hullType, description, price, imageFilePath, numberOfWeapons);
        for(Commodity e : equipment) {
            boolean equipped = e.copy().equip(this);
        }
    }

    protected final void init(short hullType, String description, int price, String imageFilePath, int numberOfWeapons) {
        model = imageFilePath.hashCode();
        this.description = description;
        this.price = price;
    }

    @Override
    public BufferedImage getImage() {
        if(image == SHIP) {
            return ImagePool.getImage(getImagePath());
        } else {
            return ImagePool.getImage(getImagePath() + "_thrust");
        }
    }

    @Override
    public void fire() {
        for(int i = 0; i < 4; i++) {
            Weapon w = weapons[i];
            if(w != null) {
                Mount m = ShipModel.getModel(model).getMount(i);
                w.fire(this, getTarget(), m.getAngle(), m.getLength());
            }
        }
    }

    @Override
    public void fireMissiles() {
        for(int i = 4; i < 6; i++) {
            Weapon w = weapons[i];
            if(w != null) {
                Mount m = ShipModel.getModel(model).getMount(i);
                w.fire(this, getTarget(), m.getAngle(), m.getLength());
            }
        }
    }

    @Override
    public double getWeaponRange(Entity target) {
        return getAttackVector(target).getLength() * getPrimaryWeapon().getProjectileType().getLife();
    }

    @Override
    public boolean isShip() {
        return true;
    }
    
    @Override
    protected void systemTick() {
        super.systemTick();
        for(Weapon w : weapons) {
            if(w != null) {
                w.tick(this);
            }
        }
        if(cloak != null) {
            cloak.tick(this);
        }
        if(booster != null) {
            booster.tick(this);
        }
        if(getTarget() != null && (getTarget().isDestroyed() || !isWithinScanRange(getTarget()))) {
            setTarget(null);
        }
    }   

    public List<Commodity> getEquipment() {
        LinkedList<Commodity> equipment = new LinkedList<Commodity>();
        for(Weapon w : weapons) {
            if(w != null) {
                equipment.add(w);
            }
        }
        if(getShield() != null) {
            equipment.add(getShield());
        }
        if(cloak != null) {
            equipment.add(cloak);
        }
        if(booster != null) {
            equipment.add(booster);
        }
        return equipment;
    }
    
    @Override
    public int getSize() {
        return 2;
    }
    
    public String getDescription() {
        return description;
    }  
    
    public int getPrice() {
        int fullPrice = this.price;
        for(Commodity c : getEquipment()) {
            fullPrice += c.getPrice();
        }
        return fullPrice;
    }

    @Override
    public Core.Vector getAttackVector(Entity target) {
        Core.Vector vector = new Core.Vector(getRotation(), getPrimaryWeapon().getBarrelSpeed());
        vector.add(getSpeed());
        vector.minus(target.getSpeed());
        return vector;
    }

    public boolean mountWeapon(Weapon w) {
        int min = 0;
        int max = 4;
        if(w.getProjectileType().isMissile()) {
            min = 4;
            max = 6;
        } else if(w.isTurret()) {
            min = 6;
            max = 10;
        }
        for(int i = min; i < max; i++) {
            if(weapons[i] == null) {
                weapons[i] = w;
                return true;
            }
        }
        return false;
    }

    public boolean mountCloak(Cloak c) {
        if(cloak == null) {
            cloak = c;
            return true;
        }
        return false;
    }

    public boolean unmountWeapon(Weapon w) {
        for(int i = 0; i < weapons.length; i++) {
            if(weapons[i] == w) {
                weapons[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean unmountCloak(Cloak c) {
        if(cloak == c) {
            cloak = null;
            return true;
        }
        return false;
    }

    

    @Override
    public void turnTurretsTowardAngle(double degrees) {
        for(int i = 6; i < 10; i++) {
            Weapon w = weapons[i];
            if(w != null) {
                w.turnTowardAngle(degrees);
            }
        }
    }
    
    @Override
    public void fireTurrets() {
        for(int i = 6; i < 10; i++) {
            Weapon w = weapons[i];
            if(w != null) {
                Mount m = ShipModel.getModel(model).getMount(i);
                w.fire(this, getTarget(), m.getAngle(), m.getLength());
            }
        }
    }

    @Override
    public boolean toggleCloak() {
        if(cloak != null && cloak.isReady()) {
            if(cloak.isActive()) {
                cloak.deactivate(null, 0, 0);
            } else {
                cloak.activate(null, 0, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setCloaked(boolean c) {
        if(c) {
            cloak();
        } else {
            decloak();
        }
    }
    
    public void cloak() {
        if(cloak != null) {
            cloak.activate(null, 0, 0);
        }
    }
    
    public void decloak() {
        if(cloak != null) {
            cloak.deactivate(null, 0, 0);
        }
    }

    @Override
    public boolean isCloaked() {
        if(cloak != null) {
            return cloak.isActive();
        }
        return false;
    }
    
    public boolean mountAfterburner(Afterburner b) {
        if(booster == null) {
            booster = b;
            return true;
        }
        return false;
    }
    
    public boolean unmountAfterburner(Afterburner b) {
        if(booster == b) {
            booster = null;
            return true;
        }
        return false;
    }

    @Override
    public void fireAfterburner(boolean b) {
        if(booster != null) {
            if(b) {
                booster.activate(this, 0, 0);
            } else {
                booster.deactivate(this, 0, 0);
            }
        }
    }

    @Override
    public boolean isBoosting() {
        if(booster != null) {
            return booster.isInUse();
        }
        return false;
    }

    @Override
    public void setBoosting(boolean b) {
        if(booster != null) {
            booster.setInUse(b);
        }
    }

    public boolean hasTurrets() {
        for(int i = 6; i < 10; i++) {
            Weapon w = weapons[i];
            if(w != null) {
                return true;
            }
        }
        return false;
    }
    
    public Weapon[] getWeapons() {
        return weapons;
    }

    public Weapon getPrimaryWeapon() {
        return weapons[0];
    }

    public boolean isFighter() {
        return getHullType() == Vessel.FIGHTER;
    }
    
    public boolean equals(SpaceShip s) {
        return s.getName().equals(getName());
    }
    
    public SpaceShip clone() {
        SpaceShip s = new SpaceShip(getName(), getHullType(), description, price, getImagePath(), getTurnRate(), getMaxHitPoints(), getScanRange(), getBaseSigniture(), getAcceleration(), getMaxSpeed(), weapons.length, getEquipment());
        return s;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    public String getFullDescription() {
        String s = getName() + 
                   "\n  " + description + 
                   "\n\nTurn Rate: " + getTurnRate() +
                   "\nArmor: " + getMaxHitPoints() +
                   "\nAcceleration: " + getAcceleration() +
                   "\nMax Speed: " + getMaxSpeed() +
                   "\nScan Range: " + getScanRange() +
                   "\nNumber of Weapons: " + weapons.length +
                   "\n\nEquipment:";
        for(Commodity c : getEquipment()) {
            s += "\n    " + c.getName();
        }
        return s;
    }                   
}
