package netwerkprog.game.util.application;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * custom application config class  for the game
 */
public class GameApplicationConfiguration extends LwjglApplicationConfiguration {
    /**
     * Makes a new configuration with the given parameters
     * @param title the title of the window
     * @param width the width in pixels
     * @param height the height in pixels
     * @param fullscreen if the window should be fullscreen
     */
    public GameApplicationConfiguration(String title, int width, int height, boolean fullscreen) {
        super();
        super.width = width;
        super.height = height;
        super.title = title;
        super.fullscreen = fullscreen;
    }

    /**
     * Makes a new configuration with the given parameters.
     * fullscreen is off
     * @param title the window title
     * @param width the width in pixels
     * @param height the height in pixels
     */
    public GameApplicationConfiguration(String title, int width, int height) {
        this(title,width,height,false);
    }
}
