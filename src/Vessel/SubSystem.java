/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vessel;

import Commodities.*;

/**
 *
 * @author Julian Craske
 */
public abstract class SubSystem extends Commodity {
    public SubSystem(String name, String description, double mass, int price) {
        super(name, description, mass, price);
    }

    public abstract boolean activate(Vessel vessel, double positionAngle, double positionLength);

    public abstract boolean deactivate(Vessel vessel, double positionAngle, double positionLength);

    public abstract void tick(Vessel vessel);

    public abstract SubSystem copy();
}
