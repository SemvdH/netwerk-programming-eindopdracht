package netwerkprog.game.client.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.util.game.Ability;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;

public class Hacker extends GameCharacter {
    public Hacker(String name, TextureRegion textureRegion, Ability... abilities) {
        super(name, Faction.HACKER, textureRegion, abilities);
    }
}
