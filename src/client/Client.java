package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private String hostname;
    private boolean isConnected = true;

    public static void main(String[] args) {
        Client client = new Client("localhost",8000);
        client.connect();

    }

    public Client(String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
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
            e.printStackTrace();
        }
    }

    private void receiveDataFromSocket(DataInputStream in) {
        String received = "";
        while (isConnected) {
            try {
                received = in.readUTF();
                System.out.println(received);
            } catch (IOException e) {
                System.out.println("exception caught - " + e.getMessage());;
            }
        }
    }
}
