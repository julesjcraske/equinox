package Core;

/**
 *
 * @author Julian Craske
 */

import java.awt.geom.Point2D;
import java.util.*;
import java.io.*;

public class Sector implements Serializable {
    private static HashMap<Integer, Sector> sectors;

    private int id;
    private int x, y;
    private String name;
    private Set<Sector> links = new HashSet<Sector>();
    private Set<Integer> teamHomes = new HashSet<Integer>();

    public Sector(String name, int x , int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setTeamHomes(Integer ... teams) {
        for(Integer team : teams) {
            teamHomes.add(team);
        }
    }

    public Set<Integer> getTeamHomes() {
        return teamHomes;
    }

    public Set<Sector> getLinks() {
        return links;
    }

    public int getNumLinks() {
        return links.size();
    }

    public void addLink(Sector sector) {
        links.add(sector);
    }

    public static void setSectors(HashMap<Integer, Sector> map) {
        sectors = map;
    }

    public static Sector getSector(int sectorId) {
        return sectors.get(sectorId);
    }
    
    public double getAngleToSector(Sector s) {
        return getAngleToXY(s.getX(), s.getY());
    }

    public double getDistanceToXY(double x2, double y2) {
        return Point2D.distance(x, y, x2, y2);
    }

    public double getSectorRadius() {
        return 4000;
    }

    public double getHyperspaceDistance() {
        return 5000;
    }

    public double getAngleToXY(double xDiff, double yDiff) {
        xDiff -= getX();
        if(xDiff == 0) xDiff += 0.01;
        yDiff -= getY();
        if(yDiff == 0) yDiff += 0.01;
        double theta = yDiff / xDiff;
        double angle = Math.toDegrees(Math.atan(theta));

        if((xDiff >= 0) && (yDiff < 0)) angle += 360;
        if(xDiff < 0) angle = 180 + angle;

        return angle % 360;
    }
}
