package netwerkprog.game.client.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
    private TextureRegion textureRegion;
    private int xPos;
    private int yPos;

    public Tile(TextureRegion textureRegion, int xPos, int yPos) {
        this.textureRegion = textureRegion;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public boolean contains(int x, int y) {

        return false;
    }
}
