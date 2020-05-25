package netwerkprog.game.client.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Tile extends Rectangle{
    private TextureRegion textureRegion;

    public Tile(TextureRegion textureRegion, int xPos, int yPos) {
        this.textureRegion = textureRegion;
        super.x = xPos;
        super.y = yPos;
        super.width = textureRegion.getRegionWidth();
        super.height = textureRegion.getRegionHeight();
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
