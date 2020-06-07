import netwerkprog.game.server.SessionController;
import org.junit.Test;

public class RestartSessionControllerTest {

    @Test
    public void restartSessionController() {
        SessionController sessionController;
        Thread sessionThread;

        sessionController = new SessionController();
        sessionThread = new Thread(sessionController);

        sessionThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sessionController.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sessionThread = new Thread(sessionController);
        sessionThread.start();
    }
}
