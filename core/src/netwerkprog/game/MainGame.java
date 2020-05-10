package netwerkprog.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float xPos = 500;
    float yPos = 500;
    float xUpdate;
    float yUpdate;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        float ratio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        xUpdate = ratio;
        yUpdate = ratio;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        updatePos();
        batch.draw(img, xPos, yPos);
        batch.end();
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
