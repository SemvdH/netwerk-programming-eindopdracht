package netwerkprog.game.util.data;

import java.io.Serializable;

public class Data implements Serializable {
    public static int port() {
        return 8000;
    }

    private String objectType;
    private Data payload;

    public Data(String type) {
        this.objectType = type;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setPayload(Data payload) {
        this.payload = payload;
    }

    public String getType() {
        return objectType;
    }

    public Data getPayload() {
        return payload;
    }
}
