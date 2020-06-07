package netwerkprog.game.server.controllers;

import netwerkprog.game.util.data.*;
import netwerkprog.game.util.game.GameCharacter;

import java.util.Arrays;
import java.util.HashSet;

public class DataController implements DataCallback {
    private final DataChangeCallback callback;
    private final HashSet<GameCharacter> gameCharacters;

    public DataController(DataChangeCallback callback) {
        this.callback = callback;
        gameCharacters = new HashSet<>();
    }

    public void addCharacter(GameCharacter gameCharacter) {
        this.gameCharacters.add(gameCharacter);
        callback.onDataChange(new CharacterData(gameCharacter.getName(), gameCharacter));
    }

    public void addAllCharacters(GameCharacter... gameCharacters) {
        this.gameCharacters.addAll(Arrays.asList(gameCharacters));
    }

    public void removeCharacter(String name) {
        this.gameCharacters.removeIf(character -> character.getName().equals(name));
        callback.onDataChange(new CharacterData(name, getCharacter(name))); //todo modify Character data to allow for removal.
    }

    public void removeCharacter(GameCharacter gameCharacter) {
        this.gameCharacters.remove(gameCharacter);
        callback.onDataChange(new CharacterData(gameCharacter.getName(), gameCharacter)); //todo modify Character data to allow for removal.
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
        source.writeData(data);
        switch (data.getType()) {
            case "Character" :
                if (data.getPayload() instanceof CharacterData) {

                }
                break;
        }
    }
}
