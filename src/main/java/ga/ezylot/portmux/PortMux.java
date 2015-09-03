package ga.ezylot.portmux;

import ga.ezylot.portmux.services.*;

import java.io.*;
import java.net.*;

public class PortMux {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        ServiceDetector.registerService("HTTP", new HTTP());
        ServiceDetector.registerService("SSH", new SSH_2_0_OPENSSH());

        while(true) {
            Socket OutsideSocket = server.accept();
            Runnable sess = new Session(OutsideSocket);
            new Thread(sess).start();
        }
    }
}
