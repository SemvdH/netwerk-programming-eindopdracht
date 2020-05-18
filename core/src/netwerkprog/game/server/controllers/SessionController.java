package netwerkprog.game.server.controllers;

import netwerkprog.game.server.ServerClient;
import netwerkprog.game.server.data.Data;
import netwerkprog.game.server.data.DataParser;
import netwerkprog.game.util.Controller;
import netwerkprog.game.util.ServerData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionController extends Controller {
    private ServerSocket serverSocket;
    private DataParser parser;
    private ArrayList<ServerClient> clients = new ArrayList<>();
    private HashMap<String, Thread> clientThreads = new HashMap<>();
    private boolean listening;

    public SessionController() {
        this.listening = true;
    }

//    public void connect() {
//        try {
//            this.serverSocket = new ServerSocket(ServerData.port());
//            System.out.println("[SERVER] listening on port " + ServerData.port());
//            Socket socket = serverSocket.accept();
//
//            System.out.println("[SERVER] got new client on " + socket.getInetAddress().getHostAddress());
//            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//
//            outputStream.writeUTF("Enter username: ");
//            String username = inputStream.readUTF();
//
//            System.out.println("[SERVER] got username " + username);
//            ServerClient serverClient = new ServerClient(username, socket, this);
//
//            Thread t = new Thread(serverClient);
//            t.start();
//
//            clientThreads.put(username, t);
//            this.clients.add(serverClient);
//
//            sendMessage(username, "--- Welcome! ---\nPeople online : " + clients.size());
//
//            clients.forEach(yeet -> sendToEveryoneExcept(username, username + " joined the server! [" + socket.getInetAddress().getHostAddress() + "]"));
//
//
//            this.serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void sendToEveryone(String text) {
        for (ServerClient serverClient : clients) {
            serverClient.writeUTF(text);
        }
    }

    public void sendToEveryoneExcept(String name, String text) {
        for (ServerClient serverClient : clients) {
            if (!serverClient.getName().equals(name))
                serverClient.writeUTF(text);
        }
    }

    public void sendMessage(String name, String text) {
        for (ServerClient serverClient : clients) {
            if (serverClient.getName().equals(name)) {
                serverClient.writeUTF(text);
                break;
            }
        }
    }

    public void removeClient(ServerClient serverClient) {
        this.clients.remove(serverClient);
        try {
            this.clientThreads.get(serverClient.getName()).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.clientThreads.remove(serverClient.getName());
        this.sendToEveryone(serverClient.getName() + " left!");
    }

    @Override
    public void run() {
        while (listening) {
            listen();
        }
    }

    public void listen() {
        try {
            this.serverSocket = new ServerSocket(ServerData.port());
            System.out.println("[SERVER] listening on port " + ServerData.port());
            Socket socket = serverSocket.accept();
            this.serverSocket.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //will most likely be removed.
    public void parseData(String request) {
        Data data = this.parser.parse(request);
    }

    public void sendData(Data data) {

    }
}
