package Player;


import java.util.*;

import Datastructures.*;
import Vessel.*;
import Core.*;
import Vessel.Ship.SpaceShip;

public class Player extends Pilot
{
    private static PlayerListener listener;
    private static short INBASE = 0;
    private static short INFLIGHT = 1;
    private static short INTURRET = 2;

    private short state = INBASE;

    private final PlayerInfo info;
    private SpaceShip playerShip;
    private int destination;

    private Collection<Entity> visibleEntities = new TreeSet<Entity>();
    private boolean[] active = new boolean[Command.values().length];

    private int defaultShip = "Scout".hashCode();

    public enum Command {
        Thrust {
            protected void active(Player player, Vessel ship) {
                ship.setThrusting(true);
            }
            protected void inactive(Player player, Vessel ship) {
                ship.setThrusting(false);
            }
        },
        Left {
            protected void active(Player player, Vessel ship) {
                if(ship.getHullType() == Vessel.CORVETTE) {
                    ship.turnLeft();
                } else {
                    ship.thrustLeft();
                }
            }
        },
        Right {
            protected void active(Player player, Vessel ship) {
                if(ship.getHullType() == Vessel.CORVETTE) {
                    ship.turnRight();
                } else {
                    ship.thrustRight();
                }
            }
        },
        Reverse {
            protected void active(Player player, Vessel ship) {
                ship.thrustReverse();
            }
        },
        Fire {
            protected void active(Player player, Vessel ship) {
                ship.fire();
            }
        },
        FireMissiles {
            protected void active(Player player, Vessel ship) {
                ship.fireMissiles();
            }
        },
        Cloak {
            protected void active(Player player, Vessel ship) {
                ship.toggleCloak();
            }
        },
        Boost {
            protected void active(Player player, Vessel ship) {
                ship.fireAfterburner(true);
            }
            protected void inactive(Player player, Vessel ship) {
                ship.fireAfterburner(false);
            }
        },
        Land {
            protected void active(Player player, Vessel ship) {
                player.land();
            }
        },
        NextTarget {
            protected void active(Player player, Vessel ship) {
                ship.setTarget(player.getNextTarget(ship, new TargetFilter() {
                    public boolean filter(Entity self, Entity target) {
                        return target != self && !target.isProjectile();
                    }
                }));
            }
        },
        ClosestEnemy {
            protected void active(Player player, Vessel ship) {
                ship.setTarget(player.getClosest(ship, new TargetFilter() {
                    public boolean filter(Entity self, Entity target) {
                        return target.isShip() && self.getIsHostile(target);
                    }
                }));
            }
        },
        TargetSelf {
            protected void active(Player player, Vessel ship) {
                ship.setTarget(ship);
            }
        },
        Hyperspace {
            protected void active(Player player, Vessel ship) {
                if(player.destination != ship.getSectorID()) {
                    ship.hyperspace(player.destination);
                }
            }
        };

        protected abstract void active(Player player, Vessel ship);

        protected void inactive(Player player, Vessel ship) {
            //Override this method to define behaviour when the command is not active
        }
    }

    public Player(String name)
    {
        info = new PlayerInfo(name);
    }    

    public String getName() {
        return info.getName();
    }

    public void control(Vessel ship) {
        visibleEntities = playerShip.getVisibleEntities();
        Command[] commands = Command.values();
        active = Controls.getPlayerPacket();

        if(state == INFLIGHT) {
            if(ship.getHullType() == Vessel.CORVETTE || state == INTURRET) {
                ship.turnTurretsTowardAngle(ship.getAngleToXY(ship.getX() + Controls.lastMouseX, ship.getY() + Controls.lastMouseY));
            } else {
                ship.turnTowardAngle(ship.getAngleToXY(ship.getX() + Controls.lastMouseX, ship.getY() + Controls.lastMouseY));
            }

            for(int i = 0; i < commands.length; i++) {
                if(active[i]) {
                    commands[i].active(this, ship);
                } else {
                    commands[i].inactive(this,ship);
                }
            }
        }       
    }

    public void setDestination(int sectorId) {
        destination = sectorId;
    }

    public int getDestination() {
        return destination;
    }

    public Entity getFocus() {
        return playerShip;
    }

    public void joinTeam(int team) {
        info.setTeam(team);
        setDock(Team.getTeam(team).getPrimaryBase());
        listener.onPlayerJoinedTeam(this);
        listener.onPlayerChange(this);
    }

    public int getTeam() {
        return info.getTeam();
    }

    public void increaseKills() {
        info.increaseKills();
        increaseScore(100);
    }

    public void increaseDeaths() {
        info.increaseDeaths();
        state = INBASE;
        listener.onPlayerChange(this);
    }

    public void increaseScore(int num) {
        info.increaseScore(num);
        listener.onPlayerChange(this);
    }

    public PlayerInfo getInfo() {
        return info;
    }

    public boolean isInFlight() {
        return state == INFLIGHT;
    }
    
    public SpaceShip getPlayerShip() {
        return playerShip;
    }

    public void getNewShip() {
        setPlayerShip((SpaceShip) Templates.getEntity(defaultShip));
    }
    
    public void setPlayerShip(SpaceShip ship) {
        if(playerShip != null && !playerShip.isDestroyed()) {
            playerShip.destroy();
        }
        playerShip = ship;
        ship.setTeam(getTeam());
        ship.setPilot(this);
        ship.create();
    }

    public Collection<Entity> getVisibleEntities() {
        return visibleEntities;
    }

    public void launch() {
        if(state == INBASE) {
            if(playerShip == null) {
                 getNewShip();
            }
            launch(playerShip);
            state = INFLIGHT;
            listener.onPlayerLaunched(this);
        }
    }

    public void land() {
        if(state == INFLIGHT) {             
            if(dockAtBase(playerShip)) {
                state = INBASE;
                listener.onPlayerLand(this);
            }
        }
    }

    public void occupyTurret(SpaceShip ship) {
        if(state == INBASE) {
            state = INTURRET;
            playerShip = ship;
            listener.onPlayerLaunched(this);
        }
    }

    public void setDefaultShip(int hash) {
        defaultShip = hash;
    }

    public static void setPlayerListener(PlayerListener l) {
        listener = l;
    }
}
