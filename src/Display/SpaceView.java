package Display;



import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;

import Core.*;
import Game.*;
import Player.Controls;

public class SpaceView extends JPanel {
    private BufferedImage bg;
    private AffineTransform transformer = new AffineTransform();
    private int[] starPositions;

    private Entity focus;
    private Entity target;
    private GameClient client;

    private static Overlay overlay;

    public SpaceView(GameClient c)
    {
        this.client = c;
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(800, 600));
        createStars(30);
        addMouseListener(Controls.getMouseListener());
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                Controls.lastMouseX = e.getX() - (getWidth() / 2);
                Controls.lastMouseY = e.getY() - (getHeight() / 2);
            }
            public void mouseMoved(MouseEvent e) {
                Controls.lastMouseX = e.getX() - (getWidth() / 2);
                Controls.lastMouseY = e.getY() - (getHeight() / 2);
            }
        });
    }

    public void createStars(int number) {
        Random random = new Random();
        starPositions = new int[number*2];
        for(int i = 0; i < starPositions.length; i++) {
            starPositions[i] = random.nextInt(2000);
        }
    }

    public void refresh() {
        if(getWidth() > 0 && getHeight() > 0) {
            focus = client.getPlayer().getPlayerShip();
            target = focus.getTarget();

            bg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics2D view = bg.createGraphics();

            drawStars(view);
            view.translate(getWidth() / 2, getHeight()/2);            
            
            for(Entity e : client.getPlayer().getVisibleEntities()) {
                drawEntity(view, e);
            }

            for(Entity e : client.getVisualEffects()) {
                drawEntity(view, e);
            }

            view.dispose();
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, Color.BLACK, this);
        drawHud(g);
        if(hasOverlay()) {
            overlay.draw(g);
        } else {
            drawMessageLog(g);
        }
    }

    private void drawEntity(Graphics2D view, Entity e) {
        if(focus.isWithinScanRange(e)) {
            BufferedImage image = e.getImage();
            double w = image.getWidth();
            double h = image.getHeight();
            double x = e.getX() - focus.getX();
            double y = e.getY() - focus.getY();
            transformer.setToRotation(Math.toRadians(e.getRotation()), w/2, h/2);
            view.translate(x - w/2, y - h/2);
            view.drawImage(image, transformer, this);
            if(e == target) {
                drawTargetBox(view, e, w, h);
            }
            view.translate(-(x - w/2), -(y - h/2));
        }
    }
    
    private void drawLine(Graphics2D view, double x1, double y1, double x2, double y2) {
        view.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    private void drawTargetBox(Graphics2D view, Entity e, double w, double h) {
        Color color = Color.lightGray;
        if(focus.getIsFriendly(e)) {
            color = Color.GREEN;
        } else if(focus.getIsHostile(e)) {
            color = Color.RED;
        }
        view.setColor(color);
        int r = (int) e.getRadius();
        view.translate(w/2-r, h/2-r);
        r = r*2;

        drawLine(view, 0, 0, r/4, 0);
        drawLine(view, 0, 0, 0, r/4);

        drawLine(view, r, 0, r/4*3, 0);
        drawLine(view, r, 0, r, r/4);

        drawLine(view, r, r, r/4*3, r);
        drawLine(view, r, r, r, r/4*3);

        drawLine(view, 0, r, r/4, r);
        drawLine(view, 0, r, 0, r/4*3);

        r = r/2;
        view.translate(-(w/2-r), -(h/2-r));
    }

    private void drawHud(Graphics graphics) {
        if(focus != null) {
            double hullPercent = focus.getHealthPercentage();
            double shieldPercent = focus.getShieldPercentage();
            double targetHullPercent = 0, targetShieldPercent = 0;

            if(target != null) {
                targetHullPercent = target.getHealthPercentage();
                targetShieldPercent = target.getShieldPercentage();
            }

            graphics.setColor(Color.white);
            graphics.drawString("Speed: " + (Math.ceil(focus.getSpeed().getLength()*100) / 5) + "m/s", 50, getHeight() - 75);
            graphics.drawString("Signiture: " + (int) (focus.getSigniture() * 100) + "%", 50, getHeight() - 60);

            graphics.setColor(Color.red);
            graphics.fillRect(50, getHeight() - 50, (int)(200 * hullPercent), 15);
            graphics.setColor(Color.blue);
            graphics.fillRect(50, getHeight() - 30, (int)(200 * shieldPercent), 15);

            graphics.setColor(Color.red);
            graphics.fillRect(getWidth() - 250, getHeight() - 50, (int)(200 * targetHullPercent), 15);
            graphics.setColor(Color.blue);
            graphics.fillRect(getWidth() - 250, getHeight() - 30, (int)(200 * targetShieldPercent), 15);

            graphics.setColor(Color.white);
            graphics.drawRect(50, getHeight() - 50, 200, 15);
            graphics.drawRect(50, getHeight() - 30, 200, 15);
            graphics.drawRect(getWidth() - 250, getHeight() - 50, 200, 15);
            graphics.drawRect(getWidth() - 250, getHeight() - 30, 200, 15);
        }
    }

    private void drawMessageLog(Graphics g) {
        Collection<String> log = client.getMessageLog(client.getLastMessageNo(), 4);
        int x = getWidth() / 4;
        int y = 20;
        int shownMessages = 0;
        Iterator<String> it = log.iterator();
        for(String s : log) {
            g.drawString(s, x, y);
            y += 20;
            shownMessages++;
            if(shownMessages >= 4)  break;
        }
    }

    private void drawStars(Graphics g) {
        g.setColor(Color.WHITE);
        double x, y;
        for(int i = 0; i < starPositions.length / 2; i++) {
            x = (starPositions[i*2] - focus.getX()) % getWidth();
            y = (starPositions[i*2+1] - focus.getY()) % getHeight();
            if(x < 0) x = getWidth() + x;
            if(y < 0) y = getHeight() + y;
            g.drawRect((int)x, (int)y, 0, 0);
        }
    }

    public static void setOverlay(Overlay o) {
        overlay = o;
    }

    public static boolean hasOverlay() {
        return overlay != null;
    }
}
