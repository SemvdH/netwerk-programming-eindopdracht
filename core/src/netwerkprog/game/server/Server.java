package netwerkprog.game.server;

import netwerkprog.game.server.controllers.DataController;
import netwerkprog.game.server.controllers.SessionController;

import java.util.HashMap;

public class Server {
    private SessionController sessionController;
    private DataController dataController;
    private Thread sessionThread;
    private HashMap<String, Thread> gameThreads;

    public void start() {
        this.sessionController = new SessionController(this);
        this.dataController = new DataController();

        this.gameThreads = new HashMap<>();
        this.sessionThread = new Thread(sessionController);

        run();
    }

    private void run() {
        setTestGames();
        this.sessionThread.start();
    }

    private void setTestGames() {
//        for (int i = 0; i < 10; i++) {
//            gameThreads.put("game " + i, new Thread(new GameController(i)));
//        }
//
//        for (String game : gameThreads.keySet()) {
//            gameThreads.get(game).start();
//        }
    }

    public DataController getDataController() {
        return dataController;
    }

    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }
}
