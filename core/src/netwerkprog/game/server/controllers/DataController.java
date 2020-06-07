package netwerkprog.game.server.controllers;

import netwerkprog.game.util.data.CharacterData;
import netwerkprog.game.util.data.Data;
import netwerkprog.game.util.data.DataCallback;
import netwerkprog.game.util.data.DataSource;
import netwerkprog.game.util.game.GameCharacter;

import java.util.Arrays;
import java.util.HashSet;

public class DataController implements DataCallback {
    private final HashSet<GameCharacter> gameCharacters;

    public DataController() {
        gameCharacters = new HashSet<>();
    }

    public void addCharacter(GameCharacter gameCharacter) {
        this.gameCharacters.add(gameCharacter);
    }

    public void addAllCharacters(GameCharacter... gameCharacters) {
        this.gameCharacters.addAll(Arrays.asList(gameCharacters));
    }

    public void removeCharacter(String name) {
        this.gameCharacters.removeIf(character -> character.getName().equals(name));
    }

    public void removeCharacter(GameCharacter character) {
        this.gameCharacters.remove(character);
    }

    public void clearCharacters() {
        this.gameCharacters.clear();
    }

    public HashSet<GameCharacter> getGameCharacters() {
        return gameCharacters;
    }

    public GameCharacter getCharacter(String name) throws IllegalArgumentException {
        for (GameCharacter character : gameCharacters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }
        throw new IllegalArgumentException("The character does not exist.");
    }

    @Override
    public void onDataReceived(Data data, DataSource source) {
        System.out.println("[DATACONTROLLER] got data: " + data);
        switch (data.getType()) {
            case "Character" :
                if (data.getPayload() instanceof CharacterData) {

                }
                break;
        }
    }
}
