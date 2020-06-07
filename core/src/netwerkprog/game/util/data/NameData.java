package netwerkprog.game.util.data;

public class NameData extends Data {
    private String name;
    public NameData(String name) {
        super("name");
        super.setPayload(this);
        this.name = name;

    }

    public String getName() {
        return name;
    }
}
