package netwerkprog.game.client.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import netwerkprog.game.util.game.Character;
import netwerkprog.game.util.game.Faction;

public class DevTest1 extends Character {
    public DevTest1() {
        super("DevTest1", Faction.HACKER, new Texture(Gdx.files.internal("core/assets/george.png")));
    }
}
