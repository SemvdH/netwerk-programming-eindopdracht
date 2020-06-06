package netwerkprog.game.server.controllers;

import netwerkprog.game.server.Server;
import netwerkprog.game.server.ServerClient;
import netwerkprog.game.util.application.Controller;
import netwerkprog.game.util.data.ConnectionData;
import netwerkprog.game.util.data.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The sessionController manages any connections from new clients and assigns individual threads to said clients.
 */
public class SessionController extends Controller {
    private Server server;
    private ServerSocket serverSocket;
    private final ArrayList<ServerClient> clients = new ArrayList<>();
    private final HashMap<String, Thread> clientThreads = new HashMap<>();
    private boolean listening;

    public SessionController(Server server) {
        this.server = server;
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
            System.out.println("[SERVER] listening on port " + Data.port());
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
            System.out.println("[SERVER] got new client on " + socket.getInetAddress().getHostAddress());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            String username = "";
            boolean registering = true;

            while (registering) {
                outputStream.writeObject(new ConnectionData("Connect", "Please give a username"));
                Object object = inputStream.readObject();

                if (object instanceof Data) {
                    Data data = (Data) object;
                    if (data instanceof ConnectionData) {
                        ConnectionData connectionData = (ConnectionData) data.getPayload();
                        if (connectionData.getAction().equals("Connect")) {
                            username = connectionData.getMessage();
                            outputStream.writeObject(new ConnectionData("Connect", "Confirm"));
                            registering = false;
                        } else {
                            //todo error messaging.
                        }
                    } else {
                        //todo error messaging.
                    }
                } else {
                    //todo error messaging.
                }
            }

            System.out.println("[SERVER] got username " + username);
            ServerClient serverClient = new ServerClient(username, inputStream, outputStream, this, server.getDataController());

            Thread t = new Thread(serverClient);
            t.start();

            this.clientThreads.put(username,t);
            this.clients.add(serverClient);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sends a server message to all connected clients.
     * @param data message.
     */
    public void serverMessage(Data data) {
        for (ServerClient serverClient : clients) {
            serverClient.writeData(data);
        }
    }

    /**
     * Sends a message to a specific user.
     * @param name user.
     * @param data message.
     */
    public void personalMessage(String name, Data data) {
        for (ServerClient serverClient : clients) {
            if (serverClient.getName().equals(name)) {
                serverClient.writeData(data);
                break;
            }
        }
    }

    /**
     * Removes a client from the server.
     * @param serverClient The client to remove.
     */
    public void removeClient(ServerClient serverClient) {
        this.clients.remove(serverClient);
        try {
            this.clientThreads.get(serverClient.getName()).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.clientThreads.remove(serverClient.getName());
        //this.serverMessage(serverClient.getName() + " left!");
    }

    /**
     * Gets a list of all connected users.
     * @return Set of all connected users.
     */
    public Set<String> getUsernames() {
        return this.clientThreads.keySet();
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
        System.out.println("[SERVER] networking shutdown ");
    }
}
