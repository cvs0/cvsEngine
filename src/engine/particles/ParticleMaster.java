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

package engine.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import engine.entities.Camera;
import engine.renderEngine.Loader;

public class ParticleMaster {
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer renderer;
	
	/**
     * Initializes the ParticleMaster with a loader and a projection matrix.
     *
     * @param loader           The loader for loading particle textures.
     * @param projectionMatrix The projection matrix for rendering particles.
     */
    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }
    
    /**
     * Updates all particles in the system.
     */
    public static void update(Camera camera) {
        Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
        
        while(mapIterator.hasNext()) {
            List<Particle> list = mapIterator.next().getValue();
            
            Iterator<Particle> iterator = list.iterator();
            
            while(iterator.hasNext()) {
                Particle p = iterator.next();
                
                boolean stillAlive = p.update(camera);
                
                if(!stillAlive) {
                    iterator.remove();
                    
                    if(list.isEmpty()) {
                        mapIterator.remove();
                    }
                }
            }
            
            InsertionSort.sortHighToLow(list);
        }
    }
    
    /**
     * Renders all particles in the system using the specified camera.
     *
     * @param camera The camera used for rendering.
     */
    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);
    }
    
    /**
     * Cleans up resources used by the ParticleMaster.
     */
    public static void cleanUp() {
        renderer.cleanUp();
    }
    
    /**
     * Adds a particle to the particle system.
     *
     * @param particle The particle to add.
     */
    public static void addParticle(Particle particle) {
        List<Particle> list = particles.get(particle.getTexture());
        
        if(list == null) {
            list = new ArrayList<Particle>();
            particles.put(particle.getTexture(), list);
        }
        
        list.add(particle);
    }
}
