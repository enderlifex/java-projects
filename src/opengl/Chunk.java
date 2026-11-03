
package opengl;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    Renderer ren;

    int xChunk;
    int yChunk;
    int zChunk;

    VBO dirtVBO = new VBO();
    VBO grassVBO = new VBO();
    VBO grassTopVBO = new VBO();
    VBO stoneVBO = new VBO();

    List<Float> dirtVertices = new ArrayList<>();
    List<Float> dirtTexCoords = new ArrayList<>();

    List<Float> grassSideVertices = new ArrayList<>();
    List<Float> grassSideTexCoords = new ArrayList<>();

    List<Float> grassTopVertices = new ArrayList<>();
    List<Float> grassTopTexCoords = new ArrayList<>();

    List<Float> stoneVertices = new ArrayList<>();
    List<Float> stoneTexCoords = new ArrayList<>();

    public Chunk(int x, int y, int z, Renderer ren) {

        this.ren = ren;

        this.xChunk = x;
        this.yChunk = y;
        this.zChunk = z;
    }

    public void generatate() {

        dirtVertices.clear();
        dirtTexCoords.clear();

        grassSideVertices.clear();
        grassSideTexCoords.clear();

        grassTopVertices.clear();
        grassTopTexCoords.clear();

        stoneVertices.clear();
        stoneTexCoords.clear();

        

        for (int z = zChunk; z < zChunk + 16; z++) {

            for (int x = xChunk; x < xChunk + 16; x++) {

                int height = ren.terraingen.getTerrainHeight(x, z);

                ren.blocks[x + ren.offset][height + ren.offset][z + ren.offset] = 1;

                for (int k = 1; k <= 3; k++) {

                    ren.blocks[x + ren.offset][height - k + ren.offset][z + ren.offset] = 1;
                }

                for (int k = 4; k <= 13; k++) {

                    ren.blocks[x + ren.offset][height - k + ren.offset][z + ren.offset] = 1;
                }
            }
        }

       

        for (int z = zChunk; z < zChunk + 16; z++) {

            for (int x = xChunk; x < xChunk + 16; x++) {

                int height = ren.terraingen.getTerrainHeight(x, z);

                ren.drawCube(this, x, height, z, "grass_block");

                int y1 = height - 1;

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
    }

   

    public void render() {

        ren.dirtTexture.bind();
        dirtVBO.renderVBO();

        ren.grassSideTexture.bind();
        grassVBO.renderVBO();

        ren.grassTopTexture.bind();
        grassTopVBO.renderVBO();

        ren.stoneTexture.bind();
        stoneVBO.renderVBO();
    }

    

    public void cleanup() {

        dirtVBO.deleteVBO();
        grassVBO.deleteVBO();
        grassTopVBO.deleteVBO();
        stoneVBO.deleteVBO();
    }
}
