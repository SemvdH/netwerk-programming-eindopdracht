package netwerkprog.game.util.data.character;

import netwerkprog.game.client.game.map.GameTile;
import netwerkprog.game.util.data.Data;

public class MoveData extends Data {
    private final String username;
    private final String characterName;
    private final GameTile tile;

    public MoveData(String username, String characterName, GameTile tile) {
        super("Move");
        super.setPayload(this);
        this.username = username;
        this.characterName = characterName;
        this.tile = tile;
    }

    public String getUsername() {
        return username;
    }

    public String getCharacterName() {
        return characterName;
    }

    public GameTile getTile() {
        return tile;
    }
}
