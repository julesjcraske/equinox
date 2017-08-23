package Core;

import java.awt.image.*;
import java.util.*;

import Datastructures.ImagePool;

public class Explosion extends Entity
{
    private static HashMap<String, String[]> explosions = new HashMap<String, String[]>();

    private String[] images = null;
    private int acts = 0;

    public Explosion(String baseFilePath) {
        super(baseFilePath, baseFilePath + "_00", 0);
        int i = 0;
        images = explosions.get(baseFilePath);
        if(images == null) {
            ArrayList<String> newImages = new ArrayList<String>();
            BufferedImage im = getImage();
            String suffix = "00";
            while(im != null) {
                newImages.add(baseFilePath + "_" + suffix);
                i++;
                suffix = i + "";
                if(i < 10) {
                    suffix = "0" + suffix;
                }
                im = ImagePool.getImageSilently(baseFilePath + "_" + suffix);
            }
            String[] ims = new String[newImages.size()];
            i = 0;
            for(String s : newImages) {
                ims[i] = s;
                i++;
            }
            images = ims;
            explosions.put(baseFilePath, ims);
        }
    }

    public void act() {
        if(acts < images.length) {
            setImage(images[acts]);
            acts++;
        } else {
            destroy();
        }
    }

    public Explosion clone() {
        return new Explosion(getName());
    }
}
