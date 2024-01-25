package engine.entities;

import org.lwjgl.util.vector.Vector3f;

public abstract class Camera {
 public abstract void move();
 public abstract Vector3f getPosition();
 public abstract float getPitch();
 public abstract float getYaw();
 public abstract float getRoll();
 public abstract void invertPitch();
}
