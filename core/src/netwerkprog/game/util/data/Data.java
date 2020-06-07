package netwerkprog.game.util.data;

import java.io.Serializable;

public class Data implements Serializable {
    public static int port() {
        return 8000;
    }

    private final String objectType;
    private Data payload;

    public Data(String type) {
        this.objectType = type;
    }

    public void setPayload(Data payload) {
        this.payload = payload;
    }

    public Data getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Data{" +
                "objectType='" + objectType + '\'' +
                '}';
    }
}
