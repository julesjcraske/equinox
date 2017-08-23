package Datastructures;

/**
 *
 * @author Julian Craske
 */

import AI.ComputerPilot;
import Vessel.Ship.SpaceShip;
import Vessel.Structure.Structure;
import Core.Entity;
import Core.EntityListener;

import Vessel.Structure.Base;
import java.util.*;
import java.awt.*;
import java.util.List;

public class Team implements EntityListener {
    private static HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
    private static Color[] teamColors = {Color.WHITE, Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW};
    static {
        setNumberOfTeams(2);
    }

    private int teamNo;
    private ArrayList<Structure> structures = new ArrayList<Structure>();
    private ArrayList<SpaceShip> availiableShips = new ArrayList<SpaceShip>();
    private ArrayList<SpaceShip> activeShips = new ArrayList<SpaceShip>();

    public Team(int teamNo) {
        this.teamNo = teamNo;
        //Create the selectable list of availiable ships
        for(Entity e : Templates.getEntities()) {
            if(e.isShip()) {
                if(((SpaceShip) e).isFighter()) {
                    availiableShips.add((SpaceShip) e);
                }
            }
        }
    }
    
    public static void setNumberOfTeams(int noTeams) {
        teams.clear();
        for(int i = 1; i <= noTeams; i++) {
            addTeam(new Team(i));
        }
    }

    public static Color getTeamColor(int teamNo) {
        if(teamNo >= 0 && teamNo < teamColors.length) {
            return teamColors[teamNo];
        } else {
            System.out.println("Team " + teamNo + " does not have a color.");
            return Color.GRAY;
        }
    }

    public void spawnShips() {
        if(activeShips.size() < 2) {
            for(Structure struct : structures) {
                if(struct.isBase()) {
                    Base base = (Base) struct;
                    int index = getAvailiableShips().size();
                    index = (int) Math.floor(Math.random() * index);
                    SpaceShip ship = getAvailiableShips().get(index).clone();
                    ship.setTeam(teamNo);
                    ship.setSector(base.getSectorID());
                    ship.setLocation(base.getLaunchLocation());
                    Core.Vector v = base.getLaunchVector();
                    ship.setRotation(v.getDirection());
                    ship.increaseSpeed(v);
                    ship.setPilot(new ComputerPilot(teamNo));
                    ship.create();
                }
            }
        }
    }

    public List<SpaceShip> getAvailiableShips() {
        return availiableShips;
    }

    public List<Structure> getStrucures() {
        return structures;
    }

    public List<SpaceShip> getActiveShips() {
        return activeShips;
    }

    public Base getPrimaryBase() {
        for(Structure s : structures) {
            if(s.isBase()) {
                return (Base) s;
            }
        }
        return null;
    }

    public void addShip(String name) {
        try {
            SpaceShip ship = (SpaceShip) Templates.getEntity(name);
            availiableShips.add(ship);
        } catch(ClassCastException e) {

        }
    }

    private void add(Entity e) {
        if(e.getTeamNo() == teamNo) {
            if(e.isShip()) {
                activeShips.add((SpaceShip) e);
            } else if(e.isStructure()) {
                structures.add((Structure) e);
            }
        }
    }

    private void remove(Entity e) {
        if(e.getTeamNo() == teamNo) {
            if(e.isShip()) {
                activeShips.remove((SpaceShip) e);
            } else if(e.isStructure()) {
                structures.remove((Structure) e);
            }
        }
    }

    public void onCreation(Entity e) {
        add(e);
    }

    public void onRemoteReceipt(Entity e) {
        add(e);
    }

    public void onDestruction(Entity e) {
        remove(e);
    }

    public void onVisualEffectCreation(Entity e) {
        //Visual effects do not affect team behaviour.
    }

    public void onSectorChange(Entity e, int oldSectorId) {

    }

    public static void addTeam(Team team) {
        teams.put(team.teamNo, team);
        Entity.addEntityListener(team);
    }

    public static Team getTeam(Entity e) {
        return getTeam(e.getTeamNo());
    }

    public static Team getTeam(int teamNo) {
        return teams.get(teamNo);
    }

    public static Collection<Team> getTeams() {
        return teams.values();
    }
}
