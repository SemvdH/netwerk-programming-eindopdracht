package netwerkprog.game.util.data;

public class ConnectionData extends Data {
    private final String action;
    private final String message;

    public ConnectionData(String action, String username) {
        super("Connection");
        super.setPayload(this);
        this.action = action;
        this.message = username;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
