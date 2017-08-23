package Game;

/**
 *
 * @author Julian Craske
 */

import java.util.*;

import Core.*;
import Player.*;

public interface GameModel extends PlayerHolder {
    public void runOneStep();

    public Entity getEntity(int hash);

    public boolean isLocal(Entity e);

    public Collection<Entity> getEntities();

    public Collection<Entity> getVisualEffects();

    public void addLocalEntity(Entity e);

    public void addExternalEntity(Entity e);

    public void addVisualEffect(Entity e);

    public int getPlayerId();

    public Map getMap();

    public void quit();

    public void showMessage(String s);

    public Collection<String> getMessageLog(int pos, int num);

    public int getLastMessageNo();

    public void setPlayerId(int id);

    public Timer getTimer();
}
