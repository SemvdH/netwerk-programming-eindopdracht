package netwerkprog.game.util.data.character;

import netwerkprog.game.client.game.map.GameTile;
import netwerkprog.game.util.data.Data;

import java.awt.*;

public class MoveData extends Data {
    private final String username;
    private final String characterName;
    private final Point pos;

    public MoveData(String username, String characterName, Point pos) {
        super("Move");
        super.setPayload(this);
        this.username = username;
        this.characterName = characterName;
        this.pos = pos;
    }

    public String getUsername() {
        return username;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Point getPos() {
        return pos;
    }
}
