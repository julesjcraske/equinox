package jnet;

/**
 *
 * @author Julian Craske
 */

import java.io.IOException;
import java.net.*;

public class UDPServer extends UDPUtil {
    private PacketHandler handler;
    private DatagramSocket socket;
    private int port;

    private boolean running = true;
    private byte[] buffer  = new byte[packetSize];
    private DatagramPacket packet;

    public UDPServer(PacketHandler ph, int port) {
        handler = ph;
        init(port);
    }

    private void init(int p) {
        this.port = p;
        try {
            socket = new DatagramSocket(port);
            Thread reciever = new Thread("Server UDP Reciever") {
                public void run() {
                    println("Server listening on port: " + port);
                    while(running) {
                        recieve();
                        yield();
                    }
                }
            };
            reciever.start();
        } catch (SocketException ex) {
            println("Unable to initialise server socket.");
        }
    }

    public void recieve() {
        packet = new DatagramPacket(buffer, packetSize);
        try {
            socket.receive(packet);
            handler.handlePacket(packet);
        } catch (IOException ex) {
            println("Exception recieving packet: " + ex);
        }
    }

    public void send(DatagramPacket p, InetAddress address, int port) {
        p.setAddress(address);
        p.setPort(port);
        send(p);
    }

    public void send(DatagramPacket p) {
        try {
            socket.send(p);
        } catch (IOException ex) {
            println("Exception sending datagram packet to "
                    + p.getAddress().getHostName() + "\n" + ex + "\n" + p);
        }
    }

    public void println(String s) {
        super.println(s);
        handler.showMessage(s);
    }
}
