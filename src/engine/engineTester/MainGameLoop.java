package engine.engineTester;

import org.lwjgl.opengl.Display;

import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.RawModel;
import engine.renderEngine.Renderer;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		float[] vertices = {
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0,
				0.5f, -0.5f, 0,
				0.5f, 0.5f, 0
		};
		
		int[] indices = {
			0,1,3,
			3,1,2
		};
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			
			renderer.render(model);
			
			DisplayManager.updateDisplay();
		}
		
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
