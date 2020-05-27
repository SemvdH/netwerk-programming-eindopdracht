package netwerkprog.game.client.game.characters;

import netwerkprog.game.client.game.map.GameTile;
import netwerkprog.game.util.game.Character;

public class SelectedCharacter {
    private Character character;
    private GameTile currentTile;

    public SelectedCharacter(Character character, GameTile tile) {
        this.character = character;
        this.currentTile  =tile;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public GameTile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(GameTile currentTile) {
        this.currentTile = currentTile;
    }
}
