package netwerkprog.game.server;

import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.connection.ConnectionData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerClient implements Runnable, DataSource {
    private final String name;
    private final SessionController server;
    private final DataCallback callback;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean isConnected;

    public ServerClient(String name, SessionController server, DataCallback callback, ObjectInputStream in, ObjectOutputStream out) {
        this.name = name;
        this.server = server;
        this.callback = callback;
        this.in = in;
        this.out = out;
        this.isConnected = true;
    }

    /**
     * Writes data to the connected client.
     * @param data The data object to write.
     */
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
                Object object = this.in.readObject();
                if (object instanceof Data) {
                    Data data = (Data) object;
                    if (data.getPayload() instanceof ConnectionData) {
                        ConnectionData connectionData = (ConnectionData) data.getPayload();
                        if (connectionData.getAction().equals("Disconnect")) {
                            this.isConnected = false;
                            this.server.disconnect(this);
                        }
                    } else {
                        callback.onDataReceived(data, this);
                    }
                }
            } catch (IOException e) {
                this.isConnected = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return this.name;
    }
}
