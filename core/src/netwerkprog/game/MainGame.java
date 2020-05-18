package netwerkprog.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import netwerkprog.game.client.Client;
import netwerkprog.game.util.FrameRate;

public class MainGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float xPos = 500;
    float yPos = 500;
    float xUpdate;
    float yUpdate;
    float screenWidth;
    float screenHeight;
    private FrameRate frameRate;
    private Thread client;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        float ratio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        xUpdate = ratio;
        yUpdate = ratio;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        frameRate = new FrameRate();

        // play music
        Music music = Gdx.audio.newMusic(Gdx.files.getFileHandle("core/assets/music.mp3", Files.FileType.Internal));
        music.setVolume(.1f);
        music.play();
        music.setLooping(true);
        connectToServer();
    }


    private void connectToServer() {
        client = new Thread(new Client("localhost"));
        try {
            client.start();
        } catch (Exception e) {
            System.out.println("There was an error connecting : " + e.getMessage());
        }
    }

    /**
     * render method that is called after the update method
     */
    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(xPos / Gdx.graphics.getWidth(), 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, xPos, yPos);
        batch.end();
        frameRate.render();
    }

    /**
     * update method that does all calculation before something is being drawn
     */
    public void update() {
        frameRate.update();
        updatePos();
    }

    private void updatePos() {
        yPos += yUpdate;
        xPos += xUpdate;

        if (yPos > screenHeight - img.getHeight() || yPos < 0) {
            yUpdate = -yUpdate;
        }

        if (xPos > screenWidth - img.getWidth() || xPos < 0) {
            xUpdate = -xUpdate;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        screenHeight = height;
        screenWidth = width;
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
