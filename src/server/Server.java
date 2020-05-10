package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;
    private int port = 8000;
    private ArrayList<ServerClient> clients = new ArrayList<>();
    private HashMap<String, Thread> clientThreads = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.connect();
    }

    public void connect() {
        try {
            this.serverSocket = new ServerSocket(port);
            boolean running = true;
            while (running) {
                System.out.println("[SERVER] listening on port " + port);
                Socket socket = serverSocket.accept();

                System.out.println("[SERVER] got new client on " + socket.getInetAddress().getHostAddress());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                outputStream.writeUTF("Enter nickname: ");
                String nickname = inputStream.readUTF();

                System.out.println("[SERVER] got nickname " + nickname);
                ServerClient serverClient = new ServerClient(nickname, socket,this);

                Thread t = new Thread(serverClient);
                t.start();

                clientThreads.put(nickname,t);
                this.clients.add(serverClient);

                sendMessage(nickname, "--- Welcome! ---\nPeople online : " + clients.size());

                clients.forEach(yeet -> sendToEveryoneExcept(nickname,nickname + " joined the server! [" + socket.getInetAddress().getHostAddress() + "]"));

            }

            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
