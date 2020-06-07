package netwerkprog.game.util.data;

import netwerkprog.game.util.game.Faction;

public class TeamData extends Data {
    private final Faction faction;

    public TeamData(Faction faction) {
        super("Team");
        super.setPayload(this);
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }
}
