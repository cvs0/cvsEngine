package engine.renderEngine;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void getVaoID() {
		return vaoID;
	}
}
