package netwerkprog.game.util.data.connection;

import netwerkprog.game.util.data.Data;

public class ReadyData extends Data {
    private String username;
    public ReadyData(String username) {
        super("ready");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
