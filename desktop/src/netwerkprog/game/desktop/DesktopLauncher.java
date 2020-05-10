package netwerkprog.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import netwerkprog.game.MainGame;
import temp.Animator;
import netwerkprog.game.GameApplicationConfiguration;
import temp.Networking;

public class DesktopLauncher {
	public static void main (String[] arg) {
		GameApplicationConfiguration config = new GameApplicationConfiguration(1200,800);
		new LwjglApplication(new MainGame(), config);
	}
}
