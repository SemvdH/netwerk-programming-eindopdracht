package netwerkprog.game.util.data.connection;

import netwerkprog.game.util.data.Data;

public class NameData extends Data {
    private String name;
    public NameData(String name) {
        super("name");
        super.setPayload(this);
        this.name = name;

    }

    public String getName() {
        return name;
    }
}
