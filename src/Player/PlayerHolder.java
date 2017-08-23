package Player;

/**
 *
 * @author Julian Craske
 */

import java.util.*;

public interface PlayerHolder {
    public PlayerInfo getPlayer(int playerId);

    public String getPlayerName(int playerId);

    public Collection<Integer> getPlayerIDs();

    public Collection<PlayerInfo> getPlayers();

    public void updatePlayerInfo(Integer id, PlayerInfo info);
    
    public void deletePlayerInfo(Integer id);
}
