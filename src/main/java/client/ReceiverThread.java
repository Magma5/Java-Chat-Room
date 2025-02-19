package client;

import java.io.BufferedReader;
import java.util.regex.Pattern;

public class ReceiverThread implements Runnable {

    BufferedReader in;
    MainFrame main;
    String name;

    public ReceiverThread(BufferedReader in, MainFrame main, String name) {
        super();
        this.in = in;
        this.main = main;
        this.name = name;
    }

    @Override
    public void run() {

        String regEx = "^[1-9]\\d*(\\s)users(\\s)online$";
        Pattern p = Pattern.compile(regEx);

        while (true) {
            try {
                String content = in.readLine();
                if (p.matcher(content).matches()) {
                    String sub = content.substring(0, content.indexOf(" "));
                    main.temp.resetTable();
                    for (int i = 0; i < Integer.parseInt(sub); i++) {
                        main.temp.addRow(in.readLine(), in.readLine(), in.readLine().substring(1));
                    }
                } else {
                    main.addMessage(content + "\n");
                }
                //while detect a input from the server, print it out

            } catch (Exception e) {
                System.out.println("connection expired!");
                return;
            }
        }
    }

}
