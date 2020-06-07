package netwerkprog.game.server;

import netwerkprog.game.util.data.Data;

public interface DataSource {
    void writeData(Data data);
    String getName();
}
