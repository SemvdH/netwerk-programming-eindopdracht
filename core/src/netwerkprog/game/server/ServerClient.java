package netwerkprog.game.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//TODO rewrite with GDX library https://www.gamefromscratch.com/post/2014/03/11/LibGDX-Tutorial-10-Basic-networking.aspx
public class ServerClient implements Runnable {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private String name;
    private Server server;
    private boolean isConnected = false;

    public ServerClient(String name, Socket socket, Server server) {
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
                if (received.equals("\\quit")) {
                    isConnected = false;
                    this.server.removeClient(this);
                } else {
                    this.server.sendToEveryoneExcept(this.name,"<" + this.name + "> : " + received);
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
