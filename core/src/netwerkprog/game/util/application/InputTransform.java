package netwerkprog.game.util.application;

public class InputTransform {

    public static float getCursorToModelX(int screenwidth, int screenX, int cursorX) {
        return (((float) cursorX) * screenwidth) / ((float) screenX);
    }

    public static float getCursorToModelY(int screenHeight, int screenY, int cursorY) {
        return ((float) (screenY - cursorY)) * screenHeight / ((float) screenY);
    }
}