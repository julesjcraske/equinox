package Vessel.Ship;

/**
 *
 * @author Julian Craske
 */

import java.io.*;
import java.util.*;
import java.awt.image.*;

import Datastructures.*;

public class ShipModel implements Serializable {
    private static HashMap<Integer, ShipModel> models = loadModels();
    private static final String modelFile = "src/models.eq";

    private String baseImagePath;
    private Mount[] mounts = new Mount[10];

    public ShipModel(String baseImagePath) {
        this.baseImagePath = baseImagePath;
        for(int i = 0; i < mounts.length; i++) {
            mounts[i] = new Mount();
        }
    }

    public Mount getMount(int index) {
        return mounts[index];
    }

    public Mount[] getMounts() {
        return mounts;
    }

    public String getBaseImagePath() {
        return baseImagePath;
    }

    public BufferedImage getImage() {
        return ImagePool.getImage(baseImagePath);
    }

    public int hashCode() {
        return baseImagePath.hashCode();
    }

    public static ShipModel getModel(String baseImagePath) {
        int hash = baseImagePath.hashCode();
        if(models.containsKey(hash)) {
            return models.get(hash);
        } else {
            ShipModel model =  new ShipModel(baseImagePath);
            models.put(hash, model);
            return model;
        }
    }

    public static ShipModel getModel(int modelNo) {
        return models.get(modelNo);
    }

    public static Vector<ShipModel> getModels() {
        Vector<ShipModel> v = new Vector<ShipModel>();
        for(ShipModel m : models.values()) {
            v.add(m);
        }
        return v;
    }

    public static void addModel(ShipModel m) {
        models.put(m.hashCode(), m);
    }

    public static void saveModels() {
        try {
            File f = new File(modelFile);
            f.delete();
            f.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            for(ShipModel m : models.values()) {
                out.writeObject(m);
            }
            out.close();
            System.out.println("Ship models saved.");
        } catch (FileNotFoundException ex) {
            System.out.println("Error saving ship models: " + ex);
        } catch (IOException ex) {
            System.out.println("Error saving ship models: " + ex);
        }
    }

    public static HashMap<Integer, ShipModel> loadModels() {
        HashMap<Integer, ShipModel> models = new HashMap<Integer, ShipModel>();
        File f = new File(modelFile);
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            ShipModel model = (ShipModel) in.readObject();
            while(model != null) {
                models.put(model.hashCode(), model);
                try {
                    model = (ShipModel) in.readObject();
                } catch(EOFException ex) {
                    model = null;                    
                    in.close();
                }
            }            
        } catch (FileNotFoundException ex) {
            System.out.println("Model file not found: " + modelFile);
        } catch (IOException ex) {
            System.out.println("Error loading model file: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error loading model file: " + ex);
        }
        return models;
    }

    public static Vector<String> getMountNames() {
        Vector<String> v = new Vector<String>();
        for(int i = 1; i < 5; i++) {
            v.add("Weapon " + i);
        }
        for(int i = 1; i < 3; i++) {
            v.add("Missile " + i);
        }
        for(int i = 1; i < 5; i++) {
            v.add("Turret " + i);
        }
        return v;
    }

    public String toString() {
        return baseImagePath;
    }
}
