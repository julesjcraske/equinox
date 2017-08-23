package Projectiles;

/**
 * @author Julian Craske
 */

public class Bullet extends Projectile
{
    public Bullet(String name, String imageFilePath, double turnRate, int life, double damage)
    {
        super(name, imageFilePath, turnRate, life, damage);
        setSigniture(3);
    }
    
    public void act()
    {
        super.act();
        turnLeft();        
    }

    public boolean isDrawOnRadar() {
        return false;
    }
    
    public Bullet clone()
    {
        return new Bullet(getName(), getImagePath(), getTurnRate(), getLife(), getDamage());
    }    
}