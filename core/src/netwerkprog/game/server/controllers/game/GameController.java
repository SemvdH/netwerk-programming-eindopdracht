package netwerkprog.game.server.controllers.game;

import netwerkprog.game.util.Controller;

public class GameController extends Controller {
    private final CharacterController characterController;
    private final ObjectController objectController;
    private final ScoreController scoreController;

    public GameController() {
        this.characterController = new CharacterController();
        this.objectController = new ObjectController();
        this.scoreController = new ScoreController();
    }

}
