package jnet;

/**
 *
 * @author Julian Craske
 */

import java.net.*;

public interface PacketHandler {
    public void handlePacket(DatagramPacket p);

    public void showMessage(String s);
}
