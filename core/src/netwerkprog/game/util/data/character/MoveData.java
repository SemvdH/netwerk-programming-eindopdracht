package netwerkprog.game.util.data.character;

import netwerkprog.game.util.data.Data;

public class MoveData extends Data {
    private final String name;
    private final int x;
    private final int y;

    public MoveData(String name, int x, int y) {
        super("Move");
        super.setPayload(this);
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
