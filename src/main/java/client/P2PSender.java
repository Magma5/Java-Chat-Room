package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class P2PSender implements Runnable {

    Socket socket;
    boolean id;
    String name;
    String to_whom;

    P2PSender(Socket socket, boolean id, String name, String to_whom) {
        this.socket = socket;
        this.id = id;
        this.name = name;
        this.to_whom = to_whom;
    }

    P2PSender(Socket socket, boolean id, String name) {
        this.socket = socket;
        this.id = id;
        this.name = name;
    }

    //false: receiver true:sender


    @SuppressWarnings("unused")
    @Override
    public void run() {
        // TODO Auto-generated method stub

        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter writer = new PrintWriter(socket.getOutputStream());
            final P2PFrame frame = new P2PFrame();
            frame.quit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.println("byebyebyebye");
                    frame.dispose();
                    writer.close();
                    try {
                        in.close();
                        socket.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            if (id) {
                writer.println(this.name);
                writer.flush();
                frame.setName(to_whom);
                frame.send.setEnabled(false);
                frame.setVisible(true);
                frame.show.append("Waiting for your peer to accept\n");
                if (in.readLine().equals("accept")) {
                    frame.send.setEnabled(true);
                    frame.show.append("You can start chatting!\n");
                } else {
                    frame.show.append("Your peer is busy!\n");
                }
            } else {
                this.to_whom = in.readLine();
                final P2PAccept accept = new P2PAccept(to_whom);
                frame.setName(to_whom);
                accept.setVisible(true);
                accept.accept.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writer.println("accept");
                        writer.flush();
                        accept.dispose();
                        frame.setVisible(true);
                    }
                });
                accept.refuse.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writer.println("refuse");
                        writer.flush();
                        frame.dispose();
                        accept.dispose();
                    }
                });
            }
            frame.send.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String content = frame.getInput();
                    writer.println(content);
                    writer.flush();
                    frame.addMessage(name + ":\n" + "  " + content + "\n");
                }
            });
            P2PReader reader = new P2PReader(frame, socket, to_whom);
            Thread t = new Thread(reader);
            t.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

}
