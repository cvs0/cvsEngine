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

package engine.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.models.RawModel;
import engine.renderEngine.Loader;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import engine.toolbox.MathUtils;

/**
 * The Terrain class represents a terrain in the game world. It includes methods for generating and rendering terrains,
 * as well as calculating terrain heights and normals.
 */
public class Terrain {
	private static final float SIZE = 1500;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float z;
	
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	/**
     * Creates a new Terrain object.
     *
     * @param gridX     The grid X coordinate of the terrain.
     * @param gridZ     The grid Z coordinate of the terrain.
     * @param loader    The loader used to load the terrain model.
     * @param texturePack The texture pack for the terrain.
     * @param blendMap    The blend map for terrain textures.
     * @param heightMap   The name of the height map image file.
     */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}

	/**
     * Gets the X coordinate of the terrain in the world.
     *
     * @return The X coordinate of the terrain.
     */
	public float getX() {
		return x;
	}

	/**
     * Sets the X coordinate of the terrain in the world.
     *
     * @param x The new X coordinate of the terrain.
     */
	public void setX(float x) {
		this.x = x;
	}

	/**
     * Gets the Z coordinate of the terrain in the world.
     *
     * @return The Z coordinate of the terrain.
     */
	public float getZ() {
		return z;
	}

	/**
     * Sets the Z coordinate of the terrain in the world.
     *
     * @param z The new Z coordinate of the terrain.
     */
	public void setZ(float z) {
		this.z = z;
	}

	/**
     * Gets the raw model representing the terrain's geometry.
     *
     * @return The raw model of the terrain.
     */
	public RawModel getModel() {
		return model;
	}

	/**
	 * Sets the raw model that represents the geometry of the terrain.
	 *
	 * @param model The raw model to set for the terrain.
	 */
	public void setModel(RawModel model) {
	    this.model = model;
	}


	/**
     * Gets the texture pack used for the terrain.
     *
     * @return The terrain texture pack.
     */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
     * Gets the blend map used for blending terrain textures.
     *
     * @return The blend map.
     */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	/**
     * Calculates the height of the terrain at a given world position.
     *
     * @param worldX The world X coordinate.
     * @param worldZ The world Z coordinate.
     * @return The height of the terrain at the given position.
     */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		if(gridX >= heights.length - 1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		
		if (xCoord <= (1-zCoord)) {
			answer = MathUtils
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = MathUtils
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
	}

	/**
	 * Generates the terrain's geometry and loads it into a raw model.
	 *
	 * @param loader    The loader used to load the model.
	 * @param heightMap The name of the height map image file.
	 * @return The raw model representing the terrain's geometry.
	 */
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		HeightsGenerator generator = new HeightsGenerator((int) x, (int) z, 128, 56375);
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = 128;
		
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		
		int vertexPointer = 0;
		
		for(int i = 0; i < VERTEX_COUNT; i++){
			for(int j = 0; j < VERTEX_COUNT; j++){
				vertices[vertexPointer * 3] = (float)j / ((float) VERTEX_COUNT - 1) * SIZE;
				
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float)i / ((float) VERTEX_COUNT - 1) * SIZE;
				
				Vector3f normal = calculateNormal(j, i, generator);
				
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				
				textureCoords[vertexPointer * 2] = (float)j / ((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float)i / ((float)VERTEX_COUNT - 1);
				
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++){
			for(int gx = 0; gx < VERTEX_COUNT - 1; gx++){
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	/**
	 * Calculates the normal vector at a given position on the terrain.
	 *
	 * @param x      The X coordinate on the terrain.
	 * @param z      The Z coordinate on the terrain.
	 * @param image  The height map image used for height calculations.
	 * @return The normal vector at the specified position.
	 */
	private Vector3f calculateNormal(int x, int z, HeightsGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		
		return normal;
	}
	
	/**
	 * Retrieves the height value at a specific position on the terrain.
	 *
	 * @param x      The X coordinate on the terrain.
	 * @param z      The Z coordinate on the terrain.
	 * @param image  The height map image used for height calculations.
	 * @return The height value at the specified position.
	 */
	private float getHeight(int x, int z, HeightsGenerator generator) {
		return generator.generateHeight(x, z);
	}
}
