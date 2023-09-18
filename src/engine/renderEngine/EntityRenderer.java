package engine.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix4f;

import engine.entities.Entity;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.textures.ModelTexture;
import engine.toolbox.MathUtils;



public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
				
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			
			List<Entity> batch = entities.get(model);
			
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
	    if (model == null || model.getRawModel() == null || model.getTexture() == null) {
	        throw new IllegalArgumentException("Invalid textured model provided");
	    }

	    RawModel rawModel = model.getRawModel();
	    if (rawModel == null) {
	        throw new IllegalArgumentException("Invalid raw model in textured model");
	    }

	    GL30.glBindVertexArray(rawModel.getVaoID());

	    GL20.glEnableVertexAttribArray(0);
	    GL20.glEnableVertexAttribArray(1);
	    GL20.glEnableVertexAttribArray(2);

	    ModelTexture texture = model.getTexture();
	    if (texture == null) {
	        throw new IllegalArgumentException("Invalid texture in textured model");
	    }

	    shader.loadNumberOfRows(texture.getNumberOfRows());

	    if (texture.isHasTransparency()) {
	        MasterRenderer.disableCulling();
	    }

	    shader.loadFakeLightingVariable(texture.isUseFakeLighting());

	    shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

	    if (model.getTexture() != null && model.getTexture().getID() != 0) {
	        GL13.glActiveTexture(GL13.GL_TEXTURE0);
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	    }
	}

	
	private void unbindTexturedModel() {
	    if (!GLContext.getCapabilities().OpenGL11) {
	        System.err.println("OpenGL 1.1 is required to unbind textured models, but it's not supported.");
	        return;
	    }

	    MasterRenderer.enableCulling();

	    GL20.glDisableVertexAttribArray(0);
	    GL20.glDisableVertexAttribArray(1);
	    GL20.glDisableVertexAttribArray(2);

	    GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
	    if (entity == null) {
	        System.err.println("Invalid entity provided for instance preparation.");
	        return;
	    }

	    Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(),
	            entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());

	    shader.loadTransformationMatrix(transformationMatrix);

	    if (entity.getTextureXOffset() >= 0 && entity.getTextureYOffset() >= 0) {
	        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	    } else {
	        System.err.println("Invalid texture offsets provided for instance preparation.");
	    }
	}
}