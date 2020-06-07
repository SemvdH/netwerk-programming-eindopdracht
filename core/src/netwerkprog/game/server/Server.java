package netwerkprog.game.server;

public class Server {
    private Thread sessionThread;

    public void start() {
        SessionController sessionController = new SessionController();
        this.sessionThread = new Thread(sessionController);
        run();
    }

    private void run() {
        this.sessionThread.start();
    }
}
