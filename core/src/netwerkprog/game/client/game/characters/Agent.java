package netwerkprog.game.client.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.util.game.Ability;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;

public class Agent extends GameCharacter {
    public Agent(TextureRegion textureRegion, Ability... abilities) {
        super("Agent", Faction.MEGACORPORATION, textureRegion, abilities);
    }
}
