package opengl;
import com.raylabz.opensimplex.OpenSimplexNoise;
public class TerrainGen {

	OpenSimplexNoise noise = new OpenSimplexNoise(34757389); // random noise library i used
	
	
	public TerrainGen() {
		
		
	}
	
	public int getTerrainHeight(int x, int z) {

	    double noiseValue = noise.getNoise2D( x*0.1, z*0.1);
	    return (int)(noiseValue *10+20);
	}
}
