package unused;

/**
 *
 * @author Julian Craske
 */

import Vessel.*;
import java.util.*;

import Commodities.*;

public abstract class CargoCarrier extends Vessel {
    private int cargoSpace;
    private LinkedList<CommodityQuantity> cargo = new LinkedList<CommodityQuantity>();

    public CargoCarrier(String name, String imageFilePath, double turnRate, short hullType, int maxHitPoints,
            int scanRange, double signiture, double acceleration, double maxSpeed, int cargoSpace) {

        super(name, imageFilePath, turnRate, hullType, maxHitPoints, scanRange, signiture, acceleration, maxSpeed);
        this.cargoSpace = cargoSpace;
    }

    public double getCargoMass() {
        double mass = 0;
        for(CommodityQuantity c : cargo) {
            mass += c.getTotalMass();
        }
        return mass;
    }

    public boolean modifyCargo(Commodity c, int quantity) {
        boolean loaded = false;
        if(c != null) {
            if(c.getMass() * quantity + getCargoMass() <= cargoSpace) {
                Iterator<CommodityQuantity> it = cargo.iterator();
                CommodityQuantity commod;
                while(it.hasNext()) {
                    commod = it.next();
                    if(commod.getCommodity().equals(c)) {
                        loaded = commod.modifyQuantity(quantity);
                        if(commod.getQuantity() == 0) it.remove();
                    }
                }
                if(!loaded && quantity > 0) {
                    cargo.add(new CommodityQuantity(c, quantity));
                    loaded = true;
                }
            }
        }
        return loaded;
    }

    public LinkedList<CommodityQuantity> getCargo() {
        return cargo;
    }

    public int getCargoSpace() {
        return cargoSpace;
    }
}
