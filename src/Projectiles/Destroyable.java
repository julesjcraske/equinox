package Projectiles;

/**
 * @author Julian Craske
 */
public interface Destroyable
{
    public void hit(double damage);
    
    public void repair(double damage);
}