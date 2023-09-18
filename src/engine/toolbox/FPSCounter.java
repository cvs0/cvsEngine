package engine.toolbox;

/**
 * A utility class for calculating frames per second (FPS) in a game or application.
 */
public class FPSCounter {
    private long lastTime; // The timestamp of the last update
    private int frameCount; // The number of frames counted in the current second
    private int fps; // The current frames per second value

    /**
     * Constructs an FPSCounter and initializes it with initial values.
     */
    public FPSCounter() {
        lastTime = System.currentTimeMillis();
        frameCount = 0;
        fps = 0;
    }

    /**
     * Updates the FPSCounter. This method should be called once per frame.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        frameCount++;

        // Calculate the time elapsed since the last FPS update
        long elapsedTime = currentTime - lastTime;

        if (elapsedTime >= 1000) {
            // If one second has passed, update the FPS
            fps = frameCount;
            frameCount = 0;
            lastTime = currentTime;
        }
    }

    /**
     * Gets the current frames per second (FPS) value.
     * @return The current FPS value.
     */
    public int getFPS() {
        return fps;
    }
}
