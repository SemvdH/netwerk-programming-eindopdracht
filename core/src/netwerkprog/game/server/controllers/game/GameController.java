package netwerkprog.game.server.controllers.game;

import netwerkprog.game.util.Controller;

import java.util.HashMap;

public class GameController extends Controller {
    private int score;
    private Timer timer;
    private final CharacterController characterController;
    private final ObjectController objectController;
    private HashMap<String, Objective> objectives;

    private final int id;

    public GameController(int id) {
        this.characterController = new CharacterController();
        this.objectController = new ObjectController();

        this.id = id;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("game thread " + id + "active");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
