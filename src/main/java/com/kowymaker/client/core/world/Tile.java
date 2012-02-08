package com.kowymaker.client.core.world;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.fenggui.util.Color;

import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Tile implements IChild
{
    public final static int width  = 60;
    public final static int height = 30;
    public final static int depth  = 10;
    
    private final Chunk     chunk;
    
    private final int       x;
    private final int       y;
    private final int       z;
    
    private final Color     color;
    
    public Tile(Chunk chunk, int x, int y, int z, java.awt.Color color)
    {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.color = new Color(color);
    }
    
    public Chunk getChunk()
    {
        return chunk;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getWorldX()
    {
        return chunk.getWorldX() + x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getWorldY()
    {
        return chunk.getWorldY() + y;
    }
    
    public int getZ()
    {
        return z;
    }
    
    public void update(ClientEngine engine)
    {
        
    }
    
    public void render(ClientEngine engine)
    {
        int calcX = getWorldX() - chunk.getWorld().getOffsetX();
        int calcY = getWorldY() - chunk.getWorld().getOffsetY();
        
        if (getWorldX() == chunk.getWorld().getOffsetX()
                && getWorldY() == chunk.getWorld().getOffsetY())
        {
            engine.getGl().color(Color.WHITE);
        }
        
        int renderX = ((calcX - calcY) * width / 2) + (chunk.getWorld().getMain().getWindow().getApplet().getCanvas().getWidth() / 2);
        int renderY = ((calcX + calcY) * height / 2) + (depth * z) + (chunk.getWorld().getMain().getWindow().getApplet().getCanvas().getHeight() / 2);
        
        Graphics g = engine.getBinding().getGraphics();
        engine.getGl().color(color);
        g.drawImage(chunk.getWorld().getTexture(), renderX, renderY - 10);
    }
    
    public boolean contains(double x, double y)
    {
        return false;
    }
    
}
