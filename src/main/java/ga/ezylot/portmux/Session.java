package ga.ezylot.portmux;
 
import ga.ezylot.portmux.services.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Session implements Runnable {

    private Socket OutsideSocket;

    public Session(Socket s) {
        this.OutsideSocket = s;
    }

    @Override
    public void run() {


        InputStream fromClinetToPortMux = null;
        OutputStream fromPortMuxToClient = null;
        InputStream fromServerToPortMux = null;
        OutputStream fromPortMuxToServer = null;

        try {
            fromClinetToPortMux = OutsideSocket.getInputStream();
            fromPortMuxToClient = OutsideSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] detectionMessage = new byte[1024];
        int detetionLenght = -1;
        try {
            detetionLenght = fromClinetToPortMux.read(detectionMessage);
        } catch (Exception exc) {

         }
        Service sess = ServiceDetector.detect(new String(detectionMessage));
        if(sess == null) return;

        Socket InsideSocket = null;
        try {
            InsideSocket = new Socket(sess.getIP(), sess.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fromServerToPortMux = InsideSocket.getInputStream();
            fromPortMuxToServer = InsideSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final Socket finalInsideSocket = InsideSocket;
        final InputStream finalFromClinetToPortMux = fromClinetToPortMux;
        final OutputStream finalFromPortMuxToServer = fromPortMuxToServer;
        final InputStream finalFromServerToPortMux = fromServerToPortMux;
        final OutputStream finalFromPortMuxToClient = fromPortMuxToClient;

        // Message form Client
        final int finalDetetionLenght = detetionLenght;

        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int len;

                finalFromPortMuxToServer.write(detectionMessage, 0, finalDetetionLenght);

                while (true) {

                    if(finalInsideSocket.isClosed() && OutsideSocket.isClosed())
                        return;

                    if(finalInsideSocket.isClosed() && OutsideSocket.isClosed() == false) {
                        OutsideSocket.close();
                        return;
                    }

                    if(finalInsideSocket.isClosed() == false && OutsideSocket.isClosed()) {
                        finalInsideSocket.close();
                        return;
                    }

                    len = finalFromClinetToPortMux.read(buffer);
                    if(len > 0)
                        finalFromPortMuxToServer.write(buffer, 0, len);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                try {
                    finalInsideSocket.close();
                    OutsideSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();

        // Message form Server
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int len;
                while (true) {

                    if(finalInsideSocket.isClosed() && OutsideSocket.isClosed())
                        return;

                    if(finalInsideSocket.isClosed() && OutsideSocket.isClosed() == false) {
                        OutsideSocket.close();
                        return;
                    }

                    if(finalInsideSocket.isClosed() == false && OutsideSocket.isClosed()) {
                        finalInsideSocket.close();
                        return;
                    }

                    len = finalFromServerToPortMux.read(buffer);
                    if(len > 0)
                        finalFromPortMuxToClient.write(buffer, 0, len);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                try {
                    finalInsideSocket.close();
                    OutsideSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
}
