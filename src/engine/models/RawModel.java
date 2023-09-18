package engine.models;

/**
 * Represents a raw model, consisting of a VAO (Vertex Array Object) ID and vertex count.
 */
public class RawModel {
    private int vaoID;
    private int vertexCount;

    /**
     * Constructs a RawModel with the provided VAO ID and vertex count.
     *
     * @param vaoID       The ID of the Vertex Array Object (VAO).
     * @param vertexCount The number of vertices in the model.
     */
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Retrieves the vertex count of this raw model.
     *
     * @return The number of vertices.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Retrieves the VAO ID of this raw model.
     *
     * @return The VAO ID.
     */
    public int getVaoID() {
        return vaoID;
    }
}
