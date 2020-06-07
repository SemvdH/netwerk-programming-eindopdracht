package netwerkprog.game.server;

import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.connection.ConnectionData;
import netwerkprog.game.util.data.connection.NameData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The sessionController manages any connections from new clients and assigns individual threads to said clients.
 */
public class SessionController implements DataCallback, Runnable {
    private ServerSocket serverSocket;
    private final ArrayList<ServerClient> clients;
    private boolean listening;

    public SessionController() {
        this.clients = new ArrayList<>();
        this.listening = true;
    }

    /**
     * Thread run method.
     */
    @Override
    public void run() {
        this.listening = true;
        while (listening) {
            listen();
        }
    }

    /**
     * Listens for any new clients.
     */
    public void listen() {
        try {
            this.serverSocket = new ServerSocket(Data.port());
            registerClient(serverSocket.accept());
            this.serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Registers a client to the server.
     * @param socket The socket used for the client connections.
     */
    public void registerClient(Socket socket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String username;
            boolean registering = true;
            while (registering) {
                outputStream.writeObject(new ConnectionData("Connect", "Please give a username"));
                Object object = inputStream.readObject();
                if (object instanceof Data) {
                    Data data = (Data) object;
                    if (data instanceof ConnectionData) {
                        ConnectionData connectionData = (ConnectionData) data.getPayload();
                        if (connectionData.getAction().equals("Connect")) {
                            outputStream.writeObject(new ConnectionData("Connect", "Confirm"));
                            registering = false;
                        }
                    }
                }
            }
            username = "player" + (this.clients.size() + 1);
            ServerClient serverClient = new ServerClient(username, this, this, inputStream, outputStream);
            Thread t = new Thread(serverClient);
            t.start();
            serverClient.writeData(new NameData(username));
            this.clients.add(serverClient);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Shuts down the sessionController.
     */
    public void shutdown() {
        this.listening = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the client from the server list.
     * @param client The Client to disconnect.
     */
    public void disconnect(ServerClient client) {
        this.clients.remove(client);
    }

    @Override
    public void onDataReceived(Data data, DataSource source) {
        for (ServerClient client: clients) {
            if (!client.getName().equals(source.getName())){
                client.writeData(data);
            }
        }
    }
}
