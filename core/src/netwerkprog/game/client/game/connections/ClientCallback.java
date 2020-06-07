package netwerkprog.game.client.game.connections;

import netwerkprog.game.util.data.Data;

public interface ClientCallback {
    void onDataReceived(Data data);
}
