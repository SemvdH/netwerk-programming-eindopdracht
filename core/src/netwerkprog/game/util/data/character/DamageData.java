package netwerkprog.game.util.data.character;

import netwerkprog.game.util.data.Data;

public class DamageData extends Data {
    private final String name;

    public DamageData(String name) {
        super("Damage");
        super.setPayload(this);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
