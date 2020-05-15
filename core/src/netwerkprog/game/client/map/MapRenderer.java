package netwerkprog.game.client.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import netwerkprog.game.util.Renderable;

public class MapRenderer implements Renderable {
    private int tileWidth;
    private Map map;

    public MapRenderer(Map map, int tileWidth) {
        this.map = map;
        this.tileWidth = tileWidth;
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
        Batch batch = new SpriteBatch();

    }

    @Override
    public void update(double deltaTime) {

    }
}
