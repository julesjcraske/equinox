/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Datastructures;

/**
 *
 * @author Julian Craske
 */

import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;


public class ImagePool {
    private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    public static BufferedImage getImage(String name) {
        name = name + ".png";
        BufferedImage image = images.get(name);
        if(image == null) {
            image = loadImage(name, false);
        }
        return image;
    }

    public static BufferedImage getImageSilently(String name) {
        name = name + ".png";
        BufferedImage image = images.get(name);
        if(image == null) {
            image = loadImage(name, true);
        }
        return image;
    }

    private static BufferedImage loadImage(String imageFilePath, boolean silent) {
        BufferedImage i = null;
        try {
            i = ImageIO.read(new File("src\\images\\" + imageFilePath));
            images.put(imageFilePath, i);
        }
        catch(IOException e) {
            if(!silent) System.out.println("There was an error loading the image: " + imageFilePath);
        }
        return i;
    }
}
