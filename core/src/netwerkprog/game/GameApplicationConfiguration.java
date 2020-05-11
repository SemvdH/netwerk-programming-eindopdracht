package netwerkprog.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

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

    /**
     * Makes a new configuration with the given title
     * the window will be 1920 x 1080 and fullscreen will be off
     * @param title the window title
     */
    public GameApplicationConfiguration(String title) {
        this(title, 1920,1080);
    }
}
