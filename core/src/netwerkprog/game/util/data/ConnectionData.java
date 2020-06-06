package netwerkprog.game.util.data;

import java.io.Serializable;

public class ConnectionData extends Data implements Serializable {
    private final String action;
    private final String message;

    public ConnectionData(String action, String message) {
        super("Connection");
        super.setPayload(this);
        this.action = action;
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
