package netwerkprog.game.client.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import netwerkprog.game.client.map.Tile;
import netwerkprog.game.util.graphics.Renderable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapRenderer implements Renderable {
    private final OrthographicCamera camera;
    private int tileWidth;
    private Map map;
    private SpriteBatch batch;
    private static String tilePath = "core/assets/map/scifitiles-sheet.png";
    private OrthographicCamera cam;
    private static int x = 0;
    private static int y = 0;


    public static TextureRegion FLOOR_TILE;
    public static TextureRegion WALL_TILE;
    public static TextureRegion PATH_TILE;

    private Tile[][] tiles;

    private boolean isStarted = false;


    public MapRenderer(Map map, int tileWidth, SpriteBatch batch, OrthographicCamera camera) {
        this.map = map;
        this.tileWidth = tileWidth;
        this.batch = batch;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera = camera;
        makeTiles();
    }

    private void makeTiles() {
        Texture texture = new Texture(Gdx.files.internal(tilePath));
        TextureRegion[][] tileTextures = TextureRegion.split(texture, 32, 32);

        FLOOR_TILE = tileTextures[1][6];
        WALL_TILE = tileTextures[0][4];
        PATH_TILE = tileTextures[4][6];

        this.tiles = new Tile[map.getHeight()][map.getWidth()];

        for (int row = map.getHeight(); row >= 0; row--) {
            y += 32;
            x = 0;
            for (int col = 0; col < map.getWidth(); col++) {
                if (map.get(row, col) == ' ') {
                    tiles[row][col] = new Tile(FLOOR_TILE,x,y, ' ');
                } else if (map.get(row, col) == '#') {
                    tiles[row][col] = new Tile(WALL_TILE,x,y, '#');
                } else if (map.get(row, col) == 'x') {
                    tiles[row][col] = new Tile(PATH_TILE,x,y, 'x');
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

        for (Tile[] tileRow : tiles) {
            for (int col = 0; col < tiles[0].length; col++) {
                Tile cur = tileRow[col];
                batch.draw(cur.getTextureRegion(), cur.x, cur.y);
            }
        }

        batch.end();
        x = 0;
        y = 0;
    }

    @Override
    public void update(double deltaTime) {

    }

    public void resize(int screenWidth, int screenHeight) {
        cam = new OrthographicCamera(screenWidth, screenHeight);
        cam.translate(screenWidth / 2, screenHeight / 2);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
