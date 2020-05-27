package netwerkprog.game.client.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.util.game.Ability;
import netwerkprog.game.util.game.Character;
import netwerkprog.game.util.game.Faction;

public class Agent extends Character {
    public Agent(TextureRegion textureRegion, Ability... abilities) {
        super("Agent", Faction.MEGACORPORATION, textureRegion, abilities);
    }
}
