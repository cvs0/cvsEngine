package engine.engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.entities.Light;
import engine.entities.Player;
import engine.guis.GuiRenderer;
import engine.guis.GuiTexture;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.MasterRenderer;
import engine.renderEngine.OBJLoader;
import engine.terrains.Terrain;
import engine.textures.ModelTexture;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import engine.toolbox.FPSCounter;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		FPSCounter fpsCounter = new FPSCounter();
		
		// TERRAIN TEXTURE //
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		// ****************//
		
		Light light = new Light(new Vector3f(200,200,100), new Vector3f(1,1,1));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		
		//  PLAYER //
		RawModel gunModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel gun = new TexturedModel(gunModel, new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(gun, new Vector3f(0,0,-25), 0, 0, 0, 1);
		Camera camera = new Camera(player);
		//*********//
		
		// GUI //
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("exampleTexture"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		//*****//
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move(terrain);
			
			renderer.processEntity(player);
			
			renderer.processTerrain(terrain);
			
			renderer.render(lights, camera);
			
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
			fpsCounter.update();
			
			int currentFPS = fpsCounter.getFPS();
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();

	}

}
