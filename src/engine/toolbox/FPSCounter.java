package engine.toolbox;

public class FPSCounter {
    private long lastTime;
    private int frameCount;
    private int fps;

    public FPSCounter() {
        lastTime = System.currentTimeMillis();
        frameCount = 0;
        fps = 0;
    }

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

    public int getFPS() {
        return fps;
    }
}
