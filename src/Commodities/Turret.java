/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Commodities;

import Core.Entity;
import Projectiles.Projectile;

/**
 *
 * @author Julian Craske
 */
public class Turret extends Weapon {
    private double rotation = 0;

    public Turret(String name, String fireSound, String description, double mass, int price, Projectile ammunition, int minGunFireDelay, double barrelSpeed, double spread, int rackSize, int reloadRate) {
        super(name, fireSound, description, mass, price, ammunition, minGunFireDelay, barrelSpeed, spread, rackSize, reloadRate);
    }

    @Override
    public Projectile fire(Entity origin, Entity target, double positionAngle, double positionLength) {
        return fire(origin, target, rotation, positionAngle, positionLength);
    }

    @Override
    public Weapon copy() {
        return new Turret(getName(), fireSound, getDescription(), getMass(), getPrice(), ammunition, minGunFireDelay, barrelSpeed, spread, rackSize, reloadRate);
    }

    @Override
    public void turnTowardAngle(double degrees) {
        rotation = degrees;
    }

    @Override
    public boolean isTurret() {
        return true;
    }
}
