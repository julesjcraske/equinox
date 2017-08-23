package Datastructures;

/**
 *
 * @author Julian Craske
 */

import java.util.*;

import Core.*;

public class CollisionDetector implements EntityListener, MoveListener {
    private static final int gridSize = 8;
    private static final int squareSize = 800;

    private HashMap<Integer, CollisionGrid> grids = new HashMap<Integer, CollisionGrid>();

    synchronized public void add(Entity e) {
        if(!grids.containsKey(e.getSectorID())) {
            grids.put(e.getSectorID(), new CollisionGrid());
        }
        grids.get(e.getSectorID()).add(e);
        e.addMoveListener(this);
    }

    synchronized public void onMove(Entity e, double dx, double dy) {
        grids.get(e.getSectorID()).move(e, dx, dy);
    }

    synchronized public void onSectorChange(Entity e, int oldSectorId) {
        grids.get(oldSectorId).remove(e);
        if(!grids.containsKey(e.getSectorID())) {
            grids.put(e.getSectorID(), new CollisionGrid());
        }
        grids.get(e.getSectorID()).add(e);
    }

    synchronized public void remove(Entity e, int sectorId) {
        CollisionGrid sector = grids.get(sectorId);
        if(sector != null) {
            sector.remove(e);
        }
    }

    synchronized public Entity getFirstCollidingEntity(Entity collider, Class c) {
        for(Collection<Entity> entities : grids.get(collider.getSectorID()).getNearbyEntityLists(collider)) {
            for(Entity e : entities) {
                if(collider != e && collider.isCollidingWith(e) && c.isInstance(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    synchronized public Collection<Entity> scan(Entity collider) {
        LinkedList<Entity> set = new LinkedList<Entity>();
        for(Entity e : grids.get(collider.getSectorID()).all) {
            if(collider.isWithinScanRange(e)) {
                if(e.isStatic()) {
                    set.addFirst(e);
                } else {
                    set.addLast(e);
                }
            }
        }
        return set;
    }

    public void onCreation(Entity e) {
        //Allow the game model to set the id of the new entity before it can be added
    }

    public void onRemoteReceipt(Entity e) {
        add(e);
    }

    public void onDestruction(Entity e) {
        remove(e, e.getSectorID());
    }

    public void onVisualEffectCreation(Entity e) {
        //Visual effects do not collide
    }

    private class CollisionGrid {
        private HashSet<Entity> all = new HashSet<Entity>();
        private HashSet<Entity>[][] grid = new HashSet[gridSize][gridSize];

        private CollisionGrid() {
            for(int i = 0; i < gridSize; i++) {
                for(int j = 0; j < gridSize; j++) {
                    grid[i][j] = new HashSet<Entity>();
                }
            }
        }
        
        private void add(Entity e) {
            all.add(e);
            for(Position p : getGrids(e)) {
                getEntities(p).add(e);
            }
        }
        
        private void remove(Entity e) {
            if(all.remove(e)) {
                for(Position p : getGrids(e)) {
                    getEntities(p).remove(e);
                }
            }
        }

        private HashSet<Entity> getEntities(Position pos) {
            return grid[pos.x][pos.y];
        }

        private void move(Entity e, double dx, double dy) {
            if(all.contains(e)) {
                Set<Position> oldGrids = getGrids(e);
                e.transpose(dx, dy);
                Set<Position> newGrids = getGrids(e);
                updateGrids(e, oldGrids, newGrids);
            } else {
                e.transpose(dx, dy);
            }
        }

        private void updateGrids(Entity e, Set<Position> oldGrids, Set<Position> newGrids) {
            for(Position p : oldGrids) {
                if(!newGrids.contains(p)) {
                    getEntities(p).remove(e);
                }
            }
            for(Position p : newGrids) {
                if(!oldGrids.contains(p)) {
                    getEntities(p).add(e);
                }
            }
        }

        private Collection<Collection<Entity>> getNearbyEntityLists(Entity e) {            
            LinkedList<Collection<Entity>> lists = new LinkedList<Collection<Entity>>();
            for(Position p : getGrids(e)) {
                lists.add(getEntities(p));
            }
            return lists;
        }

        private Set<Position> getGrids(Entity e) {
            double radius = e.getRadius();
            double x = e.getX(), y = e.getY();

            HashSet<Position> grids = new HashSet<Position>();
            grids.add(getGrid(x - radius, y - radius));
            grids.add(getGrid(x - radius, y + radius));
            grids.add(getGrid(x + radius, y - radius));
            grids.add(getGrid(x + radius, y + radius));

            return grids;
        }

        private Position getGrid(double ex, double ey) {
            int x = (int) Math.floor(ex / squareSize);
            if(ex < 0) x -= 1;
            int y = (int) Math.floor(ey / squareSize);
            if(ey < 0) y -= 1;
            x = x % gridSize;
            if(x < 0) x += gridSize;
            y = y % gridSize;
            if(y < 0) y += gridSize;
            return new Position(x, y);
        }
    }

    private class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            return hashCode() == obj.hashCode();
        }

        @Override
        public int hashCode() {
            return x * gridSize + y;
        }
    }
}
