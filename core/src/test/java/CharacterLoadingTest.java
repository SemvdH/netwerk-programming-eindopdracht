import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import netwerkprog.game.client.game.characters.DevTest1;
import netwerkprog.game.util.game.Character;
import org.junit.Test;

public class CharacterLoadingTest {

    @Test
    public void george() {
        String path = "core/assets/george.png";
        Texture texture = new Texture(Gdx.files.internal(path));
        Character george = new DevTest1();
    }
}
