package Display.Menus;

/**
 *
 * @author Julian Craske
 */

import Display.*;
import Vessel.Ship.SpaceShip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Core.*;
import Datastructures.*;
import Player.*;
import Game.*;
import AI.*;

public class ShipSelect extends Screen {
    private final JList ships;
    private final JTextArea description;
    private final Player player;
    private Vector<SpaceShip> availiableShips = new Vector<SpaceShip>();
    private Vector<SpaceShip> turretableShips = new Vector<SpaceShip>();

    public ShipSelect(GameClient client) {
        player = client.getPlayer();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Ship Select");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.PLAIN, 50));
        add(title, BorderLayout.NORTH);

        Container selectAndLaunch = new JPanel();
        add(selectAndLaunch, BorderLayout.CENTER);
 
        //Create the selectable list of availiable ships
        for(Entity e : Team.getTeam(player.getTeam()).getAvailiableShips()) {
            availiableShips.add((SpaceShip) e);
        }
        ships = new JList(availiableShips);
        selectAndLaunch.add(ships);
        
        JButton launch = new JButton("Launch");
        launch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selection = ships.getSelectedIndex();
                player.setPlayerShip(availiableShips.get(selection).clone());
                player.launch();
            }            
        });
        selectAndLaunch.add(launch);

        JButton spawnEnemy = new JButton("Spawn Enemy");
        spawnEnemy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selection = ships.getSelectedIndex();
                SpaceShip enemy = availiableShips.get(selection).clone();
                int team;
                if(player.getTeam() == 1) {
                    team = 2;
                } else {
                    team = 1;
                }
                enemy.setTeam(team);
                enemy.setPilot(new ComputerPilot(team));
                enemy.setSector(player.getDock().getSectorID());
                enemy.setLocation(Math.random() * 4000 - 2000, Math.random() * 4000 - 2000);
                enemy.setRotation(Math.random() * 360);
                enemy.create();
            }
        });
        selectAndLaunch.add(spawnEnemy);

        description = new JTextArea(20, 50);
        ships.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                description.setText(availiableShips.get(ships.getSelectedIndex()).getFullDescription());
            }
        });
        selectAndLaunch.add(description);

        Container controls = new JPanel(new GridLayout(10, 4));
        for(Player.Command command : Controls.getControls().keySet()) {
            JLabel control = new JLabel();
            control.setText(command.name() + ": " + Controls.getKey(command));
            controls.add(control);
        }
        add(controls, BorderLayout.SOUTH);
        

//        JButton transport = new JButton("Transport");
//        transport.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                Entity target = transportSelect();
//                if(target != null && target.isShip()) {
//                    player.occupyTurret((SpaceShip) target);
//                }
//            }
//        });
//        add(transport);
    }

    public void update() {
        
    }

    private Entity transportSelect() {
        JFrame window = new JFrame("Select transport location");
        window.setContentPane(new JPanel(new FlowLayout()));
        Entity selection = null;
        boolean done = false;

        Team team = Team.getTeam(player.getTeam());
        if(team != null) {
            for(SpaceShip s : team.getActiveShips()) {
                if(s.hasTurrets()) {
                    turretableShips.add(s);                    
                }
            }
            System.out.println("Availiable ships to turret " + turretableShips.size());
        }
        JList turrets = new JList(turretableShips);
        window.add(turrets);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        window.add(cancel);

        window.setSize(300, 200);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        while(!done) {

        }
        return selection;
    }
}
