package engine.textures;

/**
 * A class representing a texture used for terrain.
 */
public class TerrainTexture {
    
    private int textureID;

    /**
     * Create a new terrain texture with the specified texture ID.
     * @param textureID The ID of the texture.
     */
    public TerrainTexture(int textureID) {
        super();
        this.textureID = textureID;
    }
    
    /**
     * Get the ID of the terrain texture.
     * @return The texture ID.
     */
    public int getTextureID() {
        return textureID;
    }
}
