package Core;

import java.io.*;

public class Vector implements Serializable
{
    private double dx = 0.0;
    private double dy = 0.0;
    private double direction = 0.0;
    private double length = 0.0;
    
    public Vector() { }

    public Vector(double direction, double length)
    {
        this.length = length;
        this.direction = direction;
        updateDXDY();
    }

    public void updateDXDY() {
        dx = length * Math.cos(Math.toRadians(direction));
        dy = length * Math.sin(Math.toRadians(direction));
    }

    public void updateDirLen() {
        this.direction = (Math.toDegrees(Math.atan2(dy, dx)) + 360) % 360;
        this.length = Math.sqrt(dx*dx+dy*dy);
    }

    /**
     * Set the direction of this vector.
     */
    public void setDirection(double direction) {
        this.direction = direction;
        updateDXDY();
    }
   
    /**
     * Add other vector to this vector.
     */
    public void add(Vector other) {
        dx += other.dx;
        dy += other.dy;    
        updateDirLen();
    }

    public void minus(Vector other) {
        dx -= other.dx;
        dx -= other.dy;
        updateDirLen();
    }

    public void setXY(double x, double y) {
        dx = x;
        dy = y;
        updateDirLen();
    }
    
    public double getX() {
        return dx;
    }
     
    public double getY() {
        return  dy;
    }
    
    public double getDirection() {
        return direction;
    }
    
    public double getLength() {
        return length;
    }
    
    public void decreaseSpeed(double deceleration) {
        length -= deceleration;
        updateDXDY();
    }

    public void slow(double percent) {
        decreaseSpeed(length * percent);
    }
    
    public void stop() {
        dx = 0;
        dy = 0;
        direction = 0;
        length = 0;
    }
    
    /**
     * Create a copy of this vector.
     */
    public Vector copy() {
        Vector copy = new Vector();
        copy.dx = dx;
        copy.dy = dy;
        copy.direction = direction;
        copy.length = length;
        return copy;
    }       
}