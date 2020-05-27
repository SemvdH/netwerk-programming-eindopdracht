package netwerkprog.game.client.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

public class GameTile extends Rectangle {
    private TextureRegion textureRegion;
    private char symbol;

    public GameTile(TextureRegion textureRegion, int xPos, int yPos, char symbol) {
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
        if (!(o instanceof GameTile)) return false;
        if (!super.equals(o)) return false;
        GameTile gameTile = (GameTile) o;
        return getSymbol() == gameTile.getSymbol() &&
                gameTile.x == this.x &&
                gameTile.y == this.y &&
                this.width == gameTile.width &&
                this.height == gameTile.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTextureRegion(), getSymbol());
    }
}
