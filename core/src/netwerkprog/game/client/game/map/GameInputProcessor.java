package netwerkprog.game.client.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import netwerkprog.game.client.MainGame;
import netwerkprog.game.client.game.GAMESTATE;
import netwerkprog.game.util.data.character.DamageData;
import netwerkprog.game.util.data.character.MoveData;
import netwerkprog.game.util.data.connection.TeamData;
import netwerkprog.game.util.game.Faction;
import netwerkprog.game.util.game.GameCharacter;

import java.util.ArrayList;

public class GameInputProcessor implements InputProcessor {
    private final OrthographicCamera camera;
    private MainGame mainGame;
    private ArrayList<Integer> keysList;
    private boolean isWPressed = false;
    private boolean isAPressed = false;
    private boolean isSPressed = false;
    private boolean isDPressed = false;

    long lastTimeCounted;

    private final float CAMERA_MOVE_SPEED = .3f;


    /**
     * makes a new game input processor
     *
     * @param camera the camera object to use
     */
    public GameInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
        this.mainGame = MainGame.getInstance();
        keysList = new ArrayList<>();
        lastTimeCounted = TimeUtils.millis();

        keysList.add(Input.Keys.W);
        keysList.add(Input.Keys.A);
        keysList.add(Input.Keys.S);
        keysList.add(Input.Keys.D);

        camera.zoom = MathUtils.clamp(camera.zoom, 1.5f, 1.8f);


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
//        System.out.println(camera.position.x + " , " + camera.position.y);
        if (mainGame.getGamestate() == GAMESTATE.PLAYING) {

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
        } else if (mainGame.getGamestate() == GAMESTATE.SELECTING_FACTION) {
            if (keycode == Input.Keys.NUM_1) {
                mainGame.send(new TeamData(Faction.MEGACORPORATION,mainGame.getUsername()));
            }
            if (keycode == Input.Keys.NUM_2) {
                mainGame.send(new TeamData(Faction.HACKER,mainGame.getUsername()));
            }

        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPoint);
        if (mainGame.getGamestate() == GAMESTATE.PLAYING) {
            for (int row = 0; row < mainGame.mapRenderer.getGameTiles().length; row++) {
                for (int col = 0; col < mainGame.mapRenderer.getGameTiles()[0].length; col++) {
                    GameTile gameTile = mainGame.mapRenderer.getGameTiles()[row][col];
                    if (gameTile.contains(touchPoint.x, touchPoint.y)) {
                        if (button == Input.Buttons.LEFT) {

                            // moving selected character
                            if (mainGame.isPlayersTurn()) {

                                if (mainGame.hasCharacterSelected() && !gameTile.containsCharacter()) {

                                    if (gameTile.getSymbol() != '#' && mainGame.mapRenderer.getSurroundedTilesOfCurrentCharacter().contains(gameTile)) {
                                        removeCharacterFromTile(mainGame.getSelectedCharacter());
                                        gameTile.visit(mainGame.getSelectedCharacter());
                                        mainGame.mapRenderer.setSurroundedTilesOfCurrentCharacter(col, row);
                                        mainGame.increaseTurn();
                                        mainGame.send(new MoveData(mainGame.getUsername(),mainGame.getSelectedCharacter().getName(),gameTile));
                                    }
                                }
//                            clicking on enemy
                                if (mainGame.hasCharacterSelected() && gameTile.containsCharacter() && gameTile.getCharacter().getFaction() != mainGame.getChosenFaction()) {
                                    if (mainGame.mapRenderer.getSurroundedTilesOfCurrentCharacter().contains(gameTile)) {
                                        gameTile.getCharacter().damage(10);
                                        mainGame.increaseTurn();
                                        mainGame.send(new DamageData(gameTile.getCharacter().getName()));
                                    }
                                }
                            }
                            // set selected character
                            if (!mainGame.hasCharacterSelected() && gameTile.containsCharacter()) {
                                if (gameTile.getCharacter().getFaction() == mainGame.getChosenFaction()) {
                                    mainGame.setSelectedCharacter(gameTile.getCharacter());
                                    mainGame.mapRenderer.setSurroundedTilesOfCurrentCharacter(col, row);
                                }
                            }
                            // switch character
                            if (gameTile.containsCharacter()
                                    && !mainGame.getSelectedCharacter().equals(gameTile.getCharacter())
                                    && gameTile.getCharacter().getFaction() == mainGame.getChosenFaction()) {
                                mainGame.setSelectedCharacter(gameTile.getCharacter());
                                mainGame.mapRenderer.setSurroundedTilesOfCurrentCharacter(col, row);
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void removeCharacterFromTile(GameCharacter character) {
        rowLoop:
        for (int row = 0; row < mainGame.mapRenderer.getGameTiles().length; row++) {
            for (int col = 0; col < mainGame.mapRenderer.getGameTiles()[0].length; col++) {
                GameTile gameTile = mainGame.mapRenderer.getGameTiles()[row][col];
                if (gameTile.containsCharacter() && gameTile.getCharacter().equals(character)) {
                    gameTile.removeCharacter();
                    break rowLoop;
                }
            }
        }
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
        if (camera.position.x > 5 * mainGame.getHorizontalTileAmount())
            if (isAPressed()) {
                camera.position.add(-CAMERA_MOVE_SPEED * delta, 0, 0);
            }
        if (camera.position.y < 30 * mainGame.getVerticalTileAmount())
            if (isWPressed()) {
                camera.position.add(0, CAMERA_MOVE_SPEED * delta, 0);
            }

        if (camera.position.y > 5 * mainGame.getVerticalTileAmount())
            if (isSPressed()) {
                camera.position.add(0, -CAMERA_MOVE_SPEED * delta, 0);
            }

        if (camera.position.x < mainGame.getScreenWidth() / 2 + 5 * mainGame.getHorizontalTileAmount())
            if (isDPressed()) {
                camera.position.add(CAMERA_MOVE_SPEED * delta, 0, 0);
            }
    }
}
