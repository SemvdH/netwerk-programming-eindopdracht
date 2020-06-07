package netwerkprog.game.util.data;

import netwerkprog.game.server.ServerClient;

public interface DataCallback {
    void onDataReceived(Data data, ServerClient client);
}
