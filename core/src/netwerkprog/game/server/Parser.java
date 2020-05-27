package netwerkprog.game.server;

import netwerkprog.game.client.game.characters.DevTest1;
import netwerkprog.game.client.game.characters.DevTest2;
import netwerkprog.game.client.game.characters.DevTest3;
import netwerkprog.game.server.controllers.DataController;
import netwerkprog.game.util.data.ParserCallback;

import java.util.Scanner;

public class Parser {
    private final ParserCallback callback;
    private Scanner scanner;

    private final DataController dataController;

    public Parser(ParserCallback callback) {
        this.callback = callback;
        this.dataController = new DataController();

        this.dataController.addAllCharacters(new DevTest1(), new DevTest2(), new DevTest3());
    }

    public void parse(String request) {
        String data = "";
        this.scanner = new Scanner(request);
        scanner.useDelimiter("~");
        String type = scanner.next();
        String name = scanner.next();

        if (type.equals("character")) {
            try {
                data = dataController.getCharacter(name).toString();
            } catch (IllegalArgumentException ex) {
                data = ex.getMessage();
            }
        }



        callback.onDataReceived(data);
    }
}
