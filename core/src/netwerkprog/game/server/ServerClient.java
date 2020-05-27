package netwerkprog.game.server;

import netwerkprog.game.server.controllers.SessionController;
import netwerkprog.game.util.data.ParserCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClient implements Runnable, ParserCallback {
    private DataInputStream in;
    private DataOutputStream out;
    private final String name;
    private final SessionController server;
    private final Parser parser;
    private boolean isConnected;

    public ServerClient(String name, Socket socket, SessionController server) {
        this.name = name;
        this.server = server;
        this.parser = new Parser(this);
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.isConnected = true;
        } catch (IOException e) {
            this.isConnected = false;
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
        while (this.isConnected) {
            try {
                String received = this.in.readUTF();
                this.parser.parse(received);

            } catch (IOException e) {
                System.out.println("[SERVERCLIENT] caught exception - " + e.getMessage());
                System.out.println("[SERVERCLIENT] terminating failing connection...");
                this.isConnected = false;
                System.out.println("[SERVERCLIENT] done!");
            }
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void onDataReceived(String data) {
        writeUTF(data);
    }
}
