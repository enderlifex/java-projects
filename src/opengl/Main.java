
package opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

public class Main {
	
	 

	 
	
	

    public static void main(String[] args) {
    	
        try {
            Display.setDisplayMode(new DisplayMode(1920, 1080));
            Display.create();
            Mouse.setGrabbed(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
            
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            
            
            GLU.gluPerspective(70f,1920f / 1080f,0.01f,1000f);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            
        } catch (LWJGLException e) {
            e.printStackTrace();
            return;
        }
        
        Renderer renderer = new Renderer();
        renderer.render();

        
        
    }
    
    
     
    
    
  
    
    
   
}
