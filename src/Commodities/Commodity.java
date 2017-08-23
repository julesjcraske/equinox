package Commodities;

import Vessel.*;
import Core.*;

public class Commodity extends NamedObject
{
    private String description;
    private double mass;
    private int price;    
    
    public Commodity(String name, String description, double mass, int price)
    {
        super(name);
        this.description = description;
        this.mass = mass;
        this.price = price;
    }
    
    public boolean equip(Vessel ship) {
        return false;
    }
    
    public boolean unequip(Vessel ship) {
        return false;
    }
    
    public boolean use(Vessel ship) {
        return false;
    }
    
    public int getPrice() {
        return price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String toString() {
        return getName() + "\n" + description + "\n Mass per unit: " + mass;
    }
    
    public double getMass() {
        return mass;
    }
    
    public boolean equals(Commodity c) {
        return c.getName().equals(getName());
    }
    
    public Commodity copy() {
        return new Commodity(getName(), description, mass, price);
    }            
}
