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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.models.RawModel;

/**
 * A utility class for loading 3D models in OBJ file format.
 */
public class OBJLoader {
	
	/**
     * Loads an OBJ model from the specified file and converts it into a RawModel that can be rendered.
     *
     * @param fileName The name of the OBJ file to load.
     * @param loader   The loader used to store the model data in a VAO.
     * @return A RawModel representing the loaded OBJ model.
     */
	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load OBJ: " + fileName + ".obj");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		String line;
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try {
			
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					
					vertices.add(vertex);
					
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					
					textures.add(texture);
					
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					
					normals.add(normal);
					
				} else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					
					break;
				}
			}
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = reader.readLine();
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for(Vector3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
	}
	
	/**
	 * Processes a single vertex line from the OBJ file and adds it to the model's data.
	 *
	 * @param vertexData   The vertex data from a line in the OBJ file.
	 * @param indices      The list of vertex indices.
	 * @param textures     The list of texture coordinates.
	 * @param normals      The list of normal vectors.
	 * @param textureArray An array to store texture coordinates.
	 * @param normalsArray An array to store normal vectors.
	 */
	private static void processVertex(String[] vertexData, List<Integer> indices,
	        List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
	        float[] normalsArray) {
	    if (vertexData.length < 3) {
	        handleInvalidVertexData(vertexData);
	        return;
	    }

	    try {
	        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
	        processIndex(currentVertexPointer, indices);

	        int texIndex = Integer.parseInt(vertexData[1]) - 1;
	        processTexture(texIndex, textures, textureArray, currentVertexPointer);

	        int normIndex = Integer.parseInt(vertexData[2]) - 1;
	        processNormal(normIndex, normals, normalsArray, currentVertexPointer);
	    } catch (NumberFormatException e) {
	        handleNumberFormatError(vertexData, e);
	    }
	}

	/**
	 * Handles the case when invalid vertex data is encountered in the OBJ file.
	 *
	 * @param vertexData The vertex data from a line in the OBJ file.
	 */
	private static void handleInvalidVertexData(String[] vertexData) {
	    /**
	     * Prints an error message indicating that invalid vertex data is provided.
	     *
	     * @param vertexData The vertex data from a line in the OBJ file.
	     */
	    System.err.println("Invalid vertex data provided: " + Arrays.toString(vertexData));
	}

	/**
	 * Processes the index of a vertex and adds it to the list of vertex indices.
	 *
	 * @param currentVertexPointer The current vertex index.
	 * @param indices              The list of vertex indices.
	 */
	private static void processIndex(int currentVertexPointer, List<Integer> indices) {
	    /**
	     * Adds the current vertex index to the list of vertex indices.
	     *
	     * @param currentVertexPointer The current vertex index.
	     * @param indices              The list of vertex indices.
	     */
	    indices.add(currentVertexPointer);
	}

	/**
	 * Processes the texture index, updates the texture array, and handles invalid texture indices.
	 *
	 * @param texIndex             The texture index from the OBJ file.
	 * @param textures             The list of texture coordinates.
	 * @param textureArray         An array to store texture coordinates.
	 * @param currentVertexPointer The current vertex index.
	 */
	private static void processTexture(int texIndex, List<Vector2f> textures, float[] textureArray, int currentVertexPointer) {
	    /**
	     * Processes the texture index, updates the texture array, and handles invalid texture indices.
	     *
	     * @param texIndex             The texture index from the OBJ file.
	     * @param textures             The list of texture coordinates.
	     * @param textureArray         An array to store texture coordinates.
	     * @param currentVertexPointer The current vertex index.
	     */
	    if (texIndex >= 0 && texIndex < textures.size()) {
	        Vector2f currentTex = textures.get(texIndex);
	        textureArray[currentVertexPointer * 2] = currentTex.x;
	        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
	    } else {
	        handleInvalidTextureIndex(texIndex);
	    }
	}

	/**
	 * Handles the case when an invalid texture index is encountered.
	 *
	 * @param texIndex The invalid texture index.
	 */
	private static void handleInvalidTextureIndex(int texIndex) {
	    /**
	     * Prints an error message indicating that the texture index is invalid.
	     *
	     * @param texIndex The invalid texture index.
	     */
	    System.err.println("Invalid texture index: " + texIndex);
	}

	/**
	 * Processes the normal index, updates the normals array, and handles invalid normal indices.
	 *
	 * @param normIndex             The normal index from the OBJ file.
	 * @param normals               The list of normal vectors.
	 * @param normalsArray          An array to store normal vectors.
	 * @param currentVertexPointer  The current vertex index.
	 */
	private static void processNormal(int normIndex, List<Vector3f> normals, float[] normalsArray, int currentVertexPointer) {
	    /**
	     * Processes the normal index, updates the normals array, and handles invalid normal indices.
	     *
	     * @param normIndex             The normal index from the OBJ file.
	     * @param normals               The list of normal vectors.
	     * @param normalsArray          An array to store normal vectors.
	     * @param currentVertexPointer  The current vertex index.
	     */
	    if (normIndex >= 0 && normIndex < normals.size()) {
	        Vector3f currentNorm = normals.get(normIndex);
	        normalsArray[currentVertexPointer * 3] = currentNorm.x;
	        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
	        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	    } else {
	        handleInvalidNormalIndex(normIndex);
	    }
	}

	/**
	 * Handles the case when an invalid normal index is encountered.
	 *
	 * @param normIndex The invalid normal index.
	 */
	private static void handleInvalidNormalIndex(int normIndex) {
	    /**
	     * Prints an error message indicating that the normal index is invalid.
	     *
	     * @param normIndex The invalid normal index.
	     */
	    System.err.println("Invalid normal index: " + normIndex);
	}

	/**
	 * Handles the case when a NumberFormatException occurs during vertex data parsing.
	 *
	 * @param vertexData The vertex data from a line in the OBJ file.
	 * @param e           The NumberFormatException.
	 */
	private static void handleNumberFormatError(String[] vertexData, NumberFormatException e) {
	    /**
	     * Prints an error message indicating that a NumberFormatException occurred during vertex data parsing.
	     *
	     * @param vertexData The vertex data from a line in the OBJ file.
	     * @param e           The NumberFormatException.
	     */
	    System.err.println("Error parsing vertex data: " + Arrays.toString(vertexData));
	    e.printStackTrace();
	}
}
