package opengl;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Textures {

    public Texture loadTexture(String fileName) {
        try {
            return TextureLoader.getTexture("PNG",Textures.class.getResourceAsStream("/" + fileName));      
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}