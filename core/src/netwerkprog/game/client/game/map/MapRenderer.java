package netwerkprog.game.client.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import netwerkprog.game.client.MainGame;
import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.graphics.Renderable;

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

    private MainGame mainGame;
    private Texture square;
    private Texture square2;
    private Texture hitMarker;
    private Texture tombStone;

    public static TextureRegion FLOOR_TILE;
    public static TextureRegion WALL_TILE;
    public static TextureRegion PATH_TILE;

    private GameTile[][] gameTiles;
    private List<GameTile> surroundedTilesOfCurrentCharacter;

    public static int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

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
        makeTiles();
    }

    /**
     * loads all the images for the tiles and adds all the tiles to the array
     */
    private void makeTiles() {
        mainGame.assets.load("square.png", Texture.class);
        mainGame.assets.load("square2.png", Texture.class);
        mainGame.assets.load(tilePath, Texture.class);
        mainGame.assets.load("hit.png", Texture.class);
        mainGame.assets.load("dead.png", Texture.class);
        mainGame.assets.finishLoading();
        square = mainGame.assets.get("square.png");
        square2 = mainGame.assets.get("square2.png");
        hitMarker = mainGame.assets.get("hit.png");
        tombStone = mainGame.assets.get("dead.png");

        // load the texture file
        Texture texture = mainGame.assets.get(tilePath);
        TextureRegion[][] tileTextures = TextureRegion.split(texture, 32, 32);

        FLOOR_TILE = tileTextures[1][6];
        WALL_TILE = tileTextures[0][4];
        PATH_TILE = tileTextures[4][6];

        // init the array
        this.gameTiles = new GameTile[map.getHeight()][map.getWidth()];

        // for each game tile, put the corresponding tile image in the array
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

    /**
     * method that renders the whole map
     */
    @Override
    public void render() {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (GameTile[] gameTileRow : gameTiles) {
            for (int col = 0; col < gameTiles[0].length; col++) {
                GameTile cur = gameTileRow[col];
                //draw each tile
                batch.draw(cur.getTextureRegion(), cur.x, cur.y);

                if (cur.containsCharacter()) {
                    //draw each character on a tile
                    GameCharacter character = cur.getCharacter();

                    if (!character.isDead()) {
                        batch.draw(character.getTextureRegion(), cur.x, cur.y);

                        //if he's showing an animation, draw the hitmarker.
                        if (character.isShowingAnimation())
                            batch.draw(hitMarker, cur.x, cur.y);

                        // if hes selected, draw the green square
                        if (cur.getCharacter().equals(mainGame.getSelectedCharacter()))
                            batch.draw(square, cur.x, cur.y);


                    } else {
                        // if hes dead, draw a tombstone
                        batch.draw(tombStone, cur.x, cur.y);
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



    /**
     * gets the 8 surrounding tiles of the character, to see where he can move.
     * @param x the x position of the character
     * @param y the y position of the character
     */
    public void setSurroundedTilesOfCurrentCharacter(int x, int y) {
        List<GameTile> res = new ArrayList<>();
        for (int[] direction : directions) {
            int cx = x + direction[0];
            int cy = y + direction[1];
            if (cy >= 0 && cy < gameTiles.length)
                if (cx >= 0 && cx < gameTiles[cy].length)
                    if (gameTiles[cy][cx].getSymbol() != '#')
                        res.add(gameTiles[cy][cx]);
        }
        surroundedTilesOfCurrentCharacter = res;
    }

    /**
     * gets the game tile of the character.
     * @param character the character
     * @return the game tile of the character, null if it is not found
     */
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

    public void dispose() {
        tombStone.dispose();
        square.dispose();
        square2.dispose();
        hitMarker.dispose();
    }


    /**
     * gets the position of the specified tile.
     * @param tile the tile to get the position of
     * @return the position of the tile, a point of -1,-1 if the tile is not found
     */
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

    /**
     * gets the game tile at the specified position.
     * @param pos the position of the tile
     * @return the game tile on the position, <code>null</code> if it is not found
     */
    public GameTile getGameTile(Point pos) {
        for (int row = 0; row < this.gameTiles.length; row++) {
            for (int col = 0; col < this.gameTiles[0].length; col++) {
                if (row == pos.y && col == pos.x) {
                    return this.gameTiles[row][col];
                }
            }
        }
        return null;
    }

    /**
     * remove character from tile
     * @param character the character to remove
     */
    public void removeCharacterFromTile(GameCharacter character) {
        rowLoop:
        for (int row = 0; row < getGameTiles().length; row++) {
            for (int col = 0; col < getGameTiles()[0].length; col++) {
                GameTile gameTile = getGameTiles()[row][col];
                if (gameTile.containsCharacter() && gameTile.getCharacter().equals(character)) {
                    gameTile.removeCharacter();
                    break rowLoop;
                }
            }
        }
    }

    /**
     * resize the screen
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public void resize(int screenWidth, int screenHeight) {
        cam = new OrthographicCamera(screenWidth, screenHeight);
        cam.translate(screenWidth / 2f, screenHeight / 2f);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    /**
     * return the game tiles
     * @return the game tiles.
     */
    public GameTile[][] getGameTiles() {
        return gameTiles;
    }

    /**
     * get the surrounding tiles of character
     * @return the surrounding tiles of character
     */
    public List<GameTile> getSurroundedTilesOfCurrentCharacter() {
        return surroundedTilesOfCurrentCharacter;
    }

}
