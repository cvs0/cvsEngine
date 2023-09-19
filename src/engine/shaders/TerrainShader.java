package engine.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Camera;
import engine.entities.Light;
import engine.toolbox.MathUtils;

/**
 * The TerrainShader class represents a shader program designed for rendering terrain.
 * It extends the ShaderProgram base class and provides methods to load specific uniform variables.
 */
public class TerrainShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4; 
	
	private static final String VERTEX_FILE = "src/engine/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/engine/shaders/terrainFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_plane;

	/**
     * Creates a new TerrainShader by loading the vertex and fragment shaders from files.
     */
	public TerrainShader() {
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
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_plane = super.getUniformLocation("plane");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];

		for(int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	/**
     * Connects texture units to corresponding uniform locations in the shader program.
     */
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	public void loadClipPlane(Vector4f clipPlane){
		super.loadVector(location_plane, clipPlane);
	}
	
	/**
     * Loads the sky color for lighting calculations.
     *
     * @param r The red component of the sky color.
     * @param g The green component of the sky color.
     * @param b The blue component of the sky color.
     */
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
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
     * Loads a transformation matrix for the terrain.
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
	public void loadLights(List<Light> lights) {
		for(int i = 0; i < MAX_LIGHTS; i++) {
			if(i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
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
