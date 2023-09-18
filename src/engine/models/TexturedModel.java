package engine.models;

import engine.textures.ModelTexture;

/**
 * Represents a textured model, combining a raw model and a model texture.
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

    /**
     * Constructs a TexturedModel with the provided raw model and model texture.
     *
     * @param model   The raw model representing the geometry.
     * @param texture The model texture applied to the model.
     */
    public TexturedModel(RawModel model, ModelTexture texture) {
        this.rawModel = model;
        this.texture = texture;
    }

    /**
     * Retrieves the raw model associated with this textured model.
     *
     * @return The raw model.
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * Retrieves the model texture applied to this textured model.
     *
     * @return The model texture.
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
