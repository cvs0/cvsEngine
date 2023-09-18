package engine.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The ShaderProgram class serves as the base class for all shader programs used in the engine.
 * Subclasses should implement specific shaders by providing vertex and fragment shader file paths.
 */
public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
     * Creates a new ShaderProgram by loading and linking the vertex and fragment shaders.
     *
     * @param vertexFile   The path to the vertex shader source file.
     * @param fragmentFile The path to the fragment shader source file.
     */
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	/**
     * This method should be overridden by subclasses to get the locations of uniform variables.
     */
	protected abstract void getAllUniformLocations();
	
	/**
     * Retrieves the location of a uniform variable in the shader program.
     *
     * @param uniformName The name of the uniform variable.
     * @return The location of the uniform variable.
     */
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID,uniformName);
	}
	
	/**
     * Activates the shader program for use.
     */
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	/**
     * Deactivates the shader program.
     */
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	/**
     * Cleans up and releases resources associated with the shader program.
     */
	public void cleanUp(){
		stop();
		
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
     * This method should be overridden by subclasses to bind attribute locations.
     */
	protected abstract void bindAttributes();
	
	/**
     * Binds an attribute variable to a specific location.
     *
     * @param attribute     The attribute location.
     * @param variableName The name of the attribute variable.
     */
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
     * Loads a float value into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param value    The float value to load.
     */
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/**
     * Loads an integer value into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param value    The integer value to load.
     */
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
     * Loads a 3D vector into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param vector   The 3D vector to load.
     */
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
     * Loads a 2D vector into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param vector   The 2D vector to load.
     */
	protected void load2DVector(int location, Vector2f vector){
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	/**
     * Loads a boolean value into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param value    The boolean value to load.
     */
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		
		if(value){
			toLoad = 1;
		}
		
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
     * Loads a 4x4 matrix into a uniform variable.
     *
     * @param location The location of the uniform variable.
     * @param matrix   The 4x4 matrix to load.
     */
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
     * Loads and compiles a shader from a file.
     *
     * @param file The path to the shader source file.
     * @param type The type of shader (e.g., GL20.GL_VERTEX_SHADER).
     * @return The shader ID.
     */
	@SuppressWarnings("deprecation")
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		
		return shaderID;
	}

}