package netwerkprog.game.server;

import netwerkprog.game.util.data.ParserCallback;

public class Parser {
    private final ParserCallback callback;

    public Parser(ParserCallback callback) {
        this.callback = callback;
    }

    public void parse(String request) {
        callback.onDataReceived(null);
    }
}
