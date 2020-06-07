package netwerkprog.game.client.game.connections;

import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.connection.ConnectionData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client implements Runnable {
    private final int port;
    private final String hostname;
    private final ClientCallback callback;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private Thread receiveThread;
    private boolean connecting;
    private boolean isConnected;

    public Client(String hostname, ClientCallback callback) {
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
        try {
            if (this.receiveThread != null){
                System.out.println("[CLIENT RUN] starting receive thread");
                this.receiveThread.start();
            }
            else System.out.println("[CLIENT] couldnt connect to server, the receiving thread was null!");
        } catch (Exception e) {
            System.out.println("[CLIENT] error connecting to server: " + e.getMessage() + ", cause: " + e.getCause().toString());
        }

    }

    /**
     * Connects the client to the server.
     */
    public void connect() {
        System.out.println("[CLIENT] connecting to server on port " + this.port);
        this.connecting = true;
        try {
            this.socket = new Socket(this.hostname, this.port);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            register(in);
            this.receiveThread = new Thread(() -> receive(in));
        } catch (IOException e) {
            this.connecting = false;
            System.out.println("[CLIENT] there was an error connecting : " + e.getMessage());
            StringBuilder sb = new StringBuilder("         Stacktrace : ");
            Arrays.stream(e.getStackTrace()).forEach(n -> sb.append("\t\t").append(n).append("\n"));
            System.out.println(sb.toString());
        }
        System.out.println("[CLIENT CONNECT] connected");
    }

    public void register(ObjectInputStream in) {
        while (connecting) {
            writeData(new ConnectionData("Connect", "Request"));
            try {
                Object object = in.readObject();
                if (object instanceof Data) {
                    Data data = (Data) object;
                    if (data.getPayload() instanceof ConnectionData) {
                        ConnectionData connectionData = (ConnectionData) data.getPayload();
                        if (connectionData.getAction().equals("Connect") && connectionData.getMessage().equals("Confirm")) {
                            this.connecting = false;
                            this.isConnected = true;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param data The message to send.
     */
    public void writeData(Data data) {
        try {
            System.out.println("[CLIENT] writing data " + data);
            this.outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a message from the server.
     *
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
        writeData(new ConnectionData("Disconnect", "Request"));
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
