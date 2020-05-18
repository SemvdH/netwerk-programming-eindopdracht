package netwerkprog.game.client.map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import netwerkprog.game.MainGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameInputProcessor implements InputProcessor {
    private final OrthographicCamera camera;
    private MainGame game;
    private ArrayList<Integer> keysList;
    private boolean isWPressed = false;
    private boolean isAPressed = false;
    private boolean isSPressed = false;
    private boolean isDPressed = false;

    long lastTimeCounted;

    private final float CAMERA_MOVE_SPEED = .3f;


    public GameInputProcessor(OrthographicCamera camera, MainGame game) {
        this.camera = camera;
        this.game = game;
        keysList = new ArrayList<>();
        lastTimeCounted = TimeUtils.millis();

        keysList.add(Input.Keys.W);
        keysList.add(Input.Keys.A);
        keysList.add(Input.Keys.S);
        keysList.add(Input.Keys.D);

        camera.zoom = MathUtils.clamp(camera.zoom, 1.5f, 1.8f);
//
//        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
//        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
//
//        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, game.getScreenWidth() - effectiveViewportWidth / 2f);
//        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, game.getScreenHeight() - effectiveViewportHeight / 2f);

    }

    public boolean isWPressed() {
        return isWPressed;
    }

    public boolean isAPressed() {
        return isAPressed;
    }

    public boolean isSPressed() {
        return isSPressed;
    }

    public boolean isDPressed() {
        return isDPressed;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keysList.contains(keycode)) {
            if (keycode == keysList.get(0)) {
                this.isWPressed = true;
                return true;
            }
            if (keycode == keysList.get(1)) {
                this.isAPressed = true;
                return true;
            }
            if (keycode == keysList.get(2)) {
                this.isSPressed = true;
                return true;
            }
            if (keycode == keysList.get(3)) {
                this.isDPressed = true;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println(camera.position.x + " , " + camera.position.y);
        if (keysList.contains(keycode)) {
            if (keycode == keysList.get(0)) {
                this.isWPressed = false;
                return true;
            }
            if (keycode == keysList.get(1)) {
                this.isAPressed = false;
                return true;
            }
            if (keycode == keysList.get(2)) {
                this.isSPressed = false;
                return true;
            }
            if (keycode == keysList.get(3)) {
                this.isDPressed = false;
                return true;
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        float nextZoom = camera.zoom + amount / 5f;
        if (nextZoom >= 0.5 && nextZoom <= 2.5) {
            camera.zoom += amount / 2f;
            return true;
        }
        return false;
    }

    public void update() {
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();
        if (camera.position.x > 5 * game.getHorizontalTileAmount())
            if (isAPressed()) {
                camera.position.add(-CAMERA_MOVE_SPEED * delta, 0, 0);
            }
        if (camera.position.y < 30 * game.getVerticalTileAmount())
            if (isWPressed()) {
                camera.position.add(0, CAMERA_MOVE_SPEED * delta, 0);
            }

        if (camera.position.y > 5 * game.getVerticalTileAmount())
            if (isSPressed()) {
                camera.position.add(0, -CAMERA_MOVE_SPEED * delta, 0);
            }

        if (camera.position.x < game.getScreenWidth() / 2 + 5 * game.getHorizontalTileAmount())
            if (isDPressed()) {
                camera.position.add(CAMERA_MOVE_SPEED * delta, 0, 0);
            }
    }
}
