package netwerkprog.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.audio.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import netwerkprog.game.client.Client;
import netwerkprog.game.server.Server;

public class MainGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float xPos = 500;
    float yPos = 500;
    float xUpdate;
    float yUpdate;
    private FrameRate frameRate;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        float ratio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        xUpdate = ratio;
        yUpdate = ratio;
        frameRate = new FrameRate();

        // play music
        Music music = Gdx.audio.newMusic(Gdx.files.getFileHandle("core/assets/music.mp3", Files.FileType.Internal));
        music.setVolume(.1f);
        music.play();
        music.setLooping(true);

        connectToServer();
    }

    private void connectToServer() {
        Client client = new Client("localhost", Server.PORT);
        client.start();
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(xPos/Gdx.graphics.getWidth(), 0, 0, 1);
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
        if (yPos > Gdx.graphics.getHeight() - img.getHeight() || yPos < 0) {
            yUpdate = -yUpdate;
        }

        if (xPos > Gdx.graphics.getWidth() - img.getWidth() || xPos < 0) {
            xUpdate = -xUpdate;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
