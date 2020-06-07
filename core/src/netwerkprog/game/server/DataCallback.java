package netwerkprog.game.server;

import netwerkprog.game.util.data.Data;

public interface DataCallback {
    void onDataReceived(Data data, DataSource source);
}
