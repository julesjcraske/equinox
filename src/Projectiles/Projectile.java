package Projectiles;


/**
 * @author Julian Craske
 */

import java.util.*;

import Core.*;

public abstract class Projectile extends Entity
{
    private static Collection<ProjectileListener> projectileListeners = new ArrayList<ProjectileListener>();

    private Entity origin;
    private Entity target = null;
    private double damage;
    private int life;
    private int acts = 0;   

    public Projectile(String name, String imageFilePath, double turnRate, int life, double damage)
    {
        super(name, imageFilePath, turnRate);
        this.life = life;
        this.damage = damage;
    }

    abstract public Projectile clone();
    
    public void act() {
        move();
        if(target != null) {
            if(getTarget().isDestroyed())    setTarget(null);
        }
        collide();
        decrementLife();
    }
    
    public boolean collide() {
        if(!isReckoner()) {
            Entity destroyable = collisionDetector.getFirstCollidingEntity(this, Destroyable.class);
            if(destroyable != null) {
                boolean viableTarget = getIsHostile(destroyable);
                if(viableTarget) {
                    destroy();
                    onProjectileHit(destroyable, this, damage);
                    return true;
                }
            }
        }
        return false;
    }         
    
    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void initialise(Entity origin) {
        setOrigin(origin);
        setTeam(origin.getTeamNo());
        setSector(origin.getSectorID());
        increaseSpeed(origin.getSpeed());
    }

    public void setOrigin(Entity origin) {
        this.origin = origin;
    }

    public Entity getOrigin() {
        return origin;
    }
    
    public void decrementLife() {
        acts++;
        if(acts == life) {
            destroy();
            //onProjectileMiss(this);
        }
    }
    
    public double getDamage() {
        return damage;
    }
    
    public int getLife() {
        return life;
    }
    
    public int getActs() {
        return acts;
    }

    public boolean isMissile() {
        return false;
    }

    @Override
    public boolean isProjectile() {
        return true;
    }

    public static void addProjectileListener(ProjectileListener listener) {
        projectileListeners.add(listener);
    }

    public static void onProjectileHit(Entity e, Projectile p, double damage) {
        for(ProjectileListener l : projectileListeners) {
            l.onProjectileHit(e, p, damage);
        }
    }

    public static void onProjectileMiss(Projectile p) {
        for(ProjectileListener l : projectileListeners) {
            l.onProjectileMiss(p);
        }
    }
}