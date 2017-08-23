package Core;

/**
 *
 * @author Julian Craske
 */

import java.io.*;

public class NamedObject implements Serializable {
    private String name;

    public NamedObject(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
