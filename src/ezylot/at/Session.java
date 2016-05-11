package ezylot.at;

import ezylot.at.services.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Session implements Runnable {

    private Socket OutsideSocket;

    public Session(Socket s) {
        this.OutsideSocket = s;
    }

    @Override
    public void run() {

        Scanner fromClinetToPortMux = null;
        PrintWriter fromPortMuxToClient = null;
        Scanner fromServerToPortMux = null;
        PrintWriter fromPortMuxToServer = null;

        try {
            fromClinetToPortMux = new Scanner(OutsideSocket.getInputStream());
            fromPortMuxToClient = new PrintWriter(OutsideSocket.getOutputStream(), true);
        } catch (IOException exc) {
            exc.printStackTrace();
            return;
        }

        String detectionMessage;
        try {
            detectionMessage = fromClinetToPortMux.useDelimiter("(\r\n)|(\n)").next();
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }

        Service sess = ServiceDetector.detect(detectionMessage);
        if(sess == null) {
            System.out.println("Could not detect type of connection.");
            System.out.println("Start was: \n ------------ \n" + detectionMessage.substring(0, 50) + "\n ------------ \n");
            return;
        }

        Socket InsideSocket = null;
        try {
            InsideSocket = new Socket();
            InsideSocket.connect(new InetSocketAddress(sess.getTargetIP(), sess.getPort()), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            fromServerToPortMux = new Scanner(InsideSocket.getInputStream());
            fromPortMuxToServer = new PrintWriter(InsideSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fromPortMuxToServer.println("teastasdadw");

        final Scanner finalFromClientToPortMux = fromClinetToPortMux;
        final PrintWriter finalFromPortMuxToServer = fromPortMuxToServer;
        final Scanner finalFromServerToPortMux = fromServerToPortMux;
        final PrintWriter finalFromPortMuxToClient = fromPortMuxToClient;

        // Portmux to Client communication
        Executors.newSingleThreadExecutor().execute(new Thread(() -> {
            try {

                finalFromPortMuxToServer.println(detectionMessage);
                System.out.println(detectionMessage);

                while (finalFromClientToPortMux.hasNextLine()) {
                    String buffer = "";

                    buffer = finalFromClientToPortMux.nextLine();
                    System.out.println(buffer);
                    finalFromPortMuxToServer.println(buffer);
                }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        }));

        // Portmux to Server Communication
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    while (finalFromServerToPortMux.hasNextLine()) {
                        String buffer;

                        buffer = finalFromServerToPortMux.nextLine();
                        finalFromPortMuxToClient.println(buffer);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
    }
}
