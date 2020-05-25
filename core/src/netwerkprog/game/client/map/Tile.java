package netwerkprog.game.client.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Tile extends Rectangle{
    private TextureRegion textureRegion;
    private char symbol;

    public Tile(TextureRegion textureRegion, int xPos, int yPos, char symbol) {
        this.textureRegion = textureRegion;
        this.symbol = symbol;
        super.x = xPos;
        super.y = yPos;
        super.width = textureRegion.getRegionWidth();
        super.height = textureRegion.getRegionHeight();
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "symbol=" + symbol +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
