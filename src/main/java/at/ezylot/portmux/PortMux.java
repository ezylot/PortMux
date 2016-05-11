package at.ezylot.portmux;

import at.ezylot.portmux.services.HTTP;
import at.ezylot.portmux.services.SSH_2_0_OPENSSH;

import java.io.*;
import java.net.*;

public class PortMux {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        ServiceDetector.registerService("HTTP", new HTTP("192.168.1.201"));
        ServiceDetector.registerService("SSH", new SSH_2_0_OPENSSH("192.168.1.201", 22));

        while(true) {
            Socket OutsideSocket = server.accept();
            Runnable sess = new Session(OutsideSocket);
            new Thread(sess).start();
        }
    }
}
