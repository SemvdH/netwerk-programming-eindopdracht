package netwerkprog.game.util.data.connection;

import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.game.Faction;

public class TeamData extends Data {
    private final Faction faction;
    private final String username;

    public TeamData(Faction faction, String username) {
        super("Team");
        super.setPayload(this);
        this.faction = faction;
        this.username = username;
    }

    public Faction getFaction() {
        return faction;
    }

    public String getUsername() {
        return username;
    }
}
