package netwerkprog.game.server;

import netwerkprog.game.server.controllers.DataController;
import netwerkprog.game.server.controllers.SessionController;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataCallback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient implements Runnable {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final String name;
    private final SessionController server;
    private final DataCallback callback;
    private boolean isConnected;

    public ServerClient(String name, Socket socket, SessionController server, DataController dataController) {
        this.name = name;
        this.server = server;
        this.callback = dataController;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.isConnected = true;
        } catch (IOException e) {
            this.isConnected = false;
            e.printStackTrace();
        }
    }

    public void writeData(Data data) {
        try {
            this.out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (this.isConnected) {
            try {
                callback.onDataReceived((Data) this.in.readObject());
            } catch (IOException e) {
                System.out.println("[SERVERCLIENT] caught exception - " + e.getMessage());
                System.out.println("[SERVERCLIENT] terminating failing connection...");
                this.isConnected = false;
                System.out.println("[SERVERCLIENT] done!");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return this.name;
    }
}
