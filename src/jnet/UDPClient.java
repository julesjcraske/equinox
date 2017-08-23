/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jnet;

/**
 *
 * @author Julian Craske
 */

import java.io.*;
import java.net.*;

import Messages.*;

public class UDPClient extends UDPUtil {
    private DatagramSocket socket;
    private InetAddress host;
    private int port;

    protected int bytesSent = 0;
    protected int bytesPerSecond = 0;

    public UDPClient() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println("Failed to create UDP socket.");
            ex.printStackTrace();
        }
    }

    public void connect(String host, int port) throws UnknownHostException {
        this.host = InetAddress.getByName(host);
        this.port = port;
    }

    public void disconnect() {
        
    }

    public void send(Message message) throws IOException {
        send(createPacket(message));
    }

    private void send(DatagramPacket packet) throws IOException {
        packet.setAddress(host);
        packet.setPort(port);
        socket.send(packet);
        bytesSent += packet.getLength();
    }

    public DatagramPacket recieve() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);
        socket.receive(packet);
        return packet;
    }

    public int getBytesPerSecond() {
        return bytesPerSecond;
    }

    public void recordBytesPerSecond() {
        bytesPerSecond = bytesSent;
        bytesSent = 0;
    }
}
