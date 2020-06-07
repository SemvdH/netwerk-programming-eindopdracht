package netwerkprog.game.util.data.connection;

import netwerkprog.game.util.data.Data;

public class PlayerConnectData extends Data {
    private String username;
    public PlayerConnectData(String username) {
        super("player-connect");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
