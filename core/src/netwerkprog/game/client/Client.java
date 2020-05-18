package netwerkprog.game.client;

import netwerkprog.game.util.application.Controller;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client extends Controller {
    private int port;
    private String hostname;
    private DataParser parser;
    private boolean isConnected = true;



    public Client(String hostname) {
        this.port = Data.port();
        this.hostname = hostname;
        this.parser = new DataParser();
    }

    @Override
    public void run() {
        this.connect();
    }

    public void connect() {
        System.out.println("[CLIENT] connecting to server on port " + port);
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket(hostname,port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread readSocketThread = new Thread( () -> {
                receiveDataFromSocket(in);
            });

            readSocketThread.start();

            String input = "";

            while (!input.equals("\\quit")) {
                input = scanner.nextLine();
                out.writeUTF(input);
            }

            isConnected = false;

            socket.close();

            try {
                readSocketThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("[CLIENT] there was an error connecting : " + e.getMessage());
            StringBuilder sb = new StringBuilder("         Stacktrace : ");
            Arrays.stream(e.getStackTrace()).forEach(n -> sb.append("\t\t").append(n).append("\n"));
            System.out.println(sb.toString());
        }
    }

    private void receiveDataFromSocket(DataInputStream in) {
        String received = "";
        while (isConnected) {
            try {
                received = in.readUTF();

            } catch (IOException e) {
                System.out.println("exception caught - " + e.getMessage());;
            }
        }
    }


}
