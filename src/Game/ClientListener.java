package Game;

/**
 *
 * @author Julian Craske
 */
public interface ClientListener {

    public void onMessageShow();

    public void onConnectionAttempt();

    public void onConnectionConfirm();
}
