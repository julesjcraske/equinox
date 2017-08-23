package Player;

/**
 *
 * @author Julian Craske
 */
public interface PlayerListener {
    public void onPlayerChange(Player p);

    public void onPlayerJoinedTeam(Player p);

    public void onPlayerLaunched(Player p);

    public void onPlayerLand(Player p);
}
