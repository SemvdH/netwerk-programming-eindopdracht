package netwerkprog.game.server;

import netwerkprog.game.util.data.ConnectionData;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataCallback;
import netwerkprog.game.util.data.DataSource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerClient implements Runnable, DataSource {
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final String name;
    private final DataCallback callback;
    private boolean isConnected;

    public ServerClient(String name, ObjectInputStream in, ObjectOutputStream out, DataCallback callback) {
        this.name = name;
        this.callback = callback;
        this.in = in;
        this.out = out;
        this.isConnected = true;
    }

    public void writeData(Data data) {
        try {
            System.out.println("[SERVERCLIENT] writing data " + data);
            this.out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (this.isConnected) {
            try {
                Object object = this.in.readObject();
                System.out.println("[SERVERCLIENT] got object " + object);
                if (object instanceof Data) {
                    Data data = (Data) object;
                    if (data.getPayload() instanceof ConnectionData) {
                        ConnectionData connectionData = (ConnectionData) data.getPayload();
                        if (connectionData.getAction().equals("Disconnect")) {
                            this.isConnected = false;
                            //todo properly remove thread.
                        }
                    } else {
//                        callback.onDataReceived((Data) this.in.readObject());
                        System.out.println("[SERVERCLIENT] got data: " + data + ", sending callback");
                        callback.onDataReceived(data, this);
                    }
                }
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
