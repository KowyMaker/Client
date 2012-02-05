package com.kowymaker.client.demo;

import java.util.ArrayList;
import java.util.List;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.fenggui.util.Color;

import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Demo implements IChild
{
    private final List<Tile> tiles = new ArrayList<Tile>();
    
    public Demo()
    {
        for (int x = 0; x < 30; x++)
        {
            int startY = (x % 2 == 0) ? 0 : 1;
            for (int y = startY; y < 38; y += 2)
            {
                int tileX = x * (Tile.width / 2);
                int tileY = y * (Tile.height / 2);
                
                tiles.add(new Tile(tileX, tileY));
            }
        }
        
        System.out.println(tiles.size() + " tiles created.");
    }
    
    public void update(ClientEngine engine)
    {
        for (Tile tile : tiles)
        {
            tile.update(engine);
        }
    }
    
    public void render(ClientEngine engine)
    {
        for (Tile tile : tiles)
        {
            tile.render(engine);
        }
    }
    
    public static class Tile implements IChild
    {
        public final static int width  = 60;
        public final static int height = 30;
        
        private final int       x;
        private final int       y;
        private final Color     color;
        
        public Tile(int x, int y)
        {
            this.x = x;
            this.y = y;
            
            color = Color.random();
        }
        
        public void update(ClientEngine engine)
        {
            
        }
        
        public void render(ClientEngine engine)
        {
            LWJGLOpenGL gl = engine.getGl();
            
            gl.startQuads();
            
            gl.color(color);
            gl.vertex(x + (width / 2), y + height);
            gl.vertex(x + width, y + (height / 2));
            gl.vertex(x + (width / 2), y);
            gl.vertex(x, y + (height / 2));
            
            gl.end();
        }
    }
}
