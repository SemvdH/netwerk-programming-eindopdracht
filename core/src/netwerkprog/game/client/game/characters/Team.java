package netwerkprog.game.client.game.characters;

import netwerkprog.game.util.game.GameCharacter;
import netwerkprog.game.util.tree.BST;

public class Team {
    private BST<GameCharacter> members;

    public Team() {
        this.members = new BST<>();
    }

    /**
     * create a new team with the given characters
     * @param characters the characters
     */
    public Team(BST<GameCharacter> characters) {
        this.members = characters;
    }

    /**
     * add new member
     * @param gameCharacter the member to add
     */
    public void addMember(GameCharacter gameCharacter) {
        if (this.members.getSize() != 6)
            this.members.insert(gameCharacter);
    }

    /**
     * get members
     * @return the members
     */
    public BST<GameCharacter> getMembers() {
        return this.members;
    }

    /**
     * set members
     * @param members the members to set
     */
    public void setMembers(BST<GameCharacter> members) {
        this.members = members;
    }

    /**
     * add members
     * @param characters the members to add
     */
    public void addMember(GameCharacter... characters) {
        for (GameCharacter gameCharacter : characters) {
            this.members.insert(gameCharacter);
        }
    }

    /**
     * get the specific character
     * @param character the character to get
     * @return the character, null if it doesnt exist
     */
    public GameCharacter get(GameCharacter character) {
        for (GameCharacter cur : this.members) {
            if (cur.equals(character)) {
                return cur;
            }
        }
        return null;
    }

    /**
     * get the character with the specified username
     * @param username the username of the character
     * @return the character, null if it doesnt exist
     */
    public GameCharacter get(String username) {
        for (GameCharacter cur : this.members) {
            if (cur.getName().equals(username)) {
                return cur;
            }
        }
        return null;
    }

    /**
     * get the character at the specified position
     * @param position the position of the character
     * @return the character, null if it can't be found
     */
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

    /**
     * update all characters
     * @param deltaTime the time between the last frame
     */
    public void update(double deltaTime) {
        for (GameCharacter character : this.members) {
            character.update(deltaTime);
        }
    }

    /**
     * check if all players are dead
     * @return true if all players are dead
     */
    public boolean isDead() {
        int dead = 0;
        for (GameCharacter character : this.members) {
            if (character.isDead()) dead++;
        }
        return  dead >= this.members.getSize();
    }

    @Override
    public String toString() {
        return "Team{" +
                "members=" + members +
                '}';
    }
}
