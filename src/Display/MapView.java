package Display;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import Core.*;
import Player.*;
import Game.*;

public class MapView extends JPanel
{
    private int scale = 8;
    private int sectorSize = 9;

    private GameClient client;

    private int refreshRate = 10;
    private int refreshCount = 0;
    private int hashSize = 10;


    public MapView(GameClient c) {
        this.client = c;

        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(150, 150));
        setMaximumSize(new Dimension(300, 300));

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                Sector focus = Sector.getSector(client.getPlayer().getPlayerShip().getSectorID());
                if(focus != null) {
                    Sector selection = client.getMap().getClosestSector(focus.getX() + (e.getX() - getWidth()/2) / scale, focus.getY() + (e.getY() - getHeight()/2) / scale);
                    client.getPlayer().setDestination(selection.getId());
                    refresh();
                }
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        });
    }

    public void refresh() {
        if(refreshCount > refreshRate) {
            repaint();
        }
        refreshCount++;
    }

    public void paint(Graphics g) {        
        if(getHeight() > 0 && getWidth() > 0) {
            g.setColor(Color.lightGray);
            getGraphics().setFont(new Font("Arial", Font.PLAIN, 4));

            Game.Map map = client.getMap();
            g.fillRect(0, 0, getWidth(), getHeight());
            Sector focus = map.getSector(client.getPlayer().getPlayerShip().getSectorID());
            Sector destination = map.getSector(client.getPlayer().getDestination());

            Collection<Sector> sectors = map.getSectors();
            int x;
            int y;
            for(Sector node : sectors) {
                    x = (int)(getWidth()/2 + ((node.getX()-focus.getX()) * scale));
                    y = (int)(getHeight()/2 + ((node.getY()-focus.getY()) * scale));


                    //graphics.drawLine(x, y, (int)(getWidth()/2 + ((destination.getX()-focus.getX()) * scale)), (int)(getHeight()/2 + ((destination.getY()-focus.getY()) * scale)));

                    if(node == focus)               g.setColor(Color.green);
                    else if(node == destination)    g.setColor(Color.red);
                    else                            g.setColor(Color.blue);
                    g.fillOval(x - sectorSize/2, y - sectorSize/2, sectorSize, sectorSize);

                    g.setColor(Color.black);
                    g.drawString(node.getName(), x - 5, y - 5);
            }
        }
    }
}
