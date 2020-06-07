package netwerkprog.game.util.data;

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
