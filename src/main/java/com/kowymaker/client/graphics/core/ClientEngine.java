package com.kowymaker.client.graphics.core;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class ClientEngine implements Runnable
{
    private int                width;
    private int                height;
    
    private Object             context    = null;
    private boolean            running    = false;
    private boolean            useDisplay = false;
    private boolean            updateSize = false;
    
    private final LWJGLOpenGL  gl         = new LWJGLOpenGL();
    
    private final List<IChild> childs     = new ArrayList<IChild>();
    
    public ClientEngine(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    
    public void run()
    {
        try
        {
            initGL();
            
            running = true;
            
            while (running)
            {
                loop();
            }
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }
    
    private void initGL() throws LWJGLException
    {
        if (context != null)
        {
            if (context instanceof Canvas)
            {
                Display.setParent((Canvas) context);
                
                Display.setVSyncEnabled(true);
                Display.create();
                
                useDisplay = true;
            }
            else
            {
                GLContext.useContext(context);
            }
        }
        
        gl.setProjectionMatrixMode();
        gl.loadIdentity();
        
        gl.setOrtho(0, width, 0, height, -1, 1);
        gl.setViewPort(0, 0, width, height);
        
        gl.setModelMatrixMode();
        gl.loadIdentity();
        
        gl.enableTexture2D(true);
    }
    
    private void loop()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        update();
        render();
        
        if (useDisplay)
        {
            Display.update();
        }
        
        if (updateSize)
        {
            gl.setProjectionMatrixMode();
            gl.loadIdentity();
            
            gl.setOrtho(0, width, 0, height, -1, 1);
            gl.setViewPort(0, 0, width, height);
            
            updateSize = false;
        }
        
        gl.setModelMatrixMode();
        gl.loadIdentity();
    }
    
    private void update()
    {
        for (IChild child : childs)
        {
            child.update(this);
        }
    }
    
    private void render()
    {
        for (IChild child : childs)
        {
            child.render(this);
        }
    }
    
    public void addChild(IChild child)
    {
        childs.add(child);
    }
    
    public List<IChild> getChilds()
    {
        return childs;
    }
    
    public boolean isRunning()
    {
        return running;
    }
    
    public void setRunning(boolean running)
    {
        this.running = running;
    }
    
    public Object getContext()
    {
        return context;
    }
    
    public void setContext(Object context)
    {
        this.context = context;
    }
    
    public LWJGLOpenGL getGl()
    {
        return gl;
    }
    
    public void resize(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        updateSize = true;
    }
    
}
