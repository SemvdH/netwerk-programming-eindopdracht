package netwerkprog.game.client.game.connections;

import netwerkprog.game.util.application.Controller;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataCallback;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Controller {
    private final int port;
    private final String hostname;
    private boolean isConnected = true;
    private Socket socket;
    private Thread receiveThread;
    private DataCallback callback;
    private ObjectOutputStream outputStream;

    public Client(String hostname, DataCallback callback) {
        this.port = Data.port();
        this.hostname = hostname;
        this.callback = callback;
    }

    /**
     * Starts the client process.
     */
    @Override
    public void run() {
        this.connect();
        this.receiveThread.start();
    }

    /**
     * Connects the client to the server.
     */
    public void connect() {
        System.out.println("[CLIENT] connecting to server on port " + this.port);
        try {
            this.socket = new Socket(this.hostname, this.port);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.receiveThread = new Thread( () -> receive(in));

        } catch (IOException e) {
            System.out.println("[CLIENT] there was an error connecting : " + e.getMessage());
            StringBuilder sb = new StringBuilder("         Stacktrace : ");
            Arrays.stream(e.getStackTrace()).forEach(n -> sb.append("\t\t").append(n).append("\n"));
            System.out.println(sb.toString());
        }
    }

    /**
     * Sends a message to the server.
     * @param data The message to send.
     */
    public void send(Data data) {
        try {
            this.outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a message from the server.
     * @param in The inputStream
     */
    public void receive(ObjectInputStream in) {
        while (isConnected) {
            try {
                Object object = in.readObject();
                if (object instanceof Data) {
                    callback.onDataReceived((Data) object);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        this.isConnected = false;
        try {
            this.receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //send("Disconnect");

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
