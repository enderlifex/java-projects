
package opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VBO {

    private int vboId;
    private int textureVboId;
    private int vertexCount;

    public VBO() {
    }

    // Create both VBOs
    public void createVBO(float[] vertices, float[] texCoords) {

        if (vertices.length % 3 != 0 || texCoords.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid vertex or texture coordinate array");
        }

        if (vertices.length / 3 != texCoords.length / 2) {
            throw new IllegalArgumentException("Vertex and texture coordinate counts do not match");
        }

        // Delete old buffers
        deleteVBO();

        // Vertex VBO
        vboId = GL15.glGenBuffers();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // Texture coordinate VBO
        textureVboId = GL15.glGenBuffers();

        FloatBuffer texBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texBuffer.put(texCoords);
        texBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texBuffer, GL15.GL_STATIC_DRAW);

        vertexCount = vertices.length / 3;

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    // Render both VBOs
    public void renderVBO() {

        if (vertexCount == 0 || vboId == 0 || textureVboId == 0) {
            return;
        }

        // Vertex positions
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

        // Texture coordinates
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVboId);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0L);

        // Draw
        GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexCount);

        // Cleanup
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    // Delete both VBOs
    public void deleteVBO() {

        if (vboId != 0) {
            GL15.glDeleteBuffers(vboId);
            vboId = 0;
        }

        if (textureVboId != 0) {
            GL15.glDeleteBuffers(textureVboId);
            textureVboId = 0;
        }

        vertexCount = 0;
    }
}
