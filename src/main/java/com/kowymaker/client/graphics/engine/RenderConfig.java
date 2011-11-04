package com.kowymaker.client.graphics.engine;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderConfig
{
    // Perspective
    public static float   fovy        = 70.0f;
    public static float   zNear       = -0.1f;
    public static float   zFar        = 100.0f;
    
    // Ortho
    public static int     width       = 800;
    public static int     height      = 600;
    public static double  ozNear      = -1;
    public static double  ozFar       = 1;
    
    // Render config
    public static boolean perspective = false;
    public static int     fps         = 60;
    public static Color   background  = Color.black;
    
    public static void apply()
    {
        // GL_PROJECTION
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        if (perspective)
        {
            GLU.gluPerspective(fovy, (float) (width / height), zNear, zFar);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        else
        {
            GL11.glOrtho(0, width, height, 0, ozNear, ozFar);
            GL11.glViewport(0, 0, width, height);
        }
        
        // Clear color
        GL11.glClearColor(background.getRed() / 255,
                background.getGreen() / 255, background.getBlue() / 255,
                background.getAlpha() / 255);
        
        // GL_MODELVIEW
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        // Enable textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
    }
    
    public static void clear()
    {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }
}
