package netwerkprog.game.server.controllers;

import netwerkprog.game.server.ServerClient;
import netwerkprog.game.server.data.Data;
import netwerkprog.game.server.data.DataParser;
import netwerkprog.game.util.Controller;
import netwerkprog.game.util.ServerData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The sessionController manages any connections from new clients and assigns individual threads to said clients.
 */
public class SessionController extends Controller {
    private ServerSocket serverSocket;
    private final DataParser parser;
    private final ArrayList<ServerClient> clients = new ArrayList<>();
    private final HashMap<String, Thread> clientThreads = new HashMap<>();
    private boolean listening;

    public SessionController() {
        this.parser = new DataParser();
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
            this.serverSocket = new ServerSocket(ServerData.port());
            System.out.println("[SERVER] listening on port " + ServerData.port());
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
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            outputStream.writeUTF("Enter username: ");
            String username = inputStream.readUTF();

            System.out.println("[SERVER] got username " + username);
            ServerClient serverClient = new ServerClient(username, socket, this);

            Thread t = new Thread(serverClient);
            t.start();

            this.clientThreads.put(username,t);
            this.clients.add(serverClient);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Parses the request to the requested Data.
     * @param request The request to parse.
     * @return Parsed Data.
     */
    public Data parseData(String request) {
        return this.parser.parse(request);
    }

    /**
     * Sends a server message to all connected clients.
     * @param text message.
     */
    public void serverMessage(String text) {
        for (ServerClient serverClient : clients) {
            serverClient.writeUTF(text);
        }
    }

    /**
     * Sends a message to a specific user.
     * @param name user.
     * @param text message.
     */
    public void personalMessage(String name, String text) {
        for (ServerClient serverClient : clients) {
            if (serverClient.getName().equals(name)) {
                serverClient.writeUTF(text);
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
        this.serverMessage(serverClient.getName() + " left!");
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
