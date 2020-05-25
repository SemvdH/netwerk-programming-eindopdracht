package netwerkprog.game.client.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

public class Tile extends Rectangle {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        if (!super.equals(o)) return false;
        Tile tile = (Tile) o;
        return getSymbol() == tile.getSymbol() &&
                tile.x == this.x &&
                tile.y == this.y &&
                this.width == tile.width &&
                this.height == tile.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTextureRegion(), getSymbol());
    }
}
