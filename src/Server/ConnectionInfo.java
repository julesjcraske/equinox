/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

/**
 *
 * @author Julian Craske
 */

import java.net.*;

public class ConnectionInfo {
    private int id;
    private String name;
    private InetAddress address;
    private int port;

    public ConnectionInfo(int id, String name, InetAddress address, int port) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
