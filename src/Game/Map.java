package Game;

/**
 *
 * @author Julian Craske
 */

import java.util.*;

import Core.*;

public class Map {
    private static HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
    static {
        loadMaps();
    }

    private String name;
    private HashMap<Integer, Sector> sectors = new HashMap<Integer, Sector>();
    private int uniqueCounter = 0;

    public static Map getDefaultMap() {
        return Frontier();
    }

    public static Map Brawl() {
        Map m = new Map();
        m.name = "Brawl";
        Sector sol = new Sector("Brawl", 0, 0);
        sol.setTeamHomes(1, 2);
        m.addSector(sol);
        return m;
    }

    public static Map Frontier() {
        Map map = new Map();
        map.name = "Frontier";
        Sector sol = new Sector("Sol", 0, 0);
        sol.setTeamHomes(1);
        Sector rigel = new Sector("Rigel", 3, 0);
        rigel.setTeamHomes(2);
        map.addSector(sol);
        map.addSector(rigel);
        map.link(sol, rigel);
        return map;
    }

    public void addSector(Sector s) {
        s.setId(uniqueCounter);
        sectors.put(uniqueCounter, s);
        uniqueCounter++;
    }

    public Sector getSector(int id) {
        return sectors.get(id);
    }

    public HashMap<Integer, Sector> getMap() {
        return sectors;
    }

    public Collection<Sector> getSectors() {
        return sectors.values();
    }

    public void link(Sector a, Sector b) {
        a.addLink(b);
        b.addLink(a);
    }

    public Sector getClosestSector(int x, int y) {
        Sector closest = null;
        double distance = Double.MAX_VALUE;
        for(Sector s : getSectors()) {
            double d = s.getDistanceToXY(x, y);
            if(d < distance) {
                closest = s;
                distance = d;
            }
        }
        return closest;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    private static void loadMaps() {
        addMap(Brawl());
        addMap(Frontier());
    }

    public static void addMap(Map map) {
        maps.put(map.hashCode(), map);
    }
}
