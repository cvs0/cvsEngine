package engine.engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.Player;
import engine.fontMeshCreator.FontType;
import engine.fontMeshCreator.GUIText;
import engine.fontRendering.TextMaster;
import engine.guis.GuiRenderer;
import engine.guis.GuiTexture;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.normalMappingObjConverter.NormalMappedObjLoader;
import engine.particles.ParticleMaster;
import engine.particles.ParticleSystem;
import engine.particles.ParticleTexture;
import engine.postProcessing.Fbo;
import engine.postProcessing.PostProcessing;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.MasterRenderer;
import engine.renderEngine.OBJLoader;
import engine.terrains.Terrain;
import engine.textures.ModelTexture;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import engine.toolbox.FPSCounter;
import engine.toolbox.MousePicker;
import engine.water.WaterFrameBuffers;
import engine.water.WaterRenderer;
import engine.water.WaterShader;
import engine.water.WaterTile;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        FPSCounter fpsCount = new FPSCounter();

        Player player = setupPlayer(loader);
        Camera camera = new Camera(player);

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadTexture("/candara/candara"),
                new File("res/candara/candara.fnt"));

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        TexturedModel rocks = new TexturedModel(OBJLoader.loadObjModel("rocks", loader),
                new ModelTexture(loader.loadTexture("rocks")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                fernTextureAtlas);

        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader),
                new ModelTexture(loader.loadTexture("pine")));
        bobble.getTexture().setHasTransparency(true);

        fern.getTexture().setHasTransparency(true);

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);

        List<Entity> entities = new ArrayList<Entity>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();

        entities.add(player);

        TexturedModel barrelModel = new TexturedModel(
                NormalMappedObjLoader.loadOBJ("barrel", loader),
                new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);

        TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
                new ModelTexture(loader.loadTexture("crate")));
        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(
                NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);

        Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);

        Random random = new Random(5666778);
        for (int i = 0; i < 60; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, 0.9f));
                }
            }
            if (i % 2 == 0) {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                }
            }
        }
        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));

        List<Light> lights = new ArrayList<Light>();
        Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);

        MasterRenderer renderer = new MasterRenderer(loader, 0f, 5.0f, camera);

        ParticleMaster.init(loader, renderer.getProjectionMatrix());

        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
                renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(75, -75, 0);
        waters.add(water);

        ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"),
                4, true);

        ParticleSystem system = new ParticleSystem(particleTexture, 200f, 25f, 0.3f, 4f, 1f);

        Fbo fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);

        while (!Display.isCloseRequested()) {
            GUIText text = new GUIText(Integer.toString(fpsCount.getFPS()), 3f, font,
                    new Vector2f(0f, 0f), 1f, true, 0.5f, 0.1f, 0.0f, 0.5f,
                    new Vector2f(0.000f, 0.000f), new Vector3f(1.0f, 0.0f, 0.0f));
            text.setColour(1, 0, 0);
            fpsCount.update();
            player.move(terrain);
            camera.move();
            picker.update();

            system.generateParticles(player.getPosition());

            ParticleMaster.update(camera);

            entity.increaseRotation(0, 0.1f, 0);
            entity2.increaseRotation(0, 0.1f, 0);
            entity3.increaseRotation(0, 0.1f, 0);

            renderer.renderShadowMap(entities, sun);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            bindWaterReflectionBuffer(buffers);
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
                    new Vector4f(0, 1, 0, -water.getHeight() + 1));
            camera.getPosition().y += distance;
            camera.invertPitch();

            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
                    new Vector4f(0, -1, 0, water.getHeight()));

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            unbindWaterReflectionBuffer(buffers);
            fbo.bindFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
                    new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);

            ParticleMaster.renderParticles(camera);
            fbo.unbindFrameBuffer();
            PostProcessing.doPostProcessing(fbo.getColourTexture());

            guiRenderer.render(guiTextures);
            TextMaster.render();

            DisplayManager.updateDisplay();
            text.remove();
        }

        cleanUp(renderer, guiRenderer, loader, buffers, waterShader);
        closeDisplay();
    }
    
    private static void bindWaterReflectionBuffer(WaterFrameBuffers buffers) {
    	buffers.bindRefractionFrameBuffer();
    }
    
    private static void unbindWaterReflectionBuffer(WaterFrameBuffers buffers) {
    	buffers.unbindCurrentFrameBuffer();
    }
    
    private static Player setupPlayer(Loader loader) {
        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel,
                new ModelTexture(loader.loadTexture("playerTexture1")));
        return new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
    }
    
    private static void closeDisplay() {
    	DisplayManager.closeDisplay();
    }
    
    private static void cleanUp(MasterRenderer renderer, GuiRenderer guiRenderer, Loader loader,
            WaterFrameBuffers buffers, WaterShader waterShader) {
        PostProcessing.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
    }
}
