package engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class FreeRoamCamera extends Camera{

    private float distanceFromPlayer = 35;
    private float angleAroundPlayer = 0;
    
    private boolean mouseLocked = true;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;

    public FreeRoamCamera(Player player) {
        this.player = player;
        lockMouse();
    }
    
    private void lockMouse() {
        Mouse.setGrabbed(true);
    }

    public void move() {
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = 180 - (player.getRotY() + angleAroundPlayer);
        yaw %= 360;
        checkInput();
    }
    
    private void checkInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            toggleMouseLock();
        }
    }
    
    private void toggleMouseLock() {
        mouseLocked = !mouseLocked;
        Mouse.setGrabbed(mouseLocked);
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 4;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch + 4)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch + 4)));
    }

    private void calculatePitch() {
        float pitchChange = Mouse.getDY() * 0.2f;
        pitch = Math.max(0, Math.min(pitch - pitchChange, 90));
    }

    private void calculateAngleAroundPlayer() {
        float angleChange = Mouse.getDX() * 0.3f;
        angleAroundPlayer -= angleChange;
    }
}