/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Projectiles;

/**
 *
 * @author Julian Craske
 */

import Core.*;

public interface ProjectileListener {
    public void onProjectileHit(Entity e, Projectile p, double damage);

    public void onProjectileMiss(Projectile p);
}
