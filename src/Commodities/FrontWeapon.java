/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Commodities;

/**
 *
 * @author Julian Craske
 */

import Core.*;
import Projectiles.*;

public class FrontWeapon extends Weapon {
    public FrontWeapon(String name, String fireSound, String description, double mass, int price, Projectile ammunition, int minGunFireDelay, double barrelSpeed, double spread, int rackSize, int reloadRate) {
        super(name, fireSound, description, mass, price, ammunition, minGunFireDelay, barrelSpeed, spread, rackSize, reloadRate);
    }

    public Projectile fire(Entity origin, Entity target, double positionAngle, double positionLength) {
        return fire(origin, target, origin.getRotation(), positionAngle, positionLength);
    }

    @Override
    public Weapon copy() {
        return new FrontWeapon(getName(), fireSound, getDescription(), getMass(), getPrice(), ammunition, minGunFireDelay, barrelSpeed, spread, rackSize, reloadRate);
    }
}
