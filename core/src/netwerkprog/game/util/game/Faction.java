package netwerkprog.game.util.game;

public enum Faction {
    MEGACORPORATION("MegaCorp"),
    HACKER("Hacker"),
    AI("AI");

    String name;

    Faction(String name) {
        this.name = name;
    }
}
