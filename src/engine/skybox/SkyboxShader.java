/**
 * CvsEngine
 *
 * @author cvs0
 * @version 1.0.0
 *
 * @license
 * MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package engine.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.renderEngine.DisplayManager;
import engine.shaders.ShaderProgram;
import engine.toolbox.MathUtils;


/**
 * The SkyboxShader class represents the shader program used for rendering the skybox.
 * It extends the ShaderProgram class and provides methods for setting various shader
 * uniform variables related to the skybox rendering.
 */
public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/engine/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/engine/skybox/skyboxFragmentShader.txt";

    private static final float ROTATE_SPEED = 1f;

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;

    private float rotation = 0;

    /**
     * Creates a new SkyboxShader instance by loading the vertex and fragment shader files.
     */
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Loads the projection matrix uniform variable in the shader.
     *
     * @param matrix The projection matrix to load.
     */
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    /**
     * Loads the view matrix uniform variable in the shader, including rotation for skybox movement.
     *
     * @param camera The camera representing the view.
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = MathUtils.createViewMatrix(camera);
        
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        
        super.loadMatrix(location_viewMatrix, matrix);
    }

    /**
     * Loads the fog color uniform variable in the shader.
     *
     * @param r The red component of the fog color.
     * @param g The green component of the fog color.
     * @param b The blue component of the fog color.
     */
    public void loadFogColour(float r, float g, float b) {
        super.loadVector(location_fogColour, new Vector3f(r, g, b));
    }

    /**
     * Connects texture units to the shader for cube maps.
     */
    public void connectTextureUnits() {
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }

    /**
     * Loads the blend factor uniform variable for transitioning between day and night textures.
     *
     * @param blend The blend factor.
     */
    public void loadBlendFactor(float blend) {
        super.loadFloat(location_blendFactor, blend);
    }
    
    /**
     * Gets all the uniform locations from the shaders and applies them to the local variables.
     */
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("blendFactor");
    }
    
    /**
     * Binds the attributes to the shader.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
