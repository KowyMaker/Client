package com.kowymaker.client.graphics.core;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.fenggui.util.Color;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.kowymaker.client.graphics.core.event.EventManager;

public class ClientEngine implements Runnable
{
    private final Configuration config;
    private final EventManager  eventManager;
    
    private final FPS           fps        = new FPS();
    
    private Object              context    = null;
    private boolean             running    = false;
    private boolean             useDisplay = false;
    private boolean             updateSize = false;
    
    private LWJGLBinding        binding    = null;
    private LWJGLOpenGL         gl         = null;
    
    private final List<IChild>  childs     = new ArrayList<IChild>();
    
    public ClientEngine(Configuration config, EventManager eventManager)
    {
        this.config = config;
        this.eventManager = eventManager;
    }
    
    public void run()
    {
        try
        {
            initGL();
            
            running = true;
            fps.start();
            
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
                
                Mouse.create();
                Keyboard.create();
                
                useDisplay = true;
            }
            else
            {
                GLContext.useContext(context);
            }
        }
        
        binding = new LWJGLBinding();
        gl = (LWJGLOpenGL) binding.getOpenGL();
        
        // PROJECTION
        gl.setProjectionMatrixMode();
        gl.loadIdentity();
        
        gl.setOrtho(0, config.width, 0, config.height, -1, 1);
        gl.setViewPort(0, 0, config.width, config.height);
        
        gl.clearColor(config.background);
        
        // MODELVIEW
        gl.setModelMatrixMode();
        gl.loadIdentity();
        
        gl.enableTexture2D(true);
        gl.setTexEnvModeModulate();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
            
            gl.setOrtho(0, config.width, 0, config.height, -1, 1);
            gl.setViewPort(0, 0, config.width, config.height);
            
            updateSize = false;
        }
        
        gl.setModelMatrixMode();
        gl.loadIdentity();
    }
    
    private void update()
    {
        eventManager.update();
        fps.update();
        
        for (int index = childs.size() - 1; index >= 0; index--)
        {
            IChild child = childs.get(index);
            
            child.update(this);
        }
    }
    
    private void render()
    {
        for (int index = childs.size() - 1; index >= 0; index--)
        {
            IChild child = childs.get(index);
            
            child.render(this);
        }
        
        Graphics g = binding.getGraphics();
        
        g.setFont(ImageFont.getDefaultFont());
        gl.color(Color.WHITE);
        g.drawString("FPS: " + fps.getFps(), 0, 0);
    }
    
    public void addChild(IChild child)
    {
        childs.add(child);
    }
    
    public void addChild(IChild child, int index)
    {
        childs.add(index, child);
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
    
    public Configuration getConfig()
    {
        return config;
    }
    
    public LWJGLBinding getBinding()
    {
        return binding;
    }
    
    public LWJGLOpenGL getGl()
    {
        return gl;
    }
    
    public EventManager getEventManager()
    {
        return eventManager;
    }
    
    public FPS getFps()
    {
        return fps;
    }
    
    public void resize(int width, int height)
    {
        config.width = width;
        config.height = height;
        
        updateSize = true;
    }
    
    public static class Configuration
    {
        private int   width;
        private int   height;
        private Color background;
        
        public Configuration(int width, int height)
        {
            this(width, height, Color.BLACK);
        }
        
        public Configuration(int width, int height, Color background)
        {
            this.width = width;
            this.height = height;
            this.background = background;
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
        
        public Color getBackground()
        {
            return background;
        }
        
        public void setBackground(Color background)
        {
            this.background = background;
        }
    }
    
    public static class FPS
    {
        
        public int  current = 0;
        public int  fps     = 0;
        public long start   = 0;
        
        public long getTime()
        {
            return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        }
        
        public void start()
        {
            start = getTime();
        }
        
        public void update()
        {
            current++;
            long time = getTime();
            if (time - start >= 1000)
            {
                fps = current;
                current = 0;
                start = time;
            }
        }
        
        public int getFps()
        {
            return fps;
        }
    }
    
}
