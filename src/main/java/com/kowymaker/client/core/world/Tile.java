package com.kowymaker.client.core.world;

import org.fenggui.binding.render.Graphics;
import org.fenggui.util.Color;

import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Tile implements IChild
{
    public final static int width   = 60;
    public final static int height  = 30;
    public final static int depth   = 10;
    
    private final Chunk     chunk;
    
    private final int       x;
    private final int       y;
    private final int       z;
    
    private final Color     color;
    private boolean         visible = true;
    
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
        World.tile++;
        
        visible = isVisible();
    }
    
    public void render(ClientEngine engine)
    {
        if (visible)
        {
            int calcX = getWorldX() - chunk.getWorld().getOffsetX();
            int calcY = getWorldY() - chunk.getWorld().getOffsetY();
            
            int renderX = ((calcX - calcY) * width / 2)
                    + (engine.getConfig().getWidth() / 2) - (width / 2);
            int renderY = ((calcX + calcY) * height / 2) + (depth * z)
                    + (engine.getConfig().getHeight() / 2) - (height / 2);
            
            int left = -2 * width;
            int bottom = -2 * height;
            int right = engine.getConfig().getWidth() + (2 * width);
            int up = engine.getConfig().getHeight() + (2 * height);
            
            if (renderX >= left && renderX <= right && renderY >= bottom
                    && renderY <= up)
            {
                Graphics g = engine.getBinding().getGraphics();
                engine.getGl().color(color);
                
                if (getWorldX() == chunk.getWorld().getOffsetX()
                        && getWorldY() == chunk.getWorld().getOffsetY())
                {
                    engine.getGl().color(Color.WHITE.darker().darker());
                }
                
                g.drawImage(chunk.getWorld().getTexture(), renderX,
                        renderY - 10);
            }
        }
    }
    
    public boolean isVisible()
    {
        boolean visible = true;
        
        Tile first = chunk.getWorld().getTileByWorldCoord(getWorldX() - 1,
                getWorldY());
        
        Tile second = chunk.getWorld().getTileByWorldCoord(getWorldX(),
                getWorldY() - 1);
        
        if (first.getZ() > z && second.getZ() > z
                && chunk.getTile(x, y) != this)
        {
            visible = false;
        }
        
        return visible;
    }
    
    public boolean contains(double x, double y)
    {
        return false;
    }
    
}