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

import Messages.Message;

public abstract class UDPUtil {
    protected static int packetSize = 128;

    public static ObjectInputStream getInputStream(byte[] bytes) {
        try {
            return new ObjectInputStream(new ByteArrayInputStream(bytes));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ObjectOutputStream getOutputStream(ByteArrayOutputStream bytes) {
        try {
            return new ObjectOutputStream(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static DatagramPacket createPacket(Message msg) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(packetSize);
            ObjectOutputStream out = getOutputStream(bytes);
            msg.write(out);
            out.flush();
            out.close();
            return new DatagramPacket(bytes.toByteArray(), bytes.size());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void println(String s) {
        System.out.println(s);
    }
}
