package netwerkprog.game.client;

import netwerkprog.game.util.data.ParserCallback;

public class Parser {
    private final ParserCallback callback;

    public Parser(ParserCallback callback) {
        this.callback = callback;
    }

    public void parse(String data) {
        callback.onDataReceived(data);
    }
}
