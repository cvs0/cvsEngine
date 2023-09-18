package engine.guis;

import org.lwjgl.util.vector.Matrix4f;

import engine.shaders.ShaderProgram;

/**
 * This class represents a shader program used for rendering GUI elements on the screen.
 */
public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/engine/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/engine/guis/guiFragmentShader.txt";

    private int location_transformationMatrix;

    /**
     * Constructs a GuiShader by loading vertex and fragment shader files.
     */
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Loads a transformation matrix into the shader.
     *
     * @param matrix The transformation matrix to load.
     */
    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
