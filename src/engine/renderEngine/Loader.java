package engine.renderEngine;

import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import engine.models.RawModel;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
     * Loads vertex, texture, and normal data into a VAO and creates a RawModel.
     *
     * @param positions     The vertex positions.
     * @param textureCoords The texture coordinates.
     * @param normals       The normals.
     * @param indices       The vertex indices.
     * @return A RawModel representing the loaded data.
     */
	public RawModel loadToVAO(float[] positions,float[] textureCoords, float[] normals, int[] indices){
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);

		unbindVAO();
		
		return new RawModel(vaoID,indices.length);
	}
	
	/**
     * Loads vertex positions into a VAO and creates a RawModel.
     *
     * @param positions The vertex positions.
     * @return A RawModel representing the loaded data.
     */
	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		
		this.storeDataInAttributeList(0, 2, positions);
		
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / 2);
	}
	
	/**
     * Loads a texture from a file and generates mipmaps with specified settings.
     *
     * @param fileName The name of the texture file (without extension) located in the "res" directory.
     * @return The OpenGL texture ID.
     */
	public int loadTexture(String fileName) {
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG",
					new FileInputStream("res/" + fileName + ".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	/**
     * Cleans up OpenGL resources, including VAOs, VBOs, and textures.
     */
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Unbinds the currently bound VAO, setting it to zero.
	 */
	private void unbindVAO() {
	    GL30.glBindVertexArray(0);
	}
	
	/**
	 * Binds the indices buffer with the provided indices data to the current VAO.
	 *
	 * @param indices The vertex indices to bind.
	 */
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Converts an array of integers into an IntBuffer.
	 *
	 * @param data The array of integers to convert.
	 * @return An IntBuffer containing the data from the array.
	 */
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Converts an array of floats into a FloatBuffer.
	 *
	 * @param data The array of floats to convert.
	 * @return A FloatBuffer containing the data from the array.
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
