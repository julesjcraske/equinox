package AI;

/**
 *
 * @author Julian Craske
 */

import Vessel.Ship.Miner;
import Core.*;
import Vessel.*;
import Asteroids.*;

public class MinerPilot extends Pilot {
    private static final int dockTime = 100;
    private int counter = 0;

    @Override
    public void control(Vessel vessel) {
        vessel.setThrusting(false);
        try {
            Miner miner = (Miner) vessel;
            if(miner.isDocked()) {
                counter++;
                if(counter > dockTime) {
                    launch(vessel);
                    counter = 0;
                }
            } else {
                if(miner.isFull()) {
                    dockAtBase(vessel);
                } else {
                    mineHeliumRock(miner);
                }
            }
        } catch(ClassCastException ex) {
            System.out.println("Miner pilot is not in a miner: " + vessel.getName());
        }
    }

    private void mineHeliumRock(Miner miner) {
        Entity target = miner.getTarget();

        //Find nearest helium rock
        if(miner.getTarget() == null) {
            miner.setTarget(getClosest(miner, new TargetFilter() {
                public boolean filter(Entity self, Entity target) {
                    if(target.isAsteroid()) {
                        if(((Asteroid) target).isHeliumAsteroid()) {
                            if(((HeliumAsteroid) target).hasSufficientHelium()) return true;
                        }
                    }
                    return false;
                }
            }));

        //Mine current target
        } else {
            int facing = miner.turnTowardAngle(miner.getAngleToEntity(target));
            double distance = miner.getDistanceToEntity(target);
            if(distance < Miner.getMiningDistance()) {
                if(facing == miner.STRAIGHT) {
                    try {
                        boolean mining = miner.mine((HeliumAsteroid) target);
                        if(!mining) miner.setTarget(null);
                        if(distance < Miner.getMiningDistance() * 0.9) {
                            miner.thrustLeft();
                        }
                    } catch(ClassCastException ex) {
                        miner.setTarget(null);
                    }
                }
            } else if(facing == miner.STRAIGHT) {
                miner.setThrusting(true);
            }            
        }
    }
}
