package Display;

/**
 *
 * @author Julian Craske
 */

import java.util.*;
import java.awt.*;

import Player.PlayerInfo;

public class ScoreBoard implements Overlay {
    Collection<PlayerInfo> players;

    public ScoreBoard(Collection<PlayerInfo> players) {
       this. players = players;
    }

    public void draw(Graphics g) {        
        int x = 50;
        int y = 50;
        drawLegend(g, x, y);
        y += 50;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        for(PlayerInfo info : players) {
            drawPlayer(g, info, x, y);
            y += 50;
        }
    }

    private void drawLegend(Graphics g, int x, int y) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Team", x, y);
        x += 50;
        g.drawString("Player", x, y);
        x += 150;
        g.drawString("Kills", x, y);
        x += 50;
        g.drawString("Deaths", x, y);
        x += 100;
        g.drawString("Score", x, y);
    }

    private void drawPlayer(Graphics g, PlayerInfo info, int x, int y) {
        g.drawString(info.getTeam() + "", x, y);
        x += 50;
        g.drawString(info.getName(), x, y);
        x += 150;
        g.drawString(info.getKills() + "", x, y);
        x += 50;
        g.drawString(info.getDeaths() + "", x, y);
        x += 100;
        g.drawString(info.getScore() + "", x, y);
    }
}
