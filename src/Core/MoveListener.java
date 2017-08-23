package Core;

/**
 *
 * @author Julian Craske
 */

public interface MoveListener {

    public void onMove(Entity e, double dx, double dy);

    public void onSectorChange(Entity e, int oldSectorId);
}
