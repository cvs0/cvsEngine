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
				// left bottom tri
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				
				// right top tri
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			
			renderer.render(model);
			
			DisplayManager.updateDisplay();
		}
		
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
