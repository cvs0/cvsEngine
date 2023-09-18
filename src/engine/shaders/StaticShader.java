package engine.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.entities.Light;
import engine.toolbox.MathUtils;

/**
 * The StaticShader class represents a shader program designed for rendering static objects.
 * It extends the ShaderProgram base class and provides methods to load specific uniform variables.
 */
public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/engine/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/engine/shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;

	/**
     * Creates a new StaticShader by loading the vertex and fragment shaders from files.
     */
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}
	
	/**
     * Loads the number of texture rows for a texture atlas.
     *
     * @param numberOfRows The number of texture rows.
     */
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	/**
     * Loads an offset for texture mapping.
     *
     * @param x The X-axis offset.
     * @param y The Y-axis offset.
     */
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
	}
	
	/**
     * Loads the sky color for lighting calculations.
     *
     * @param r The red component of the sky color.
     * @param g The green component of the sky color.
     * @param b The blue component of the sky color.
     */
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}
	
	/**
     * Loads a boolean value to control fake lighting.
     *
     * @param useFake True to enable fake lighting, false otherwise.
     */
	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	/**
     * Loads shine variables for specular lighting.
     *
     * @param damper      The shine damper value.
     * @param reflectivity The reflectivity value.
     */
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**
     * Loads a transformation matrix for the object.
     *
     * @param matrix The transformation matrix.
     */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
     * Loads light information for lighting calculations.
     *
     * @param light The light source to load.
     */
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	/**
     * Loads the view matrix for the camera.
     *
     * @param camera The camera to create the view matrix for.
     */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
     * Loads the projection matrix for the camera.
     *
     * @param projection The projection matrix to load.
     */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
}