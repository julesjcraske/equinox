package Datastructures;

/**
 *
 * @author jc271
 */


import Vessel.Ship.Miner;
import Vessel.Ship.SpaceShip;
import java.util.*;

import Core.*;
import Vessel.Structure.*;
import Commodities.*;
import Projectiles.*;
import Asteroids.*;

public abstract class Templates {
    public static HashMap<Integer, Commodity> commodities = new HashMap<Integer, Commodity>();
    public static HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();

    static {
        initialiseTemplates();
    }

    //public Bullet(String name, String imageFilePath, double turnRate, int life, double damage)
    //public Missile(String name, String imageFilePath, double turnRate, int maxHitPoints, int delay, int life, double damage, double acceleration, double maxSpeed)

    //public Weapon(String name, String description, double mass, int price, Projectile ammunition, int minGunFireDelay, double barrelSpeed, double spread, int rackSize, int reloadRate)
    //public Afterburner(String name, String description, double mass, int price, double acceleration, int fuelCapacity, double rechargeRate)
    //public Shield(String name, String description, double mass, int price, int maxShield, double regenRate)
    //public Cloak(String name, String description, double mass, int price, double power) {

    //public SpaceShip(String name, short hullType, String description, int price, String imageFilePath, double turnRate, int maxHitPoints, int scanRange, double signiture, double acceleration, double maxSpeed, int numberOfWeapons, Commodity ... equipment) {

    public static void initialiseTemplates() {
        Bullet laser = new Bullet("Laser", "laserBlue", 0, 20, 2.6);
        put(laser);
        Weapon laserCannon = new FrontWeapon("Laser Cannon", "plasmaac1.wav", "The standard weapon on all terran vessels.", 2, 300, laser, 3, 16, 3, 50, 40);
        put(laserCannon);

        Bullet disruptor = new Bullet("Disruptor", "blueStar", 3, 24, 2);
        put(disruptor);
        Weapon disruptorCannon = new FrontWeapon("Disruptor Cannon", "plasmaac1.wav", "A weak but long range cannon designed for finishing off weakened vessels.", 2, 300, disruptor, 3, 25, 1, 40, 20);
        put(disruptorCannon);

        Bullet minigunBullet = new Bullet("Minigun Bullet", "minigunBullet", 30, 16, 3.4);
        put(minigunBullet);
        Weapon minigun = new FrontWeapon("Minigun", "plasmaac1.wav", "A powerful but short range weapon.", 2, 300, minigunBullet, 2, 12, 5, 60, 30);
        put(minigun);
        
        Missile missile = new Missile("Missile", "missile", 2, 3, 10, 45, 40, 1.8, 20);
        put(missile);        
        Weapon missile2Launcher = new FrontWeapon("Missile Launcher", "missilelaunch.wav", "Rack size of 2.", 3, 300, missile, 20, 2, 0, 2, 100);
        put(missile2Launcher);
        Weapon missile4Launcher = new FrontWeapon("Missile Launcher", "missilelaunch.wav", "Rack size of 4.", 3, 300, missile, 20, 2, 0, 4, 100);
        put(missile4Launcher);

        Missile hunterMissile = new Missile("Hunter Missile", "missile", 4, 3, 10, 80, 30, 2.1, 24);
        put(hunterMissile);
        Weapon hunterMissileLauncher = new FrontWeapon("Hunter Missile Launcher", "missilelaunch.wav", "Rack size of 3.", 3, 300, hunterMissile, 40, 4, 0, 3, 160);
        put(hunterMissileLauncher);

        Shield smallShield = new Shield("Small Shield", "A basic shield for small vessels.", 5, 500, 100, 0.1);
        put(smallShield);
        Shield medShield = new Shield("Medium Shield", "A basic shield.", 5, 500, 150, 0.2);
        put(smallShield);

        Afterburner booster = new Afterburner("Booster", "Boooooost!", 2, 300, 0.32, 60, 0.6);
        put(booster);
        Afterburner heavyBooster = new Afterburner("Heavy Booster", "Boooooost!", 2, 300, 0.48, 100, 0.8);
        put(heavyBooster);
        
        Cloak cloak = new Cloak("Cloak", "Lowers your ship signiture making you harder to detect.", 3, 300, 0.7);
        put(cloak);
        
        SpaceShip scout = new SpaceShip("Scout", SpaceShip.FIGHTER, "A light combat and reconnaisance vessel, good at detecting cloaked ships.", 1000, "Scout", 4.2, 60, 3000, 0.72, 0.52, 15, 2, laserCannon, missile2Launcher, smallShield);
        put(scout);

        SpaceShip fighter = new SpaceShip("Fighter", SpaceShip.FIGHTER, "A flexible combat vessel with a variety of equipment.", 1000, "Fighter", 5.4, 100, 2000, 1, 0.48, 10, 3, laserCannon, laserCannon, missile4Launcher, smallShield, booster);
        put(fighter);
        
        SpaceShip stealthFighter = new SpaceShip("Stealth Fighter", SpaceShip.FIGHTER, "A stealthy combat vessel designed to pick off enemies from a distance.", 1000, "StealthFighter", 3.8, 80, 2500, 0.88, 0.36, 12, 4, disruptorCannon, disruptorCannon, hunterMissileLauncher, hunterMissileLauncher, cloak);
        put(stealthFighter);
        
        SpaceShip interceptor = new SpaceShip("Interceptor", SpaceShip.FIGHTER, "A fast and agile combat vessel that lacks missiles.", 1000, "Interceptor", 6.6, 200, 1600, 0.8, 0.32, 8, 2, minigun, minigun, heavyBooster);
        put(interceptor);

        //SpaceShip testTank = new SpaceShip("Test Tank", SpaceShip.FIGHTER, "Has no weaponry but can take a beating.", 1000, "Interceptor", 7.3, 20000, 3000, 2, 0.33, 12, 0, booster);
        //put(testTank);

        
//        SpaceShip destroyer = new SpaceShip("Destroyer", SpaceShip.CAPITAL, "Killing machine.", 1000, "Destroyer", 3.3, 120, 2000, 2.5, 1.2, 8, 6, disruptorCannon, disruptorCannon, disruptorCannon, disruptorCannon, missileLauncher, missileLauncher, smallShield);
//        put(destroyer);

//        Missile quickfireMissile = new Missile("Quickfire Missile", "missile", 5, 2, 8, 70, 7, 7, 12);
//        put(quickfireMissile);
//        Weapon quickfireLauncher = new FrontWeapon("Quickfire Missile Launcher", "Big bada boom!", 3, 300, quickfireMissile, 14, 3, 0);
//        put(quickfireLauncher);
//        Shield medShield = new Shield("Medium Shield", "A basic shield for medium vessels", 5, 500, 50, 0.1);
//        put(medShield);
//        SpaceShip missileFrigate = new SpaceShip("Missile Frigate", SpaceShip.FRIGATE, "A medium class ship effective against medium and heavy class vessels.", 3000, "MissileFrigate", 4, 50, 2000, 1, 0.8, 8, 2, quickfireLauncher, quickfireLauncher, medShield);
//        put(missileFrigate);

//        Bullet acBullet = new Bullet("AC Bullet", "blueStar", 0, 25, 0.6);
//        put(acBullet);
//        Weapon acTurret = new Turret("Autocannon Turret", "Shred your foes with this mighty turret.", 3, 300, acBullet, 2, 18, 6);
//        put(acTurret);
//        SpaceShip flakFrigate = new SpaceShip("Flak Frigate", SpaceShip.CORVETTE, "A medium class ship effective against medium and heavy class vessels.", 3000, "FlakFrigate", 4, 50, 2000, 1, 0.8, 8, 2, acTurret, acTurret, medShield);
//        put(flakFrigate);

//        Bullet weakLaser = new Bullet("Weak Laser", "laserBlue", 0, 20, 0.7);
//        put(weakLaser);
//        Weapon weakLaserCannon = new FrontWeapon("Weak Laser Cannon", "Burn through your enemies hull!", 2, 300, weakLaser, 3, 14, 7);
//        put(weakLaserCannon);
//        Shield weakShield = new Shield("Weak Shield", "Provides almost no protection.", 5, 200, 10, 0.02);
//        put(weakShield);
//        SpaceShip enemy = new SpaceShip("Enemy", SpaceShip.FIGHTER, "A light combat vessel.", 1000, "Enemy", 3.7, 15, 1500, 1, 1.6, 12, 1, weakLaserCannon, weakShield);
//        put(enemy);

        Structure garrison = new Base("Garrison", "earth", 1000, 5000, 2, scout, scout);
        put(garrison);

        put(new HeliumAsteroid());
        put(new Miner(medShield));
    }

    public static void put(Entity e) {
        entities.put(e.getName().hashCode(), e);
    }

    public static void put(Commodity c) {
        commodities.put(c.getName().hashCode(), c);
    }

    public static Entity getEntity(int hash) {
        return entities.get(hash).clone();
    }

    public static Collection<Entity> getEntities() {
        return entities.values();
    }

    public static Entity getEntity(String name) {
        return getEntity(name.hashCode());
    }

    public static Commodity getCommodity(int hash) {
        return commodities.get(hash).copy();
    }
}
