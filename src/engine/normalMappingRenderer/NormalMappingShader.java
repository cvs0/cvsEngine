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

package engine.normalMappingRenderer;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Light;
import engine.shaders.ShaderProgram;

/**
 * Represents a shader program for rendering models with normal maps.
 */
public class NormalMappingShader extends ShaderProgram {	
	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/engine/normalMappingRenderer/normalMapVShader.txt";
	private static final String FRAGMENT_FILE = "src/engine/normalMappingRenderer/normalMapFShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPositionEyeSpace[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_modelTexture;
	private int location_normalMap;

	/**
     * Creates a new instance of the NormalMappingShader.
     */
    public NormalMappingShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_normalMap = super.getUniformLocation("normalMap");
		
		location_lightPositionEyeSpace = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	/**
     * Connects texture units to the shader program.
     */
    protected void connectTextureUnits() {
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_normalMap, 1);
	}
	
    /**
     * Loads a clipping plane vector into the shader.
     *
     * @param plane The vector representing the clipping plane.
     */
    protected void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
	
    /**
     * Loads the number of rows in the texture atlas into the shader.
     *
     * @param numberOfRows The number of rows in the texture atlas.
     */
    protected void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
    /**
     * Loads an offset vector into the shader.
     *
     * @param x The x-coordinate of the offset.
     * @param y The y-coordinate of the offset.
     */
    protected void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x,y));
	}
	
    /**
     * Loads the sky color vector into the shader.
     *
     * @param r The red component of the sky color.
     * @param g The green component of the sky color.
     * @param b The blue component of the sky color.
     */
    protected void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
    /**
     * Loads the shine variables (damper and reflectivity) into the shader.
     *
     * @param damper      The shine damper value.
     * @param reflectivity The reflectivity value.
     */
    protected void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
    /**
     * Loads a transformation matrix into the shader.
     *
     * @param matrix The transformation matrix to load.
     */
    protected void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
    /**
     * Loads lights into the shader, including their positions, colors, and attenuations.
     *
     * @param lights     A list of light sources in the scene.
     * @param viewMatrix The view matrix.
     */
    protected void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
    /**
     * Loads the view matrix into the shader.
     *
     * @param viewMatrix The view matrix to load.
     */
    protected void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
    /**
     * Loads the projection matrix into the shader.
     *
     * @param projection The projection matrix to load.
     */
    protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
    /**
     * Calculates and returns the eye-space position of a light source.
     *
     * @param light      The light source.
     * @param viewMatrix The view matrix.
     * @return The eye-space position of the light source.
     */
    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}
}
