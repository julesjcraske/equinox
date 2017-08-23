package Display;


import Vessel.Ship.SpaceShip;
import java.awt.*;
import javax.swing.*;
import java.util.*;

import Game.*;
import Core.*;
import Vessel.*;
import Player.*;

public class RadarView extends JPanel{
    private static final int refreshRate = 5;

    private int refreshCount = 0;
    private GameClient client;

    public RadarView(GameClient c)
    {
        this.client = c;
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(200, 200));
        setMaximumSize(new Dimension(300, 300));
    }

    public void refresh() {
        refreshCount++;
        if(refreshCount >= refreshRate) {
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        Player player = client.getPlayer();
        SpaceShip focus = player.getPlayerShip();
        Collection<Entity> radarObjects = player.getVisibleEntities();
        Entity target = focus.getTarget();
        int scanRange = focus.getScanRange() * 2;

        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        for(Entity thing : radarObjects) {
            if(thing.isDrawnOnRadar()) {
                double x = thing.getX();
                double y = thing.getY();

                Color color = Color.white;
                if(thing == target) {
                    color = Color.yellow;
                } else if(focus.getIsFriendly(thing)) {
                    color = Color.green;
                } else if(focus.getIsHostile(thing)) {
                    color = Color.red;
                }
                g.setColor(color);

                if(thing.isCircular()) {
                    g.drawOval((int)(getWidth()/2 + ((x-focus.getX()) / scanRange * getWidth()/2)), (int)(getHeight()/2 + ((y-focus.getY()) / scanRange * getHeight()/2)), thing.getSize(), thing.getSize());
                } else {
                    g.drawRect((int)(getWidth()/2 + ((x-focus.getX()) / scanRange * getWidth()/2)), (int)(getHeight()/2 + ((y-focus.getY()) / scanRange * getHeight()/2)), thing.getSize(), thing.getSize());
                }
            }
        }
    }
}
