package Vessel.Ship;

/**
 *
 * @author Julian Craske
 */

import java.io.*;
import Core.Vector;

public class Mount implements Serializable {
    private Vector position;

    public Mount() {
        position = new Vector();
    }

    public void setPosition(int x, int y) {
        position.setXY(x, y);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getAngle() {
        return position.getDirection();
    }

    public double getLength() {
        return position.getLength();
    }
}
