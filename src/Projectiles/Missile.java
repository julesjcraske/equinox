package Projectiles;


import Core.*;
import Datastructures.*;
import java.awt.image.*;

public class Missile extends Projectile implements Destroyable
{  
    private double acceleration;
    private double armor;
    private int maxArmor;
    private double maxSpeed;
    private int delay;

    private static int THRUST = 1;
    
    public Missile(String name, String imageFilePath, double turnRate, int maxHitPoints, int delay, int life, double damage, double acceleration, double maxSpeed)
    {
        super(name, imageFilePath, turnRate, life, damage);
        maxArmor = maxHitPoints;
        armor = maxHitPoints;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.delay = delay;
        setExplosion("smallExplosion");
    }
    
    public void act() 
    {        
        if(getTarget() != null) {
            double range = getDistanceToEntity(getTarget());
            double acts = (range / (maxSpeed));
            double x = getTarget().getSpeed().getX();
            double y = getTarget().getSpeed().getY();
            double mx = this.getSpeed().getX();
            double my = this.getSpeed().getY();
            double targetAngle;

            targetAngle = getAngleToXY(getTarget().getX() + x*acts - mx*acts, getTarget().getY() + y*acts - my*acts);
            turnTowardAngle(targetAngle);      
        }
        if(getOrigin() != null) {
            if(getTarget() == null || getTarget() == getOrigin()) {
                setTarget(getOrigin().getTarget());
            }
        }
        if(getActs() > delay) {
            setImageNo(THRUST);
            increaseSpeed(getRotation(), acceleration);

            if(!isReckoner()) {
                MissileCloud cloud = new MissileCloud();
                cloud.initialise(this);
                cloud.createAsVisualEffect();
            }
        }        
        super.act();
        if(getActs() > delay) {
            drag();
        }
    }

    public BufferedImage getImage() {
        if(image == THRUST) {
            return ImagePool.getImage(getImagePath());
        } else {
            return ImagePool.getImage(getImagePath() + "_thrust");
        }
    }

    @Override
    public boolean collide() {
        if(getActs() > delay) {
            if(super.collide()) {
                explode();
                return true;
            }
        }
        return false;
    }

    public void drag() {
        decreaseSpeed(acceleration * getSpeed().getLength() / maxSpeed);
    }

    @Override
    public boolean isMissile() {
        return true;
    }
    
    public Missile clone() {
        return new Missile(getName(), getImagePath(), getTurnRate(), getMaxHitPoints(), delay, getLife(), getDamage(), acceleration, maxSpeed);
    }
    
    public int getSize() {
        return 0;
    }

    public boolean isDestroyable() {
        return true;
    }
    
    /**
     * Destroyable Methods
     */
    public void hit(double damage) {
        armor--;
        if(armor <= 0) destroy();
    }
    
    public double getHitPoints() {
        return armor;
    }
    
    public int getMaxHitPoints() {
        return maxArmor;
    }
    
    public void repair(double damage) {
        
    }
    
    class MissileCloud extends Entity {
        private static final int maxLife = 3;
        private int life = 0;
        private Missile origin;
        
        MissileCloud() {
            super("Missile Cloud", "missileCloud", 3);
        }
        
        public void act() {
            if(origin.isDestroyed()) {
                destroy();
            } else if(life < maxLife) {
                move();
                life++;
            } else {
                destroy();
            }
        }
        
        public void initialise(Missile origin)
        {
            this.origin = origin;
            if(origin != null) {                
                double dx = origin.getImage().getWidth() / 2 * Math.cos(Math.toRadians(180 + origin.getRotation()));
                double dy = origin.getImage().getWidth() / 2 * Math.sin(Math.toRadians(180 + origin.getRotation()));
                setLocation(origin.getX() + dx, origin.getY() + dy);
                setRotation(origin.getRotation() + 180 + (Math.random() - 0.5) * 10);
                setSector(origin.getSectorID());
                increaseSpeed(getRotation(), 3);
                increaseSpeed(origin.getSpeed());
            }
        }

        @Override
        public Entity clone() {
            return new MissileCloud();
        }
    }
}
