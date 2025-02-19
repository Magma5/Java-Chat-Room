package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PListener implements Runnable {

    int port;
    String name;
    ServerSocket server;
    int try_number;

    P2PListener(int port, String name) {
        this.port = port;
        this.name = name;
        try_number = 1;
        while (true) {
            try {
                server = new ServerSocket(port - try_number);
                //System.out.println("myport" + port);
                //System.out.println("listening..." + (port-try_number));
                break;
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("NO available port!");
                break;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try_number++;
                System.out.println(try_number);
            }
        }
    }

    public int gettry_number() {
        return try_number;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {

            //System.out.println("listening..." + port);
            while (true) {
                Socket socket = null;
                socket = server.accept();
                P2PSender sender = new P2PSender(socket, false, name);
                Thread t = new Thread(sender);
                t.start();

            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
