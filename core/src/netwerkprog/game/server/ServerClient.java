package netwerkprog.game.server;

import netwerkprog.game.server.controllers.SessionController;
import netwerkprog.game.server.data.Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClient implements Runnable {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private String name;
    private SessionController server;
    private boolean isConnected = false;

    public ServerClient(String name, Socket socket, SessionController server) {
        this.name = name;
        this.server = server;
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.isConnected = true;
        } catch (IOException e) {
            this.isConnected = false;
            e.printStackTrace();
        }
    }

    public void readUTF() {
        try {
            System.out.println("[SERVERCLIENT] got message: " + in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUTF(String text) {
        try {
            this.out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                String received = this.in.readUTF();
                Data data = server.parseData(received);
                if (data.toString().equals("\\quit")) {
                    this.isConnected = false;
                    this.server.removeClient(this);
                } else {
                    this.out.writeUTF(data.toString());
                }

            } catch (IOException e) {
                System.out.println("[SERVERCLIENT] caught exception - " + e.getMessage());
                System.out.println("[SERVERCLIENT] terminating failing connection...");
                isConnected = false;
                System.out.println("[SERVERCLIENT] done!");
            }
        }
    }

    public String getName() {
        return name;
    }
}
