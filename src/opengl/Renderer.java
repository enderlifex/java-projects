
package opengl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import org.lwjgl.input.Mouse;

import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;


public class Renderer {

	public List<Chunk> chunkList = new ArrayList<>();
	public int renderDistance = 8;
    //camera

    float cameraX = 0f;
    float cameraY = 0f;
    float cameraZ = 5f;
    float yaw = 0f;
    float pitch = 0f;
    float cameraSpeed = 5f;
    float sensitivity = 0.15f;
    long lastFrame = System.nanoTime();
    
    int previousChunkX = Integer.MIN_VALUE;
    int previousChunkZ = Integer.MIN_VALUE;
    
    //raycast
    int dx =0;
    int dy=0;
    int dz=0;
    //ray
    double rayX;
    double rayY;
    double rayZ;
    int hitRange = 4;

    Set<BlockPos> blocks = new HashSet<>();
    
    int chunks[][] = new int[200][200];

    int offset = 200;
    
    Chunk chunk = new Chunk(this);
    Chunk chunk2 = new Chunk(this);
    Chunk chunk3 = new Chunk(this);
    Chunk chunk4 = new Chunk(this);
    Chunk chunk5 = new Chunk(this);


    Texture dirtTexture;
    Texture grassSideTexture;
    Texture grassTopTexture;
    Texture stoneTexture;
    Texture flowerRoseTexture;
    Texture sandTexture;


    Textures textures = new Textures();
    TerrainGen terraingen = new TerrainGen();
    Player p = new Player(this);
    
    
    int CurrentChunkX;
    int CurrentChunkZ;


    public Renderer() {

        dirtTexture = textures.loadTexture("dirt.png");
        grassSideTexture = textures.loadTexture("grass_side.png");
        grassTopTexture = textures.loadTexture("grass_top.png");  
        stoneTexture = textures.loadTexture("stone.png");
        flowerRoseTexture = textures.loadTexture("flower_rose.png");
        sandTexture = textures.loadTexture("sand.png");
        dirtTexture.setTextureFilter(GL11.GL_NEAREST);
        grassSideTexture.setTextureFilter(GL11.GL_NEAREST);
        grassTopTexture.setTextureFilter(GL11.GL_NEAREST);
        stoneTexture.setTextureFilter(GL11.GL_NEAREST);
        flowerRoseTexture.setTextureFilter(GL11.GL_NEAREST);
        sandTexture.setTextureFilter(GL11.GL_NEAREST);


        
        
            for (int z = 0; z < 49; z++) {
               for (int x = 0; x < 49; x++) {
            	   chunks[z][x] = 0;
               }
                  }
    	
            generateChunks();
    }


    public void render() {


        // Build the terrain mesh once
    	//chunk.generatate(0, 0, 0);
    	

    	
    	

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glCullFace(GL11.GL_BACK);


        while (!Display.isCloseRequested()) {
        	

        	CurrentChunkX = (int) Math.floor(cameraX / 16.0);
        	CurrentChunkZ = (int) Math.floor(cameraZ / 16.0);

            // Set background colour

            GL11.glClearColor(0f, 0f, 0f, 1f);


            // Clear the screen

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            GL11.glLoadIdentity();


            updateCamera();

            applyCamera();

            updateChunks();
            renderChunks(cameraX, cameraZ);


            Display.update();


            Display.sync(240);


        }


        chunk.dirtVBO.deleteVBO();

        chunk.grassVBO.deleteVBO();

        chunk.grassTopVBO.deleteVBO();

        chunk.stoneVBO.deleteVBO();
        
        chunk.flowerVBO.deleteVBO();
        
        chunk.sandVBO.deleteVBO();
        
        for (Chunk chunk : chunkList) {
            chunk.cleanup();
        }

        chunkList.clear();


        Display.destroy();


    }


    public void applyCamera() {


        GL11.glRotatef(pitch, 1f, 0f, 0f);
        GL11.glRotatef(yaw, 0f, 1f, 0f);
        GL11.glTranslatef(-cameraX, -cameraY, -cameraZ);

    }

    public void drawFlower(Chunk targetChunk,float x, float y, float z, String blockType) {
    	
    	   

           List<Float> v;
           List<Float> t;
    	

           if (blockType.equals("flower")) {
               v = targetChunk.flowerVertices;
               t = targetChunk.flowerTexCoords;

           } else {
        	   v = null;
               t = null;
           }
           
           

            addFace(v, t,
            		 x-0.5f, y-0.5f, z-0.5f,  //bottom left
            		    x+0.5f, y-0.5f, z+0.5f,  //bottom right
            		    x+0.5f, y+0.5f, z+0.5f,  //top right
            		    x-0.5f, y+0.5f, z-0.5f   //top left
            		    );  

           


           //left



               addFace(v, t,
            		   x+0.5f, y-0.5f, z-0.5f,  
            		    x-0.5f, y-0.5f, z+0.5f,  
            		    x-0.5f, y+0.5f, z+0.5f,  
            		    x+0.5f, y+0.5f, z-0.5f
            		    );  

           }
           
           
           
    	
    
    public void drawCube(Chunk targetChunk, float x, float y, float z, String blockType) {

        int bx = (int)x + offset;
        int by = (int)y + offset;
        int bz = (int)z + offset;

        List<Float> v;
        List<Float> t;
        
       

        if (blockType.equals("dirt_block")) {
            v = targetChunk.dirtVertices;
            t = targetChunk.dirtTexCoords;

        } else if (blockType.equals("grass_block")) {
            v = targetChunk.grassSideVertices;
            t = targetChunk.grassSideTexCoords;

        }else if (blockType.equals("sand_block")) {
        	 v = targetChunk.sandVertices;
             t = targetChunk.sandTexCoords;
        }
        
        else {
            v = targetChunk.stoneVertices;
            t = targetChunk.stoneTexCoords;
        }


        //front
        
        if (!isBlockAt((int)x, (int)y, (int)z + 1)) {

            addFace(v, t,
                x-0.5f,y-0.5f,z+0.5f,
                x+0.5f,y-0.5f,z+0.5f,
                x+0.5f,y+0.5f,z+0.5f,
                x-0.5f,y+0.5f,z+0.5f);

        }


        //back

        if (!isBlockAt((int)x, (int)y, (int)z - 1)) {

            addFace(v, t,
                x+0.5f,y-0.5f,z-0.5f,
                x-0.5f,y-0.5f,z-0.5f,
                x-0.5f,y+0.5f,z-0.5f,
                x+0.5f,y+0.5f,z-0.5f);

        }


        //right

        if (!isBlockAt((int)x + 1, (int)y, (int)z)) {

            addFace(v, t,
                x+0.5f,y-0.5f,z+0.5f,
                x+0.5f,y-0.5f,z-0.5f,
                x+0.5f,y+0.5f,z-0.5f,
                x+0.5f,y+0.5f,z+0.5f);

        }


        //left

        if (!isBlockAt((int)x - 1, (int)y, (int)z)) {

            addFace(v, t,
                x-0.5f,y-0.5f,z-0.5f,
                x-0.5f,y-0.5f,z+0.5f,
                x-0.5f,y+0.5f,z+0.5f,
                x-0.5f,y+0.5f,z-0.5f);

        }


        //top

        if (!isBlockAt((int)x, (int)y + 1, (int)z)) {

            if(blockType.equals("grass_block")) {

            	
            	addFace(targetChunk.grassTopVertices, targetChunk.grassTopTexCoords,
                    x-0.5f,y+0.5f,z+0.5f,
                    x+0.5f,y+0.5f,z+0.5f,
                    x+0.5f,y+0.5f,z-0.5f,
                    x-0.5f,y+0.5f,z-0.5f);
            	
            } else {

                addFace(v, t,
                    x-0.5f,y+0.5f,z+0.5f,
                    x+0.5f,y+0.5f,z+0.5f,
                    x+0.5f,y+0.5f,z-0.5f,
                    x-0.5f,y+0.5f,z-0.5f);

            }

        }


        //bottom

        if (!isBlockAt((int)x, (int)y - 1, (int)z)) {

            if(blockType.equals("grass_block")) {

            	addFace(targetChunk.dirtVertices, targetChunk.dirtTexCoords,
                    x-0.5f,y-0.5f,z-0.5f,
                    x+0.5f,y-0.5f,z-0.5f,
                    x+0.5f,y-0.5f,z+0.5f,
                    x-0.5f,y-0.5f,z+0.5f);

            } else {

                addFace(v, t,
                    x-0.5f,y-0.5f,z-0.5f,
                    x+0.5f,y-0.5f,z-0.5f,
                    x+0.5f,y-0.5f,z+0.5f,
                    x-0.5f,y-0.5f,z+0.5f);

            }

        }


    }


    public void updateCamera() {


        long currentFrame = System.nanoTime();

        float delta = (currentFrame - lastFrame) / 1000000000f;

        lastFrame = currentFrame;

        float movement = cameraSpeed * delta;

        yaw += Mouse.getDX() * sensitivity;

        pitch -= Mouse.getDY() * sensitivity;

        pitch = Math.max(-90f, Math.min(90f, pitch));

        float sin = (float) Math.sin(Math.toRadians(yaw));
        float cos = (float) Math.cos(Math.toRadians(yaw));
        


        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cameraX += sin * movement;
            cameraZ -= cos * movement;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cameraX -= sin * movement;
            cameraZ += cos * movement;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cameraX -= cos * movement;
            cameraZ -= sin * movement;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cameraX += cos * movement;
            cameraZ += sin * movement;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            cameraY += movement;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cameraY -= movement;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
        	cameraSpeed+=5;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
        	cameraSpeed-=5;
        }
        
      
        
        //raycast 
        
        double dx = -Math.sin(yaw) * Math.cos(pitch);
        double dy = Math.sin(pitch);
        double dz = Math.cos(yaw) * Math.cos(pitch);
        
        
        rayX = cameraX + dx * hitRange;
        rayY = cameraY + dy * hitRange;
        rayZ = cameraZ + dz * hitRange;
        
       // System.out.println(" " + rayX+" "+rayY+" "+rayZ);

    }


    public void addVertex(
        List<Float> vertices,
        List<Float> texCoords,
        float x, float y, float z,
        float u, float v
    ) {

        vertices.add(x);

        vertices.add(y);

        vertices.add(z);

        texCoords.add(u);

        texCoords.add(v);

    }


    public void addFace(
        List<Float> vertices,
        List<Float> texCoords,
        float x1, float y1, float z1,
        float x2, float y2, float z2,
        float x3, float y3, float z3,
        float x4, float y4, float z4
    ) {

        addVertex(vertices, texCoords, x1, y1, z1, 1f, 1f);
        addVertex(vertices, texCoords, x2, y2, z2, 0f, 1f);
        addVertex(vertices, texCoords, x3, y3, z3, 0f, 0f);
        addVertex(vertices, texCoords, x4, y4, z4, 1f, 0f);

    }


    public float[] toArray(List<Float> list) {

        float[] array = new float[list.size()];

        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;

    }
    public void renderChunks(float playerX, float playerZ) {

        int playerChunkX = (int) Math.floor(playerX / 16.0);
        int playerChunkZ = (int) Math.floor(playerZ / 16.0);

        for (Chunk chunk : chunkList) {

            int chunkX = Math.floorDiv(chunk.xChunk, 16);
            int chunkZ = Math.floorDiv(chunk.zChunk, 16);

            int dx = chunkX - playerChunkX;
            int dz = chunkZ - playerChunkZ;

            int distanceSquared = dx * dx + dz * dz;

            if (distanceSquared <= renderDistance * renderDistance) {
                chunk.render();
            }
        }
    }
    
    public void generateChunks() {

        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {

                Chunk chunk = new Chunk(this);

                chunk.generatate(
                    x * 16,
                    0,
                    z * 16
                );

                chunkList.add(chunk);
            }
        }
    }
    public void updateChunks() {

        int playerChunkX = (int) Math.floor(cameraX / 16.0);
        int playerChunkZ = (int) Math.floor(cameraZ / 16.0);

        if (playerChunkX == previousChunkX &&
            playerChunkZ == previousChunkZ) {
            return;
        }

        previousChunkX = playerChunkX;
        previousChunkZ = playerChunkZ;

        for (int x = playerChunkX - renderDistance;
             x <= playerChunkX + renderDistance; x++) {

            for (int z = playerChunkZ - renderDistance;
                 z <= playerChunkZ + renderDistance; z++) {

                boolean exists = false;

                for (Chunk chunk : chunkList) {
                    if (chunk.xChunk == x * 16 &&
                        chunk.zChunk == z * 16) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Chunk chunk = new Chunk(this);
                    chunk.generatate(x * 16, 0, z * 16);
                    chunkList.add(chunk);
                }
            }
        }
    }
    static class BlockPos {
        final int x, y, z;

        BlockPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BlockPos)) return false;

            BlockPos other = (BlockPos) obj;

            return x == other.x &&
                   y == other.y &&
                   z == other.z;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(x, y, z);
        }
    }
    public void addBlock(int x, int y, int z) {
        blocks.add(new BlockPos(x, y, z));
    }

    public boolean isBlockAt(int x, int y, int z) {
        return blocks.contains(new BlockPos(x, y, z));
    }

}
