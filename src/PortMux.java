import com.sun.corba.se.spi.activation.Server;
import services.HTTP;

import java.io.*;
import java.net.*;

public class PortMux {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);

        ServiceDetector.registerService("HTTP", new HTTP());

        while(true) {
            Socket OutsideSocket = server.accept();
            Runnable sess = new Session(OutsideSocket);
            new Thread(sess).run();
        }
    }
}
