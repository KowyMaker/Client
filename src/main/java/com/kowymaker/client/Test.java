package com.kowymaker.client;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;

import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Test implements IChild
{
    
    public void render(ClientEngine engine)
    {
        LWJGLOpenGL gl = new LWJGLOpenGL();
        
        float x = -50.0f;
        float y = -50.0f;
        float width = 100.0f;
        float height = 100.0f;
        
        gl.startQuads();
        
        gl.color(1.0f, 1.0f, 0.0f, 1.0f);
        gl.vertex(x, y);
        gl.color(1.0f, 0.0f, 1.0f, 1.0f);
        gl.vertex(x, y + height);
        gl.color(0.0f, 1.0f, 1.0f, 1.0f);
        gl.vertex(x + width, y + height);
        gl.color(0.5f, 0.2f, 0.8f, 1.0f);
        gl.vertex(x + width, y);
        
        gl.end();
    }
    
    public void update(ClientEngine engine)
    {
        
    }
    
}
