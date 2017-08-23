package Vessel;

/**
 *
 * @author Julian Craske
 */

import java.awt.geom.*;

public interface Dockable {
    public int getSectorID();

    public Point2D getLaunchLocation();

    public Core.Vector getLaunchVector();
}
