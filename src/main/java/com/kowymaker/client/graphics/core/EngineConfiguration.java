package com.kowymaker.client.graphics.core;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class EngineConfiguration
{
    private double  originX      = 0.0;
    private double  originY      = 0.0;
    private boolean centerOrigin = false;
    
    private int     width        = 800;
    private int     height       = 600;
    
    private int     fps          = 60;
    private Color   background   = Color.black;
    
    public double getOriginX()
    {
        return originX;
    }
    
    public void setOriginX(double originX)
    {
        this.originX = originX;
    }
    
    public double getOriginY()
    {
        return originY;
    }
    
    public void setOriginY(double originY)
    {
        this.originY = originY;
    }
    
    public boolean isCenteredOrigin()
    {
        return centerOrigin;
    }
    
    public void setCenterOrigin(boolean centerOrigin)
    {
        this.centerOrigin = centerOrigin;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public int getFps()
    {
        return fps;
    }
    
    public void setFps(int fps)
    {
        this.fps = fps;
    }
    
    public Color getBackground()
    {
        return background;
    }
    
    public void setBackground(Color background)
    {
        this.background = background;
    }
    
    public void apply()
    {
        // GL_PROJECTION
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        
        //Positioning origin
        double top = -originY;
        double left = -originX;
        
        double right = width - originX;
        double bottom = height - originY;
        
        if(centerOrigin)
        {
            top = -((height / 2) + originY);
            left = -((width / 2) + originX);
            
            right = (width / 2) - originX;
            bottom = (height / 2) - originY;
        }
        
        GL11.glOrtho(left, right, bottom, top, -1, 1);
        
        GL11.glViewport(0, 0, width, height);
        
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
