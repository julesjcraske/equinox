package AI;

/**
 *
 * @author Julian Craske
 */

import Vessel.*;
import Core.*;
import Datastructures.*;

public class ComputerPilot extends Pilot {
    private int team;
    private int objective = IDLE;
    private int slide;
    private double inaccuracy = 0;
    private double accuracyMomentum = 0;
    private int counter = 0;

    //How close the pilot wants to get to his target
    private double aggression = Math.random() * 0.50 + 0.45;

    //The higher the skill the more accurate at shooting
    private double skill = Math.random() * 0.8 + 0.4;

    //How good the pilot is at leading ahead of the target
    private double foresight = Math.random() * 0.3 + 0.8;

    //How good the pilot is at compensating for his ships inertia
    private double piloting = Math.random() * 0.15 + 0.8;

    private static final int IDLE = 0;
    private static final int GOTO = 1;
    private static final int ATTACK = 2;
    private static final int DEFEND = 3;

    private static final double inaccuracyVariation = 0.1;
    private static final double maxInaccuracy = 20;

    public ComputerPilot(int team) {
        double p = Math.random();
        if(p < 0.4) {
            slide = Entity.STRAIGHT;
        } else if(p < 0.7) {
            slide = Entity.RIGHT;
        } else {
            slide = Entity.LEFT;
        }

        this.team = team;
        setDock(Team.getTeam(team).getPrimaryBase());
    }

    public double getAngleOfEngagement(Vessel ship, Entity target) {
        double range = ship.getDistanceToEntity(target);
        double attackVelocity = ship.getAttackVector(target).getLength();
        if(attackVelocity == 0) attackVelocity++;
        double acts = (range / attackVelocity);
        double x1 = ship.getSpeed().getX();
        double y1 = ship.getSpeed().getY();
        double x2 = target.getSpeed().getX();
        double y2 = target.getSpeed().getY();
        double targetAngle = ship.getAngleToXY(target.getX() + x2*acts*foresight - x1*acts*piloting,
                                                target.getY() + y2*acts*foresight - y1*acts*piloting);
        return targetAngle;
    }

    @Override
    public void control(Vessel ship) {        
        Entity target = ship.getTarget();

        switch(objective) {
            case IDLE:
                if(target != null) {
                    if(ship.getIsHostile(target))    objective = ATTACK;
                    else if(ship.getIsFriendly(target)) {
                        idle(ship);
                        //objective = DEFEND;
                    }
                    else idle(ship);
                }
                else idle(ship);
                break;

            case GOTO:
                if(target != null) {
                    ship.turnTowardAngle(getAngleOfEngagement(ship, target));
                    if(ship.getDistanceToEntity(target) < 250) {
                        target = null;
                        objective = IDLE;
                    } else {
                        ship.setThrusting(true);
                    }
                }
                else objective = IDLE;
                break;

            case ATTACK:
                if(target != null) {
                    if(ship.getIsHostile(target)) {
                        double angleOfAttack = getAngleOfEngagement(ship, target);
                        int aimedAtTarget = ship.turnTowardAngle(angleOfAttack + inaccuracy);
                        double distance = ship.getDistanceToEntity(target);
                        double range = ship.getWeaponRange(target);

                        ship.setThrusting(false);
                        ship.fireAfterburner(false);
                        if(distance < range) {
                            if(aimedAtTarget == Entity.STRAIGHT) {
                                ship.fire();
                                ship.fireMissiles();
                                double accuracyAdjust = (Math.random() - 0.5) * inaccuracyVariation + accuracyMomentum;
                                accuracyAdjust += inaccuracyVariation * (-inaccuracy / (maxInaccuracy / skill));
                                accuracyMomentum = accuracyAdjust;
                                inaccuracy += accuracyAdjust;
                            } else if(aimedAtTarget == Entity.LEFT) {
                                slide = Entity.LEFT;
                            } else if(aimedAtTarget == Entity.RIGHT) {
                                slide = Entity.RIGHT;
                            }
                            if(slide == Entity.LEFT && Math.random() < piloting)    ship.thrustRight();
                            if(slide == Entity.RIGHT && Math.random() < piloting)    ship.thrustLeft();                            
                        }
                        if(distance > aggression * range) {
                            ship.setThrusting(true);
                            //if(distance < range * 4) {
                                ship.fireAfterburner(true);
                            //}
                        } else {
                            ship.thrustReverse();
                        }
                    }
                }
                else objective = IDLE;
                break;

            case DEFEND:
                break;

            default:
                break;
        }
    }

    private void idle(Vessel ship) {
        ship.setThrusting(false);

        //Try to find a target
        ship.setTarget(getNextTarget(ship, new TargetFilter() {
            @Override
            public boolean filter(Entity self, Entity target) {
                return self.getIsHostile(target) && target.isDestroyable();
            }
        }));

        //Hyperspace if bored
        if(ship.getTarget() == null && Math.random() < 0.01) {
            int numLinks = ship.getSector().getNumLinks();
            int choice = (int) Math.random() * numLinks;
            numLinks = 0;
            for(Sector s : ship.getSector().getLinks()) {
                if(numLinks == choice) {
                    ship.hyperspace(s.getId());
                }
                numLinks++;
            }
            
        }
    }

    public static void main(String[] args) {

    }
}
