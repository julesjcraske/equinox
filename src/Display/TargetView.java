package Display;


import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.geom.*;

import Core.*;
import Game.*;

public class TargetView extends JPanel
{
    private GameClient client;

    private BufferedImage bg;
    private JLabel name = new JLabel();
    private JPanel target = new JPanel() {
        public void paint(Graphics g) {
            g.drawImage(bg, 0, 0, Color.lightGray, this);
        }
    };
    
    public TargetView(GameClient c)
    {
        this.client = c;
        setMinimumSize(new Dimension(100, 100));
        setPreferredSize(new Dimension(200, 200));
        setMaximumSize(new Dimension(300, 300));
        
        setLayout(new BorderLayout());
        add(name, BorderLayout.NORTH);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("Arial", Font.PLAIN, 12));
        add(target, BorderLayout.CENTER);
    }

    public void refresh() {
        if(target.getWidth() > 0 && target.getHeight() > 0) {
            bg = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_BGR);
            Entity e = client.getPlayer().getPlayerShip().getTarget();
            if(e != null) {
                Graphics2D view = bg.createGraphics();
                AffineTransform transformer = new AffineTransform();
                BufferedImage image = e.getImage();

                //Draw text info including speed and distance
                view.setColor(Color.white);
                view.setFont(view.getFont().deriveFont(Font.PLAIN, 10));
                int speed = (int) (e.getSpeed().getLength() * 20);
                view.drawString("Speed: " + speed + "m/s", 5, target.getHeight() - 20);
                int distance = (int) client.getPlayer().getPlayerShip().getDistanceToEntity(e);
                view.drawString("Distance: " + distance + "m", 5, target.getHeight() - 10);

                //Draw target image at rotation
                view.translate(getWidth() / 2 - image.getWidth()/2, getHeight()/ 2 - image.getHeight()/2);
                transformer.setToRotation(Math.toRadians(e.getRotation()), image.getWidth()/2, image.getHeight()/2);
                view.drawImage(image, transformer, this);
                
                name.setText((e.isReckoner()? "R":"") + e.getTeamNo() + ": " + client.getPlayerName(e.getOwner()) + " - " + e.getName());
            }
            else          name.setText("No Target");
            repaint();
        }
    }
}
