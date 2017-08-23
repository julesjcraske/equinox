package Game;

/**
 *
 * @author Julian Craske
 */

import java.util.*;

import Core.*;
import Player.*;
import Datastructures.*;
import Messages.*;
import Projectiles.*;

public abstract class NetworkedGame implements GameModel, EntityListener, ProjectileListener {
    private static double drThresh = 3;

    private int playerId = -1;
    private int uniqueCounter = 0;

    private Map map;

    private ArrayList<String> messageLog = new ArrayList<String>();
    private LinkedList<Entity> newEntities = new LinkedList<Entity>();
    private LinkedList<Entity> visualEffects = new LinkedList<Entity>();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private HashMap<Integer, Entity> reckonedLocalModel = new HashMap<Integer, Entity>();
    private HashMap<Integer, PlayerInfo> players = new HashMap<Integer, PlayerInfo>();

    public NetworkedGame() {        
        Entity.addEntityListener(this);
        Projectile.addProjectileListener(this);

        Team.setNumberOfTeams(2);
        loadMap(Map.getDefaultMap());
    }

    public static boolean deadReckon(Entity actual, Entity reckoner) {
        if(actual.isDestroyed() != reckoner.isDestroyed())
            return true;
        if(actual.getSectorID() != reckoner.getSectorID())
            return true;
        if(actual.isThrusting() != reckoner.isThrusting())
            return true;
        if(actual.isBoosting() != reckoner.isBoosting())
            return true;
        if(actual.getDistanceToEntity(reckoner) > drThresh)
            return true;
        if(Math.abs(actual.getRotation() - reckoner.getRotation()) > drThresh)
            return true;
        if(reckoner.getRemainingHitPoints() - actual.getRemainingHitPoints() > drThresh)
            return true;
        if(Math.abs(reckoner.getSigniture() - actual.getSigniture()) > 0.01) // 1% Signiture difference
            return true;

        return false;
    }

    private synchronized void createNewEntities() {
        for(Entity e : newEntities) {
            entities.put(e.hashCode(), e);
        }
        newEntities.clear();
    }

    private synchronized void addNewEntity(Entity e) {
        newEntities.add(e);
    }

    public abstract void sendEntityUpdate(Entity e);

    public abstract void send(Message m, int destination);

    public void runOneStep() {
        createNewEntities();

        Entity reckoner, actual;
        Iterator<Entity> it = entities.values().iterator();
        while(it.hasNext()) {
            actual = it.next();
            actual.act();

            //DEAD RECKONING
            if(isLocal(actual)) {
                reckoner = reckonedLocalModel.get(actual.hashCode());
                reckoner.act();
                if(deadReckon(actual, reckoner)) {
                    sendEntityUpdate(actual);
                    reckoner.takeForm(actual);
                }                
            }

            if(actual.isDestroyed()) {
                reckonedLocalModel.remove(actual.hashCode());
                it.remove();
            }
        }

        it = visualEffects.iterator();
        while(it.hasNext()) {
            actual = it.next();
            actual.act();
            if(actual.isDestroyed()) {
                it.remove();
            }
        }
    }   

    public void showMessage(String s) {
        messageLog.add(s);
        System.out.println(s);
    }

    public ArrayList<String> getMessageLog(int pos, int num) {
        ArrayList<String> messages = new ArrayList<String>();
        for(int i = 0; i < num; i++) {
            if(pos-i >= 0) {
                messages.add(messageLog.get(pos-i));
            }
        }
        return messages;
    }

    public int getLastMessageNo() {
        return messageLog.size() - 1;
    }

    public Map getMap() {
        return map;
    }

    public void addLocalEntity(Entity e) {
        e.setOwner(playerId);
        do {
            e.setId(uniqueCounter);
            uniqueCounter++;
            if(uniqueCounter == Integer.MAX_VALUE)  uniqueCounter = 0;
        } while(getEntity(e.hashCode()) != null);
        Entity.collisionDetector.add(e);
        addNewEntity(e);

        //Create and set up the reckoner
        Entity reckoner = e.clone();
        reckoner.setAsReckoner();
        reckoner.setOwner(playerId);
        reckoner.setId(e.getId());
        reckoner.takeForm(e);
        reckonedLocalModel.put(e.hashCode(), reckoner);
    }

    public void addExternalEntity(Entity e) {
        addNewEntity(e);
    }

    public boolean isLocal(Entity e) {
        return e.isOwnedBy(getPlayerId());
    }    

    public void setDRThreshold(double value) {
        drThresh = value;
    }

    public double getDRThreshold() {
        return drThresh;
    }

    public Collection<Entity> getVisualEffects() {
        return visualEffects;
    }

    public void addVisualEffect(Entity e) {
        visualEffects.add(e);
    }

    public void setPlayerId(int id) {
        playerId = id;
    }

    public Entity getEntity(int hash) {
        return entities.get(hash);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void updatePlayerInfo(Integer id, PlayerInfo info) {
        if(players.get(id) == null) {
            showMessage(info.getName() + " has joined the game.");
            players.put(id, info);
        } else {
            players.get(id).takeForm(info);
        }
    }

    public PlayerInfo getPlayer(int playerId) {
        return players.get(playerId);
    }

    public String getPlayerName(int playerId) {        
        PlayerInfo info = getPlayer(playerId);
        if(info == null) {
            if(playerId == 0)
                return "Server";
            else
                return "Unknown";
        } else {
            return info.getName();
        }
    }

    public Collection<Integer> getPlayerIDs() {
        return players.keySet();
    }

    public Collection<PlayerInfo> getPlayers() {
        return players.values();
    }

    public void deletePlayerInfo(Integer id) {
        if(players.get(id) != null) {
            showMessage(players.get(id).getName() + " has left the game.");
            players.remove(id);
        }
    }

    public void loadMap(Map map) {
        this.map = map;
        Sector.setSectors(map.getMap());
    }

    public void onCreation(Entity e) {
        addLocalEntity(e);
    }

    public void onRemoteReceipt(Entity e) {
        addExternalEntity(e);
    }

    public void onDestruction(Entity e) {
        
    }

    public void onProjectileHit(Entity victim, Projectile p, double damage) {
        if(isLocal(p)){
            if(isLocal(victim)) {
                ((Destroyable) victim).hit(damage);
            } else {
                send(new ProjectileHit(victim, p, damage), victim.getOwner());
            }
        }
    }

    public void onProjectileMiss(Projectile p) {

    }

    public void onVisualEffectCreation(Entity e) {
        visualEffects.add(e);
    }
}
