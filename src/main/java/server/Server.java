package server;


public class Server {
    public static void main(String[] args) {
        SocketServer s = new SocketServer(); //socket server listen the port and for new service thread
        Thread t = new Thread(s);
        t.start();

        try {
            t.join();
            // main threat holds
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }
}
