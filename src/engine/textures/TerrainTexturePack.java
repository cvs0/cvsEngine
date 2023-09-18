package engine.textures;

/**
 * A class representing a pack of textures for terrain rendering, including a background texture and three color textures.
 */
public class TerrainTexturePack {
    
    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture;
    private TerrainTexture gTexture;
    private TerrainTexture bTexture;
    
    /**
     * Create a new terrain texture pack with the specified textures.
     * @param backgroundTexture The background texture.
     * @param rTexture The red channel texture.
     * @param gTexture The green channel texture.
     * @param bTexture The blue channel texture.
     */
    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    /**
     * Get the background texture of the terrain texture pack.
     * @return The background texture.
     */
    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Get the red channel texture of the terrain texture pack.
     * @return The red channel texture.
     */
    public TerrainTexture getrTexture() {
        return rTexture;
    }

    /**
     * Get the green channel texture of the terrain texture pack.
     * @return The green channel texture.
     */
    public TerrainTexture getgTexture() {
        return gTexture;
    }

    /**
     * Get the blue channel texture of the terrain texture pack.
     * @return The blue channel texture.
     */
    public TerrainTexture getbTexture() {
        return bTexture;
    }
}
