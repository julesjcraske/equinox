package Core;


import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

import Datastructures.*;

public abstract class Entity extends NamedObject
{
    private static double friction = 0.02;
    private static double hitRadius = 0.8;

    public static final int STRAIGHT = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private static Collection<EntityListener> entityListeners = new ArrayList<EntityListener>();
    private static SoundListener soundListener;
    private Collection<MoveListener> moveListeners = new LinkedList<MoveListener>();

    public static final CollisionDetector collisionDetector = new CollisionDetector();
    static {
        addEntityListener(collisionDetector);
    }

    private Integer hashcode = null;
    private int owner;
    private int id;
    private int team = 0;
    protected int image = 0;

    private int sector = 0;
    private String baseImage;
    private Vector speed = new Vector();
    private Point2D location = new Point2D.Double();
    private double rotation = 0.0;
    private double turnRate;
    private double signiture = 1;
    private int scanRange = 0;

    private boolean destroyed = false;
    private boolean reckoner = false;
    private boolean visualEffect = false;
    private String explosion = null;
    
    public Entity(String name, String imageFilePath, double turnRate)
    {        
        super(name);
        baseImage = imageFilePath;
        this.turnRate = turnRate;
    }   
    
    public abstract void act();

    @Override
    public abstract Entity clone();

    public int getOwner() {
        return owner;
    }

    public String getTag() {
        return getTag(owner, id);
    }

    public static String getTag(int owner, int id) {
        return owner + ":" + id;
    }

    public void setOwner(int playerId) {
        owner = playerId;
        hashcode = null;
    }

    public boolean isOwnedBy(int playerId) {
        return owner == playerId;
    }

    public void setId(int id) {
        this.id = id;
        hashcode = null;
    }

    public int getId() {
        return id;
    }

    public Sector getSector() {
        return Sector.getSector(sector);
    }

    public int getSectorID() {
        return sector;
    }

    public void setSector(int newSectorId) {
        int oldSectorId = getSectorID();
        if(oldSectorId != newSectorId) {            
            sector = newSectorId;
            for(MoveListener l : moveListeners) {
                l.onSectorChange(this, oldSectorId);
            }
        }
    }

    public int getImageNo() {
        return image;
    }

    public void setImageNo(int imageNo) {
        image = imageNo;
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }
    
    public void setLocation(double x, double y) {
        location.setLocation(x, y);
    }
    
    public void setLocation(Point2D loc) {
        location.setLocation(loc);
    }

    public Point2D getLocation() {
        return location;
    }

    public void drag() {
        decreaseSpeed(friction * getSpeed().getLength());
    }
    
    public void setImage(String imageFilePath) {
        this.baseImage = imageFilePath;
    }

    public BufferedImage getImage() {
        return ImagePool.getImage(baseImage);
    }

    public String getImagePath() {
        return baseImage;
    }
    
    public void turnLeft() {
         setRotation(rotation - turnRate);
    }

    public void turnRight() {
        setRotation(rotation + turnRate);
    }

    public void setRotation(double degrees) {
        rotation = (360 + degrees) % 360.0;
    }

    public double getRotation() {
        return rotation;
    }

    public int turnTowardAngle(double degrees) {
        return turnTowardAngle(degrees, turnRate);
    }

    protected int turnTowardAngle(double degrees, double turnRate)
    {
        degrees = (degrees + 360) % 360;
        if(rotation - degrees > 180)        degrees += 360;
        else if(degrees - rotation > 180)   degrees -= 360;

        int direction = STRAIGHT;
        if(rotation < degrees - turnRate) {
            turnRight();
            direction = RIGHT;
        } else if(rotation > degrees + turnRate) {
            turnLeft();
            direction = LEFT;
        }  else {
            setRotation(degrees);
        }
        if(Math.abs(degrees - rotation) < turnRate) {
            direction = STRAIGHT;
        }
        return direction;
    }

    /**
     * Determine whether this entity's rotation is similar to a particular value
     * @param angle The target angle
     * @param allowedDeviation The allowed difference between rotation and target angle (must be greater than zero)
     * @return
     */
    public boolean isFacing(double angle, double allowedDeviation) {
        double rotation = getRotation();
        if(rotation - 360 - angle < rotation - angle)   rotation -= 360;
        if(rotation + 360 - angle < rotation - angle)   rotation += 360;
        return getRotation() < angle + allowedDeviation && getRotation() > angle - allowedDeviation;
    }
    
    public void move(double dx, double dy) {
        if(isReckoner() || isVisualEffect()) {
            transpose(dx, dy);
        } else {
            collisionDetector.onMove(this, dx, dy);
        }
    }

    public void transpose(double dx, double dy) {
        location.setLocation(getX() + dx, getY() + dy);
    }

    public Vector getSpeed() {
        return speed;
    }

    public void increaseSpeed(double direction, double accel) {
        speed.add(new Vector(direction, accel));
    }

    public void increaseSpeed(Vector v) {
        speed.add(v);
    }

    public void decreaseSpeed(double deceleration) {
        speed.decreaseSpeed(deceleration);
    }

    public void slow(double percent) {
        speed.slow(percent);
    }

    public boolean isCloaked() {
        return false;
    }

    public void setCloaked(boolean c) {

    }

    protected void move() {
        move(speed.getX(), speed.getY());
    }
           
    public boolean isCollidingWith(Entity e) {
            return getDistanceToEntity(e) < getHitRadius() + e.getHitRadius();
    }

    public double getHitRadius() {
        return getRadius() * hitRadius;
    }

    public double getRadius() {
        return (getImage().getHeight() + getImage().getWidth()) / 4;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }

    public void create() {
        for(EntityListener l : entityListeners) {
            l.onCreation(this);
        }
    }
    
    public void destroy() {
        destroyed = true;
        if(!isReckoner() && !isVisualEffect()) {
            for(EntityListener l : entityListeners) {
                l.onDestruction(this);
            }
        }
    }

    public void explode() {
        if(explosion != null && !isReckoner()) {
            Explosion e = new Explosion(explosion);
            e.setLocation(getLocation());
            e.setSector(getSectorID());
            e.createAsVisualEffect();
            playSoundEffect("erflakb.wav");
        }
    }
    
    public double getDistanceToEntity(Entity e) {
        return getDistanceToXY(e.getX(), e.getY());
    } 
    
    public double getDistanceToXY(double dx, double dy) {
        return location.distance(dx, dy);
    }
    
    public double getAngleToEntity(Entity e) {
        return getAngleToXY(e.getX(), e.getY());
    }
    
    public double getAngleToXY(double xDiff, double yDiff) {
        xDiff -= getX();
        if(xDiff == 0) xDiff++;
        yDiff -= getY();
        if(yDiff == 0) yDiff++;
        double theta = yDiff / xDiff;
        double angle = Math.toDegrees(Math.atan(theta));
        
        if((xDiff >= 0) && (yDiff < 0)) angle += 360;
        if(xDiff < 0) angle = 180 + angle;
        
        return angle % 360;
    }
    
    public double getTurnRate() {
        return turnRate;
    }
    
    public boolean getIsHostile(Entity e) {
        if(e.getTeamNo() == 0) return false;
        return getTeamNo() != e.getTeamNo();
    }

    public boolean getIsFriendly(Entity e) {
        if(e.getTeamNo() == 0) return false;
        return getTeamNo() == e.getTeamNo();
    }

    public int getTeamNo() {
        return team;
    }

    public void setTeam(int t) {
        team = t;
    }

    public int getSize() {
        return 0;
    }

    public void setScanRange(int scanRange) {
        this.scanRange = scanRange;
    }
    
    public int getScanRange() {
        return scanRange;
    }

    public boolean isWithinScanRange(Entity e) {
        if(getSectorID() == e.getSectorID()) {
            if(getTeamNo() == e.getTeamNo() || e.isStatic()) {
                return true;
            } else {
                return getDistanceToEntity(e) < getScanRange() * e.getSigniture();
            }
        } else {
            return false;
        }
    }

    public void setSigniture(double sig) {
        signiture = sig;
    }

    public double getSigniture() {
        return signiture;
    }

    public void takeForm(Entity e) {
        setLocation(e.getX(), e.getY());
        setRotation(e.getRotation());
        speed.setXY(e.getSpeed().getX(), e.getSpeed().getY());
        setHealthPercentage(e.getHealthPercentage());
        setShieldPercentage(e.getShieldPercentage());
        setSigniture(e.getSigniture());
        setThrusting(e.isThrusting());
        setBoosting(e.isBoosting());
        setCloaked(e.isCloaked());
        setSector(e.getSectorID());
    }
    
    public static void addEntityListener(EntityListener listener) {
        entityListeners.add(listener);
    }

    public void addMoveListener(MoveListener listener) {
        moveListeners.add(listener);
    }

    public void createAsVisualEffect() {
        visualEffect = true;
        for(EntityListener l : entityListeners) {
            l.onVisualEffectCreation(this);
        }
    }

    public void addAsRemoteEntity() {
        for(EntityListener l : entityListeners) {
            l.onRemoteReceipt(this);
        }
    }

    public void setExplosion(String baseFilePath) {
        explosion = baseFilePath;
    }

    public void setAsReckoner() {
        reckoner = true;
    }

    public boolean isReckoner() {
        return reckoner;
    }

    public boolean isVisualEffect() {
        return visualEffect;
    }

    public void setThrusting(boolean b) {

    }

    public boolean isThrusting() {
        return false;
    }

    public void setBoosting(boolean b) {
        //Overriden by spaceship
    }

    public boolean isBoosting() {
        return false;
    }

    public boolean isCircular() {
        return false;
    }

    public void setTarget(Entity target) {
        //Override for entities that need a target
    }

    public Entity getTarget() {
        return null;
    }

    public static void setSoundListener(SoundListener listener) {
        soundListener = listener;
    }

    public void playSoundEffect(String effect) {
        if(soundListener != null) {
            soundListener.play(this, effect);
        }
    }

    public void playSoundEffectIfFocus(String effect) {
        if(soundListener != null) {
            soundListener.playIfFocus(this, effect);
        }
    }

    public Collection<Entity> getVisibleEntities() {
        return collisionDetector.scan(this);
    }

    @Override
    public int hashCode() {
        if(hashcode == null) {
            hashcode = getTag().hashCode();
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    //Health and Shield methods, override for more complicated objects.
    
    public double getHealthPercentage() {
        if(isDestroyed())
            return 0;
        else
            return 1;
    }
    
    public double getShieldPercentage() {
        return 0;
    }

    public double getRemainingHitPoints() {
        return 0;
    }

    public void setHealthPercentage(double percent) {
        if(percent <= 0) {
            destroy();
        }
    }

    public void setShieldPercentage(double percent) {
        //A default entity has no shields
    }

    //Entity type checks, override appropriately

    public boolean isShip() {
        return false;
    }

    public boolean isProjectile() {
        return false;
    }

    public boolean isStructure() {
        return false;
    }

    public boolean isDockable() {
        return false;
    }

    public boolean isDrawnOnRadar() {
        return true;
    }

    public boolean isAsteroid() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }

    public boolean isDestroyable() {
        return false;
    }
}
