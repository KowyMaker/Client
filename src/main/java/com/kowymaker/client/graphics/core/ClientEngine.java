package com.kowymaker.client.graphics.core;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class ClientEngine implements Runnable
{
    private Object                    context;
    private boolean                   useDisplay    = false;
    private boolean                   running       = false;
    private boolean                   updateDisplay = false;
    
    private final LWJGLOpenGL         gl            = new LWJGLOpenGL();
    
    private final EngineConfiguration configuration = new EngineConfiguration();
    
    private final List<IChild>        childs        = new ArrayList<IChild>();
    
    public Object getContext()
    {
        return context;
    }
    
    public void setContext(Object context)
    {
        this.context = context;
    }
    
    public boolean isUseDisplay()
    {
        return useDisplay;
    }
    
    public void setUseDisplay(boolean useDisplay)
    {
        this.useDisplay = useDisplay;
    }
    
    public boolean isRunning()
    {
        return running;
    }
    
    public void setRunning(boolean running)
    {
        this.running = running;
    }
    
    public LWJGLOpenGL getGl()
    {
        return gl;
    }
    
    public EngineConfiguration getConfiguration()
    {
        return configuration;
    }
    
    public boolean isUpdateDisplay()
    {
        return updateDisplay;
    }
    
    public void setUpdateDisplay(boolean updateDisplay)
    {
        this.updateDisplay = updateDisplay;
    }
    
    public void addChild(IChild child)
    {
        if (!childs.contains(child))
        {
            childs.add(child);
        }
    }
    
    public void removeChild(int index)
    {
        childs.remove(index);
    }
    
    public void removeChild(IChild child)
    {
        childs.remove(child);
    }
    
    public List<IChild> getChilds()
    {
        return childs;
    }
    
    public void resize(int width, int height)
    {
        configuration.setWidth(width);
        configuration.setHeight(height);
        
        updateDisplay = true;
    }
    
    public void run()
    {
        initGL();
        running = true;
        while (running)
        {
            loop();
        }
    }
    
    public void loop()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        update();
        render();
        
        if (useDisplay)
        {
            Display.update();
        }
        
        EngineConfiguration.clear();
        
        if (useDisplay)
        {
            Display.sync(configuration.getFps());
        }
    }
    
    public void initGL()
    {
        try
        {
            if (context instanceof Canvas)
            {
                Display.setParent((Canvas) context);
                Canvas canvas = (Canvas) context;
                configuration.setWidth(canvas.getWidth());
                configuration.setHeight(canvas.getHeight());
                
                Display.setDisplayMode(new DisplayMode(
                        configuration.getWidth(), configuration.getHeight()));
                Display.setVSyncEnabled(true);
                Display.create();
                useDisplay = true;
            }
            else
            {
                GLContext.useContext(context);
            }
            
            configuration.apply();
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void update()
    {
        if (updateDisplay)
        {
            configuration.apply();
            updateDisplay = false;
        }
        
        for (IChild child : childs)
        {
            child.update(this);
        }
    }
    
    public void render()
    {
        for (IChild child : childs)
        {
            child.render(this);
        }
    }
    
}
