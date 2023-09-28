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

package engine.terrains;

import java.util.Random;

/**
 * The HeightsGenerator class is responsible for assisting in the generation of procedural terrain.
 */
public class HeightsGenerator {
	
	private static final float AMPLITUDE = 70f;
	
	private Random random = new Random();
	private int seed;
	
	/**
     * Creates the HeightsGenerator object and generates the seed.
     */
	public HeightsGenerator() {
		this.seed = random.nextInt(1000000000);
	}
	
	/**
     * Generates the heights for the procedural terrain.
     * 
     * @param x The X position
     * @param y The Y position
     * @return the final height for the specific coordinates.
     */
	public float generateHeight(int x, int z) {
		return 1;
	}
}