
package opengl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Chunk {

    Renderer ren;

    int xChunk;
    int yChunk;
    int zChunk;
    
    Random random = new Random();

    VBO dirtVBO = new VBO();
    VBO grassVBO = new VBO();
    VBO grassTopVBO = new VBO();
    VBO stoneVBO = new VBO();
    VBO flowerVBO = new VBO();
    VBO sandVBO = new VBO();

    List<Float> dirtVertices = new ArrayList<>();
    List<Float> dirtTexCoords = new ArrayList<>();

    List<Float> grassSideVertices = new ArrayList<>();
    List<Float> grassSideTexCoords = new ArrayList<>();

    List<Float> grassTopVertices = new ArrayList<>();
    List<Float> grassTopTexCoords = new ArrayList<>();

    List<Float> stoneVertices = new ArrayList<>();
    List<Float> stoneTexCoords = new ArrayList<>();
    
    List<Float> flowerVertices = new ArrayList<>();
    List<Float> flowerTexCoords = new ArrayList<>();
    
    List<Float> sandVertices = new ArrayList<>();
    List<Float> sandTexCoords = new ArrayList<>();
    
    int chunkSize = 16;  //700

    public Chunk(Renderer ren) {

        this.ren = ren;

        
    }

    public void generatate(int x0, int y0, int z0) {

    	this.xChunk = x0;
        this.yChunk = y0;
        this.zChunk = z0;
    	
    	
    	
        dirtVertices.clear();
        dirtTexCoords.clear();

        grassSideVertices.clear();
        grassSideTexCoords.clear();

        grassTopVertices.clear();
        grassTopTexCoords.clear();

        stoneVertices.clear();
        stoneTexCoords.clear();
        
        flowerVertices.clear();
        flowerTexCoords.clear();
        
        sandVertices.clear();
        sandTexCoords.clear();

        

     
        for (int z = zChunk; z < zChunk + chunkSize; z++) {

            for (int x = xChunk; x < xChunk + chunkSize; x++) {

                int height = ren.terraingen.getTerrainHeight(x, z);

                ren.addBlock(x, height, z);

                for (int k = 1; k <= 13; k++) {
                    ren.addBlock(x, height - k, z);
                }
            }
        }

       

        for (int z = zChunk; z < zChunk + chunkSize; z++) {

            for (int x = xChunk; x < xChunk + chunkSize; x++) {

                int height = ren.terraingen.getTerrainHeight(x, z);
                int sandSeed = ren.terraingen.getSandSeed(x, z);
                int y1 = height - 1;
                
                if(random.nextInt(1,200) == 67) {
                	ren.drawFlower(this, x, height+1, z, "flower");
                }
               
                
                if(sandSeed >= 25f && height < 18f) {
                	ren.drawCube(this, x, height, z, "sand_block");
                }else {
                	ren.drawCube(this, x, height, z, "grass_block");
                }
                
                

                for (int k = 0; k < 3; k++) {
                    ren.drawCube(this, x, y1, z, "dirt_block");
                    y1--;
                }

                for (int k = 0; k < 10; k++) {
                    ren.drawCube(this, x, y1, z, "stone_block");

                    y1--;
                }
            }
        }

       

        dirtVBO.createVBO(
            ren.toArray(dirtVertices),
            ren.toArray(dirtTexCoords)
        );

        grassVBO.createVBO(
            ren.toArray(grassSideVertices),
            ren.toArray(grassSideTexCoords)
        );

        grassTopVBO.createVBO(
            ren.toArray(grassTopVertices),
            ren.toArray(grassTopTexCoords)
        );

        stoneVBO.createVBO(
            ren.toArray(stoneVertices),
            ren.toArray(stoneTexCoords)
        );
        
        flowerVBO.createVBO(
                ren.toArray(flowerVertices),
                ren.toArray(flowerTexCoords)
            );
        sandVBO.createVBO(
                ren.toArray(sandVertices),
                ren.toArray(sandTexCoords)
            );
    }

   

    public void render() {

    	GL11.glColor3f(0.76f, 0.76f, 0.76f);
        ren.dirtTexture.bind();
        dirtVBO.renderVBO();
        GL11.glColor3f(1f, 1f, 1f);
        
        GL11.glColor3f(0.76f, 0.76f, 0.76f);
        ren.grassSideTexture.bind();
        grassVBO.renderVBO();
        GL11.glColor3f(1f, 1f, 1f);
        
        GL11.glColor3f(0.60f, 0.80f, 0.35f);
        ren.grassTopTexture.bind();
        grassTopVBO.renderVBO();
        GL11.glColor3f(1f, 1f, 1f);
        
        GL11.glColor3f(0.76f, 0.76f, 0.76f);
        ren.stoneTexture.bind();
        stoneVBO.renderVBO();
        GL11.glColor3f(1f, 1f, 1f);
        
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
        GL11.glDisable(GL11.GL_CULL_FACE);
        ren.flowerRoseTexture.bind();
        flowerVBO.renderVBO();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
        GL11.glColor3f(0.76f, 0.76f, 0.76f);
        ren.sandTexture.bind();
        sandVBO.renderVBO();
        GL11.glColor3f(1f, 1f, 1f);
        
        
    }

    

    public void cleanup() {

        dirtVBO.deleteVBO();
        grassVBO.deleteVBO();
        grassTopVBO.deleteVBO();
        flowerVBO.deleteVBO();
        stoneVBO.deleteVBO();
        sandVBO.deleteVBO();
    }
}
