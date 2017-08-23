package Display;


import java.awt.*;
import javax.swing.*;

import Game.*;

public class FlightView extends Screen
{
    private SpaceView spaceView;
    private TargetView targetView;
    private RadarView radarView;
    private TextHud textHud;
    private MapView mapView;

    private GameClient client;

    public FlightView(GameClient c)
    {
        this.client = c;
        spaceView = new SpaceView(client);
        textHud = new TextHud(client);
        radarView = new RadarView(client);
        targetView = new TargetView(client);
        mapView = new MapView(client);
        Container bottomPanel = new JPanel();
        
        setLayout(new BorderLayout());
        
        add(spaceView, BorderLayout.CENTER);
        Container rightPanel = new JPanel();
            rightPanel.setLayout(new GridLayout(4, 1));
            rightPanel.add(textHud);
            rightPanel.add(targetView);
            rightPanel.add(radarView);
            rightPanel.add(mapView);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void update() {
        spaceView.refresh();
        targetView.refresh();
        radarView.refresh();
        mapView.refresh();
        textHud.refresh();
    }
}
