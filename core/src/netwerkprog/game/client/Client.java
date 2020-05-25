package netwerkprog.game.client;

import netwerkprog.game.util.application.Controller;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client extends Controller {
    private final int port;
    private final String hostname;
    private final DataParser parser;
    private boolean isConnected = true;
    private Socket socket;
    private Thread receiveThread;
    private DataOutputStream outputStream;

    public Client(String hostname) {
        this.port = Data.port();
        this.hostname = hostname;
        this.parser = new DataParser();
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
            DataInputStream in = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

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
     * @param message The message to send.
     */
    public void send(String message) {
        try {
            this.outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a message from the server.
     * @param in The inputStream
     */
    public void receive(DataInputStream in) {
        Data data = null;
        while (isConnected) {
            try {
                data = this.parser.parse(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (data != null) {
                send(this.parser.parse(data));
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

        send("Disconnect");

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
