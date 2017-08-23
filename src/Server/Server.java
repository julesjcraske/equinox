package Server;



/**
 *
 * @author Julian Craske
 */



import java.net.*;
import java.io.*;
import java.util.*;

import jnet.*;
import Core.*;
import Messages.*;
import Player.*;
import Game.*;
import Datastructures.*;
import Core.Vector;
import Asteroids.*;
import Vessel.Ship.Miner;
import AI.*;
import javax.swing.*;

public class Server extends NetworkedGame implements GameServer, PacketHandler, Runnable {
    private static final int defaultPort = 8001;
    private static final int serverId = 0;
    private static long gameRate = 20;

    private boolean running = false;
    private int maxPlayers = 10;
    private UDPServer connection;

    private HashMap<Integer, Integer> timeouts = new HashMap<Integer, Integer>();
    private HashMap<Integer, ConnectionInfo> clients = new HashMap<Integer, ConnectionInfo>();

    private java.util.Timer timer = new java.util.Timer();
    private JTextArea log = new JTextArea();

    protected Server() {
        super();
        setPlayerId(serverId);
        connection = new UDPServer(this, defaultPort);
        timer.schedule(new PingTask(), 10000, 10000);

        JFrame window = new JFrame("Equinox Server");
        window.add(new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 200);
        window.setVisible(true);
    }

    public void run() {
        gameStart();
        long lastStep = 0;
        long time = 0;
        running = true;
        showMessage("running...");
        while(running) {            
            if((time = System.currentTimeMillis()) > lastStep + (1000 / gameRate)) {
                lastStep = time;
                runOneStep();
            }
            Thread.yield();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread thread = new Thread(server, "Server Gameplay");
        thread.start();
    }

    public void sendEntityUpdate(Entity e){
        broadcast(new EntityUpdate(e));
    }

    public void gameStart() {
        for(Sector s : getMap().getSectors()) {
            LinkedList<Entity> statics = new LinkedList<Entity>();
            spawnTeamHomes(s, statics);
            spawnAsteroids(s, statics);
        }
        timer.schedule(new TimerTask() {
            public void run() {
                for(Team t : Team.getTeams()) {
                    t.spawnShips();
                }
            }
        }, 10000, 60000);
    }

    private void spawnGarrison(Collection<Entity> statics, int sector, int team, double x, double y) {
        Entity garrison = Templates.getEntity("Garrison");
        garrison.setSector(sector);
        garrison.setTeam(team);
        garrison.setLocation(x, y);
        garrison.create();
        statics.add(garrison);
        spawnMiner(garrison);
    }

    private void spawnTeamHomes(Sector s, Collection<Entity> statics) {
        Set<Integer> teamHomes = s.getTeamHomes();
        if(teamHomes.size() == 1) {
            spawnGarrison(statics, s.getId(), teamHomes.iterator().next(), 0, 0);
        } else if(teamHomes.size() > 1) {
            double angle = Math.random() * 360;
            double baseDistance = 1500;
            Vector v = new Vector(angle, baseDistance);
            for(Integer team : teamHomes) {
                spawnGarrison(statics, s.getId(), team, v.getX(), v.getY());
                angle += (360 / teamHomes.size()) % 360;
                v.setDirection(angle);
            }
        }
    }

    private void spawnMiner(Entity base) {
        Miner miner = (Miner) Templates.getEntity("Miner");
        miner.setSector(base.getSectorID());
        miner.setTeam(base.getTeamNo());
        miner.setLocation(base.getLocation());
        miner.setPilot(new MinerPilot());
        miner.create();
    }

    private void spawnAsteroids(Sector s, Collection<Entity> statics) {
        int numberOfHelium = 4;
        if(s.getTeamHomes().size() == 1) {
            numberOfHelium = 2;
        }
        for(int i = 0; i < numberOfHelium; i++) {
            HeliumAsteroid heroid = new HeliumAsteroid();
            boolean placed = true;
            int tries = 0;
            int maxTries = 10;
            do {
                tries++;
                Core.Vector v = new Vector(Math.random() * 360, Math.random() * s.getSectorRadius());
                heroid.setLocation(v.getX(), v.getY());
                for(Entity e : statics) {
                    if(heroid.getDistanceToEntity(e) < 500) {
                        placed = false;
                        break;
                    }
                }
            } while(!placed && tries < maxTries);
            heroid.setSector(s.getId());
            heroid.create();
            statics.add(heroid);
        }
    }

    protected synchronized void addPlayer(DatagramPacket packet, Message message) {
        ConnectionRequest request;
        try {
            request = (ConnectionRequest) message;
        } catch (ClassCastException e) {
            return;
        }
        String playerName = request.getPlayerName();
        if(!playerName.equals("") && !getPlayerNames().contains(playerName)) {
            Integer id;
            for(id = 1; id <= maxPlayers; id++) {
                if(clients.get(id) == null) break;
            }
            if(id > maxPlayers) {
                System.out.println("Server is full.");
                return;
            }

            clients.put(id, new ConnectionInfo(id, playerName, packet.getAddress(), packet.getPort()));
            send(new ConnectionAccept(id), id);
            updatePlayerInfo(id, new PlayerInfo(playerName));
            broadcast(new PlayerInfoMessage(id, getPlayer(id)), id);

            for(Entity e : getEntities()) {
                send(new NewEntity(e), id);
                send(new EntityUpdate(e), id);
            }            
            
            timeouts.put(id, 0);
        } else {
            System.out.println("Player name in use or invalid: '" + playerName + "'");
        }
    }

    public void handlePacket(DatagramPacket packet) {
        try {
            ObjectInputStream in = UDPUtil.getInputStream(packet.getData());
            short code = in.readShort();
            Message message = Message.readMessage(code, in);
            if(message != null) {
                if(isConnected(message.getPlayerId(), packet.getAddress(), packet.getPort())) {
                    message.onRecieve(this);
                    message.exec(this);
                } else {
                    addPlayer(packet, message);
                }
            } else {
                System.out.println("Recieved unknown message: " + code);
            }
        } catch (IOException ex) {            
            ex.printStackTrace();
        }
    }

    public void broadcast(Message message) {
        broadcast(message, -1);
    }
    
    public void broadcast(Message message, int playerId) {
        broadcast(UDPUtil.createPacket(message), playerId);
    }

    public void broadcast(DatagramPacket p, int playerId) {
        for(Integer id : clients.keySet()) {
            if(id != playerId) {
                ConnectionInfo info = clients.get(id);
                p.setAddress(info.getAddress());
                p.setPort(info.getPort());
                connection.send(p);
            }
        }
    }

    public void send(Message message, int playerId) {
        ConnectionInfo info = clients.get(playerId);
        if(info != null) {
            connection.send(UDPUtil.createPacket(message), info.getAddress(), info.getPort());
        } else {
            System.out.println("Invalid player ID: " + playerId);
        }
    }    

    public synchronized void removePlayer(int playerId) {
        broadcast(new PlayerInfoMessage(playerId));
        clients.remove(playerId);
        deletePlayerInfo(playerId);
        Entity e;
        Iterator<Entity> it = getEntities().iterator();
        while(it.hasNext()) {
            e = it.next();
            if(e.getOwner() == playerId) {
                e.destroy();
                broadcast(new EntityUpdate(e), playerId);
                it.remove();
            }
        }
    }

    @Override
    public void addLocalEntity(Entity e) {
        super.addLocalEntity(e);
        broadcast(new NewEntity(e));
    }

    public Collection<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<String>();
        for(ConnectionInfo player : clients.values()) {
            names.add(player.getName());
        }
        return names;
    }

    protected boolean isConnected(int playerId, InetAddress address, int port) {
        ConnectionInfo info = clients.get(playerId);
        if(info == null) {
            return false;
        } else if(info.getPort() == port && info.getAddress().equals(address)) {
            return true;
        }
        return false;
    }

    public void showMessage(String s) {
        super.showMessage(s);
        String text = "";
        for(String m : getMessageLog(getLastMessageNo(), 10)) {
            text = m + "\n" + text;
        }
        log.setText(text);

    }

    public int getServerId() {
        return serverId;
    }

    public void quit() {
        running = false;
    }

    public void resetTimeout(int playerId) {
        timeouts.put(playerId, 0);
    }

    private synchronized void pingAllClients() {
        for(Integer playerId : clients.keySet()) {
            int tries = timeouts.get(playerId);
            tries++;
            if(tries > 2) {
                showMessage(clients.get(playerId).getName() + " timed out.");
                removePlayer(playerId);
            } else {
                timeouts.put(playerId, tries);
                send(new Ping(), playerId);
            }
        }
    }

    private class PingTask extends TimerTask {
        public void run() {
            pingAllClients();
        }
    }

    public java.util.Timer getTimer() {
        return timer;
    }

    public static int getDefaultPort() {
        return defaultPort;
    }
}
