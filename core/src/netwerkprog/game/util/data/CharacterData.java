package netwerkprog.game.util.data;

import netwerkprog.game.util.game.GameCharacter;

public class CharacterData extends Data {
    private final String name;
    private final GameCharacter character;

    public CharacterData(String name, GameCharacter character) {
        super("Character");
        super.setPayload(this);
        this.name = name;
        this.character = character;
    }
}
