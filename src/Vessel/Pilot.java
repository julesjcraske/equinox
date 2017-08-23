package Vessel;

/**
 *
 * @author Julian Craske
 */

import Core.*;
import Datastructures.Team;
import Vessel.Structure.Structure;

import java.util.*;

public abstract class Pilot {
    private Dockable dock;
    private int targetNumber = 0;

    public abstract void control(Vessel vessel);

    public Entity getNextTarget(Entity collider, TargetFilter ... filters) {
        Collection<Entity> entities = collider.getVisibleEntities();
        ArrayList<Entity> filtered = new ArrayList<Entity>();
        boolean passFilter = true;
        for(Entity e : entities) {
            passFilter = true;
            for(TargetFilter filter : filters) {
                if(!filter.filter(collider, e)) {
                    passFilter = false;
                    break;
                }
            }
            if(passFilter) {
                filtered.add(e);
            }
        }
        if(filtered.size() > 0) {
            if(targetNumber >= filtered.size()) {
                targetNumber = 0;
            }
            Entity e = filtered.get(targetNumber);
            targetNumber++;
            return e;
        } else {
            return null;
        }
    }

    public Entity getClosest(Entity collider, TargetFilter ... filters) {
        Collection<Entity> entities = collider.getVisibleEntities();
        Entity closest = null;
        double distance = Double.MAX_VALUE, d;
        boolean passFilter = true;
        for(Entity e : entities) {
            passFilter = true;
            for(TargetFilter filter : filters) {
                if(!filter.filter(collider, e)) {
                    passFilter = false;
                    break;
                }
            }
            if(passFilter && (d = collider.getDistanceToEntity(e)) < distance) {
                closest = e;
                distance = d;
            }
        }
        return closest;
    }

    protected boolean dockAtBase(Vessel vessel) {
        Entity base = vessel.getTarget();        
        if(base == null) {
            vessel.setTarget(getClosest(vessel, new TargetFilter() {
                public boolean filter(Entity self, Entity target) {
                    return target.isDockable() && target.getTeamNo() == self.getTeamNo();
                }
            }));
        } else {
            if(!base.isDockable()) {
                vessel.setTarget(null);
                return false;
            }
            vessel.turnTowardAngle(vessel.getAngleToEntity(base));
            if(vessel.getDistanceToEntity(base) < base.getRadius()) {
                if(vessel.getSpeed().getLength() < 1) {
                    setDock((Dockable) base);
                    vessel.dock();
                    return true;
                }
            } else if(vessel.isFacing(vessel.getAngleToEntity(base), 10)){
                vessel.setThrusting(true);
            }
        }
        return false;
    }

    protected void launch(Vessel vessel) {
        if(getDock() == null) {
            Team team = Team.getTeam(vessel);
            if(team == null) {
                System.out.println("Vessel " + vessel.getName() + " : " + vessel.getTag() + " has not been assigned a team");
                return;
            }
            Collection<Structure> structures = team.getStrucures();
            for(Entity structure : structures) {
                if(structure.isDockable()) {
                    setDock((Dockable) structure);
                }
            }
        }
        vessel.getSpeed().stop();
        if(dock != null) {
            vessel.setLocation(dock.getLaunchLocation());
            vessel.setSector(dock.getSectorID());
            Core.Vector v = dock.getLaunchVector();
            vessel.getSpeed().add(v);
            vessel.setRotation(v.getDirection());
        }
    }

    public void setDock(Dockable dock) {
        this.dock = dock;
    }

    public Dockable getDock() {
        return dock;
    }
}
