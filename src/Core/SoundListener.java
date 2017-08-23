package Core;

/**
 *
 * @author Julian Craske
 */
public interface SoundListener {
    public void playIfFocus(Entity source, String sound);

    public void play(Entity source, String sound);
}
