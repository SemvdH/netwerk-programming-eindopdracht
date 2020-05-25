package netwerkprog.game.util.game;

public abstract class Ability {
    protected String name;

    public Ability(String name) {
        this.name = name;
    }


    public abstract String getCommand();


}
