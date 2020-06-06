import netwerkprog.game.server.controllers.SessionController;
import org.junit.Test;

public class RestartSessionControllerTest {

    @Test
    public void restartSessionController() {
        SessionController sessionController;
        Thread sessionThread;

        sessionController = new SessionController(null);
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
        System.out.println(sessionThread.getState());
        sessionThread = new Thread(sessionController);
        sessionThread.start();
    }
}
