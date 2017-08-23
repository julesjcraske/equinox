package Core;

/**
 *
 * @author Julian Craske
 */
public interface EntityListener {
    public void onCreation(Entity e);

    public void onRemoteReceipt(Entity e);

    public void onDestruction(Entity e);

    public void onVisualEffectCreation(Entity e);
}
