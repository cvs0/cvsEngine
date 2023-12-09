/**
 * CvsEngine
 *
 * @author cvs0
 * @version 1.0.0
 *
 * @license
 * MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

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

/**
 * The EntityRenderer class is responsible for rendering entities in the game world.
 */
public class EntityRenderer {

    private StaticShader shader;

    /**
     * Creates an EntityRenderer with the provided shader and projection matrix.
     *
     * @param shader           The static shader used for rendering.
     * @param projectionMatrix The projection matrix for the camera.
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders a map of textured models and their associated entities.
     *
     * @param entities A map where each textured model is associated with a list of entities to render.
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            if (model != null) {
                prepareTexturedModel(model);
    
                List<Entity> batch = entities.get(model);
    
                if (batch != null) {
                    for (Entity entity : batch) {
                        if (entity != null) {
                            prepareInstance(entity);
                            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                                    GL11.GL_UNSIGNED_INT, 0);
                        }
                    }
                }
    
                unbindTexturedModel();
            }
        }
    }
    
    /**
     * Prepares the textured model for rendering by binding its VAO, enabling attribute arrays, and loading shader uniforms.
     *
     * @param model The textured model to prepare.
     * @throws IllegalArgumentException If the provided model or its components are invalid.
     */
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
    
        int textureID = model.getTexture().getID();
        if (textureID != 0) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        }
    }    

    /**
     * Unbinds the currently prepared textured model by disabling attribute arrays and unbinding the VAO.
     * Note: Requires OpenGL 1.1 support to unbind textured models.
     */
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

    /**
     * Prepares an entity instance for rendering by loading its transformation matrix and texture offsets.
     *
     * @param entity The entity to prepare.
     */
    private void prepareInstance(Entity entity) {
        if (entity == null) {
            System.err.println("Invalid entity provided for instance preparation.");
            return;
        }
    
        Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
    
        if (transformationMatrix == null) {
            System.err.println("Failed to create transformation matrix for the entity.");
            return;
        }
    
        shader.loadTransformationMatrix(transformationMatrix);
    
        if (entity.getTextureXOffset() >= 0 && entity.getTextureYOffset() >= 0) {
            shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
        } else {
            System.err.println("Invalid texture offsets provided for instance preparation.");
        }
    }
}
