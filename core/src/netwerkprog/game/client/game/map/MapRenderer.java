package netwerkprog.game.client.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import netwerkprog.game.client.MainGame;
import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.graphics.Renderable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapRenderer implements Renderable {
    private final OrthographicCamera camera;
    private int tileWidth;
    private Map map;
    private SpriteBatch batch;
    private static String tilePath = "core/assets/map/scifitiles-sheet.png";
    private OrthographicCamera cam;
    private static int x = 0;
    private static int y = 0;
    private BitmapFont font;

    private ShapeRenderer shapeRenderer;

    private MainGame mainGame;
    private Texture square;
    private Texture square2;


    public static TextureRegion FLOOR_TILE;
    public static TextureRegion WALL_TILE;
    public static TextureRegion PATH_TILE;

    private GameTile[][] gameTiles;
    private List<GameTile> surroundedTilesOfCurrentCharacter;


    /**
     * makea a new mapRenderer object
     *
     * @param map       the map object
     * @param tileWidth the width of the tile
     * @param batch     the batch object so no new ones have to be made
     * @param camera    the camera object
     */
    public MapRenderer(Map map, int tileWidth, SpriteBatch batch, OrthographicCamera camera) {
        this.map = map;
        this.tileWidth = tileWidth;
        this.batch = batch;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera = camera;
        this.mainGame = MainGame.getInstance();
        font = new BitmapFont();
        makeTiles();
    }

    /**
     * loads all the images for the tiles and adds all the tiles to the array
     */
    private void makeTiles() {
        mainGame.assets.load("square.png", Texture.class);
        mainGame.assets.load("square2.png", Texture.class);
        mainGame.assets.load(tilePath, Texture.class);
        mainGame.assets.finishLoading();
        square = mainGame.assets.get("square.png");
        square2 = mainGame.assets.get("square2.png");

        Texture texture = mainGame.assets.get(tilePath);
        TextureRegion[][] tileTextures = TextureRegion.split(texture, 32, 32);

        FLOOR_TILE = tileTextures[1][6];
        WALL_TILE = tileTextures[0][4];
        PATH_TILE = tileTextures[4][6];

        this.gameTiles = new GameTile[map.getHeight()][map.getWidth()];

        for (int row = map.getHeight(); row >= 0; row--) {
            y += 32;
            x = 0;
            for (int col = 0; col < map.getWidth(); col++) {
                if (map.get(row, col) == ' ') {
                    gameTiles[row][col] = new GameTile(FLOOR_TILE, x, y, ' ');
                } else if (map.get(row, col) == '#') {
                    gameTiles[row][col] = new GameTile(WALL_TILE, x, y, '#');
                } else if (map.get(row, col) == 'x') {
                    gameTiles[row][col] = new GameTile(PATH_TILE, x, y, 'x');
                }
                x += 32;
            }
        }
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public Map getMap() {
        return map;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public void render() {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (GameTile[] gameTileRow : gameTiles) {
            for (int col = 0; col < gameTiles[0].length; col++) {
                GameTile cur = gameTileRow[col];
                batch.draw(cur.getTextureRegion(), cur.x, cur.y);

                if (cur.containsCharacter()) {
                    batch.draw(cur.getCharacter().getTextureRegion(), cur.x, cur.y);
                    if (cur.getCharacter().equals(mainGame.getSelectedCharacter())) {
                        batch.draw(square, cur.x, cur.y);

                    }
                }
            }
        }
        if (surroundedTilesOfCurrentCharacter != null && !surroundedTilesOfCurrentCharacter.isEmpty()) {
            for (GameTile gameTile : surroundedTilesOfCurrentCharacter) {
                batch.draw(square2, gameTile.x, gameTile.y);
            }
        }

        batch.end();
        x = 0;
        y = 0;
    }

    private static int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

    public List<GameTile> setSurroundedTilesOfCurrentCharacter(int x, int y) {
        List<GameTile> res = new ArrayList<GameTile>();
        for (int[] direction : directions) {
            int cx = x + direction[0];
            int cy = y + direction[1];
            if (cy >= 0 && cy < gameTiles.length)
                if (cx >= 0 && cx < gameTiles[cy].length)
                    if (gameTiles[cy][cx].getSymbol() != '#')
                        res.add(gameTiles[cy][cx]);
        }
        surroundedTilesOfCurrentCharacter = res;
        return res;
    }

    public GameTile getTile(GameCharacter character) {
        for (GameTile[] tiles : this.gameTiles) {
            for (GameTile tile : tiles) {
                if (tile.containsCharacter())
                    if (tile.getCharacter().equals(character)) {
                        return tile;
                    }
            }
        }
        return null;
    }


    public Point getPos(GameTile tile) {
        for (int row = 0; row < this.gameTiles.length; row++) {
            for (int col = 0; col < this.gameTiles[0].length; col++) {
                if (gameTiles[row][col].equals(tile)) {
                    return new Point(col, row);
                }
            }
        }
        return new Point(-1, -1);
    }

    @Override
    public void update(double deltaTime) {

    }

    public void resize(int screenWidth, int screenHeight) {
        cam = new OrthographicCamera(screenWidth, screenHeight);
        cam.translate(screenWidth / 2f, screenHeight / 2f);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    public GameTile[][] getGameTiles() {
        return gameTiles;
    }

    public List<GameTile> getSurroundedTilesOfCurrentCharacter() {
        return surroundedTilesOfCurrentCharacter;
    }

}
