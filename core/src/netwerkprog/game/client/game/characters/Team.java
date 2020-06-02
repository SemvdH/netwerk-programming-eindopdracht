package netwerkprog.game.client.game.characters;

import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.tree.BST;

import java.util.ArrayList;
import java.util.Iterator;

public class Team {
    private BST<GameCharacter> members;

    public Team() {
        this.members = new BST<>();
    }

    public Team(BST<GameCharacter> characters) {
        this.members = characters;
    }

    public void addMember(GameCharacter gameCharacter) {
        if (this.members.getSize() != 6)
            this.members.insert(gameCharacter);
    }

    public BST<GameCharacter> getMembers() {
        return this.members;
    }

    public void setMembers(BST<GameCharacter> members) {
        this.members = members;
    }

    public void addMember(GameCharacter... characters) {
        for (GameCharacter gameCharacter : characters) {
            this.members.insert(gameCharacter);
        }
    }

    public GameCharacter get(GameCharacter character) {
        for (GameCharacter cur : this.members) {
            if (cur.equals(character)) {
                return cur;
            }
        }
        return null;
    }

    public GameCharacter get(int position) {
        if (position >= this.members.getSize()) {
            throw new IndexOutOfBoundsException("position out of range");
        }
        int i = 0;
        for (GameCharacter cur : this.members) {
            if (i == position) return cur;
            i++;
        }
        return null;
    }
}
