package opengl;
import java.util.Random;

import com.raylabz.opensimplex.OpenSimplexNoise;
public class TerrainGen {
	
	long seed = 1000000000L;

	OpenSimplexNoise noise;  //  noise library
	
	
	
	public TerrainGen() {
		Random random = new Random();
		seed = random.nextInt(900000000) + 1000000000;
		noise = new OpenSimplexNoise(seed); 
		
	}
	
	public int getTerrainHeight(int x, int z) {
	    double noiseValue = noise.getNoise2D( x*0.9, z*0.9);
	    return (int)(noiseValue *10+20);
	}
}
