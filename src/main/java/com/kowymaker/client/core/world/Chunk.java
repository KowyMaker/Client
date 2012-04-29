package com.kowymaker.client.core.world;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.kokakiwi.maths.generator.world.env.Parameter;
import com.kokakiwi.maths.generator.world.gen.Biome;
import com.kokakiwi.maths.generator.world.utils.MathsHelper;
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;
import com.kowymaker.spec.utils.debug.Debug;

public class Chunk implements IChild
{
    public final static int              width  = 16;
    public final static int              height = 16;
    
    private final World                  world;
    
    private final int                    x;
    private final int                    y;
    
    private final List<List<List<Tile>>> tiles  = new LinkedList<List<List<Tile>>>();
    
    public Chunk(World world, int x, int y)
    {
        this.world = world;
        this.x = x;
        this.y = y;
    }
    
    public void generate()
    {
        for (int x = 0; x < width; x++)
        {
            List<List<Tile>> range = new LinkedList<List<Tile>>();
            tiles.add(range);
            for (int y = 0; y < height; y++)
            {
                List<Tile> range2 = new LinkedList<Tile>();
                range.add(range2);
                
                double worldX = getWorldX() + x;
                double worldY = getWorldY() + y;
                
                Biome biome = world.getGenerator().getBiome(worldX, worldY);
                
                double height = world.getHeightmap().getValue(worldX, worldY) * 10;
                
                int numZ = (int) MathsHelper.supervise(Math.round(height) + 1,
                        1, 20);
                
                for (int z = 0; z < numZ; z++)
                {
                    Tile tile = new Tile(this, x, y, z, biome.getColor(worldX,
                            worldY));
                    range2.add(tile);
                }
                
                for (Parameter param : world.getGenerator().getEnvironment()
                        .getParameters().values())
                {
                    param.reset();
                }
            }
        }
    }
    
    public Tile getTileByWorldCoord(int x, int y)
    {
        return getTile(x % width, y % height);
    }
    
    public Tile getTile(int x, int y)
    {
        Tile tile = null;
        
        if (x >= 0 && x < tiles.size())
        {
            List<List<Tile>> range = tiles.get(x);
            if (y >= 0 && y < range.size())
            {
                List<Tile> range2 = range.get(y);
                int z = range2.size() - 1;
                
                tile = range2.get(z);
            }
        }
        
        return tile;
    }
    
    public World getWorld()
    {
        return world;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getWorldX()
    {
        return x * width;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getWorldY()
    {
        return y * height;
    }
    
    public void update(ClientEngine engine)
    {
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();
        
        for (int x = width - 1; x >= 0; x--)
        {
            List<List<Tile>> range = tiles.get(x);
            for (int y = height - 1; y >= 0; y--)
            {
                boolean hovered = false;
                List<Tile> range2 = range.get(y);
                for (int z = range2.size() - 1; z >= 0; z--)
                {
                    Tile tile = range2.get(z);
                    
                    if (!hovered && tile.contains(mouseX, mouseY))
                    {
                        tile.setHovered(true);
                        hovered = true;
                    }
                    else
                    {
                        tile.setHovered(false);
                    }
                    
                    tile.update(engine);
                }
            }
        }
    }
    
    public void render(ClientEngine engine)
    {
        for (int x = width - 1; x >= 0; x--)
        {
            List<List<Tile>> range = tiles.get(x);
            for (int y = height - 1; y >= 0; y--)
            {
                List<Tile> range2 = range.get(y);
                for (int z = 0; z < range2.size(); z++)
                {
                    Tile tile = range2.get(z);
                    tile.render(engine);
                }
            }
        }
    }
    
    public boolean contains(double x, double y)
    {
        return false;
    }
    
    @Override
    public String toString()
    {
        return "Chunk [x=" + x + ", y=" + y + "]";
    }
    
}
