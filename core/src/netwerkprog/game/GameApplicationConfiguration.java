package netwerkprog.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * custom application config class  for the game
 */
public class GameApplicationConfiguration extends LwjglApplicationConfiguration {
    /**
     * makes a new configuration with the given parameters
     * @param width the width (in pixels)
     * @param height the height (in pixels)
     * @param fullscreen whether the app should run in fullscreen
     */
    public GameApplicationConfiguration(int width, int height, boolean fullscreen) {
        super();
        super.width = width;
        super.height = height;
        super.fullscreen = fullscreen;
    }

    /**
     * makes a new configuration with the given parameters.
     * No fullscreen
     * @param width the width (in pixels)
     * @param height the height (in pixels)
     */
    public GameApplicationConfiguration(int width, int height) {
        this(width,height,false);
    }

    /**
     * makes a new configuration with standard full hd width and height
     */
    public GameApplicationConfiguration() {
        this(1920,1080,false);
    }
}
