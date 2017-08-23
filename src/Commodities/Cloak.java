/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Commodities;

/**
 *
 * @author Julian Craske
 */

import Vessel.Ship.SpaceShip;
import Vessel.*;

public class Cloak extends SubSystem {
    private boolean active = false;
    private double activity = 0;
    private double power;

    public Cloak(String name, String description, double mass, int price, double power) {
        super(name, description, mass, price);
        this.power = power;
    }

    @Override
    public boolean equip(Vessel ship) {
        return ship.mountCloak(this);
    }

    public boolean unequip(Vessel ship) {
        return ship.unmountCloak(this);
    }

    @Override
    public boolean activate(Vessel vessel, double positionAngle, double positionLength) {
        active = true;
        return true;
    }

    @Override
    public boolean deactivate(Vessel vessel, double positionAngle, double positionLength) {
        active = false;
        return true;
    }

    @Override
    public void tick(Vessel vessel) {
        if(active) {
            activity = Math.min(activity + 0.025, 1);
        } else {
            activity = Math.max(activity - 0.025, 0);
        }
        vessel.setSigniture(vessel.getBaseSigniture() * (1 - power * activity));
    }

    @Override
    public SubSystem copy() {
        return new Cloak(getName(), getDescription(), getMass(), getPrice(), power);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isReady() {
        return activity == 0 || activity == 1;
    }
}
