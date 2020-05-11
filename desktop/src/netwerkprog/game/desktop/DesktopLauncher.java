package netwerkprog.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import netwerkprog.game.MainGame;
import netwerkprog.game.util.GameApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		GameApplicationConfiguration config = new GameApplicationConfiguration("Netwerk Game",1200,800);
		new LwjglApplication(new MainGame(), config);
	}
}
