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

package engine.renderEngine;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import engine.models.RawModel;
import engine.textures.TextureData;

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
	 * Loads vertex positions and texture coordinates into a VAO and creates a RawModel.
	 *
	 * @param positions      The vertex positions.
	 * @param textureCoords  The texture coordinates.
	 * @return A RawModel representing the loaded data.
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * Loads vertex positions, texture coordinates, normals, and tangents into a VAO and creates a RawModel.
	 *
	 * @param positions   The vertex positions.
	 * @param textureCoords  The texture coordinates.
	 * @param normals       The normals.
	 * @param tangents      The tangents.
	 * @param indices       The vertex indices.
	 * @return A RawModel representing the loaded data.
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents,
			int[] indices) {
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		storeDataInAttributeList(3,3,tangents);

		unbindVAO();
		
		return new RawModel(vaoID,indices.length);
	}
	
	/**
	 * Creates an empty VBO with a specified amount of floats.
	 *
	 * @param floatCount   The amount of floats the VBO will be able to store.
	 * @return The VBO id.
	 */
	public int createEmptyVbo(int floatCount) {
		int vbo = GL15.glGenBuffers();
		
		vbos.add(vbo);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vbo;
	}
	
	/**
	 * Adds an instanced attribute for instanced rendering.
	 *
	 * @param vao  The VAO ID.
	 * @param vbo  The VBO ID.
	 * @param attribute  The attribute ID.
	 * @param dataSize  The data size in bytes.
	 * @param instancedDataLength  The length of the instanced data.
	 * @param offset  The data offset.
	 */
	public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize,
			int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4,
				offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads vertex positions into a VAO and creates a RawModel.
	 *
	 * @param positions   The vertex positions.
	 * @param dimensions  The number of dimensions for each vertex position.
	 */
	public void updateVbo(int vbo, float[] data, FloatBuffer buffer) {
//	    if (buffer.remaining() < data.length) {
//	        throw new IllegalArgumentException("Buffer capacity is too small for the provided data");
//	    }
	    
	    buffer.clear();
	    buffer.put(data);
	    buffer.flip();
	    
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
	    GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Loads vertex positions into a VAO and creates a RawModel.
	 *
	 * @param positions   The vertex positions.
	 * @param dimensions  The number of dimensions for each vertex position.
	 * @return A RawModel representing the loaded data.
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		
		this.storeDataInAttributeList(0, dimensions, positions);
		
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / dimensions);
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
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -2.4f);
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
	public void cleanUp() {
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
	
	/**
	 * Loads a cube map texture from an array of texture files.
	 *
	 * @param textureFiles An array of texture file names (without extensions) for each cube map face.
	 * @return The OpenGL texture ID for the cube map.
	 */
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for(int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
			
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
					data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
					data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		textures.add(texID);
		
		return texID;
	}
	
	/**
	 * Decodes a texture file in PNG format and retrieves its width, height, and pixel data.
	 *
	 * @param fileName The name of the texture file (including the path) to decode.
	 * @return A TextureData object containing the texture's pixel data, width, and height.
	 */
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		
		ByteBuffer buffer = null;
		
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work.");
			System.exit(-1);
		}
		
		return new TextureData(buffer, width, height);
	}
	
	/**
	 * Creates a new Vertex Array Object (VAO) and returns its OpenGL ID.
	 * This method also adds the ID to the list of VAOs for cleanup purposes.
	 *
	 * @return The OpenGL ID of the newly created VAO.
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		
		vaos.add(vaoID);
		
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	/**
	 * Stores vertex data in an attribute list within a Vertex Buffer Object (VBO).
	 *
	 * @param attributeNumber The attribute number to bind the data to.
	 * @param coordinateSize  The size of each coordinate (e.g., 2 for 2D, 3 for 3D).
	 * @param data            The vertex data to store.
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
	    int vboID = GL15.glGenBuffers();

	    if (vboID == 0) {
	        throw new IllegalStateException("Failed to generate VBO");
	    }

	    vbos.add(vboID);

	    try {
	        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

	        FloatBuffer buffer = storeDataInFloatBuffer(data);

	        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	        int error = GL11.glGetError();
	        if (error != GL11.GL_NO_ERROR) {
	            throw new IllegalStateException("OpenGL error: " + error);
	        }

	        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);

	        error = GL11.glGetError();
	        if (error != GL11.GL_NO_ERROR) {
	            throw new IllegalStateException("OpenGL error: " + error);
	        }

	        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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

//	    if (buffer.remaining() < data.length) {
//	        throw new IllegalArgumentException("Buffer capacity is too small for the provided data");
//	    }

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

//	    if (buffer.remaining() < data.length) {
//	        throw new IllegalArgumentException("Buffer capacity is too small for the provided data");
//	    }

	    buffer.put(data);
	    buffer.flip();

	    return buffer;
	}

}
