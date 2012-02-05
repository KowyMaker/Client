package com.kowymaker.client.graphics.core;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

public class ClientApplet extends Applet
{
    private static final long  serialVersionUID = 2556163398694720499L;
    
    private final AWTGLCanvas  canvas;
    private final ClientEngine engine;
    private final Thread       thread;
    
    public ClientApplet(int width, int height) throws HeadlessException,
            LWJGLException
    {
        super();
        canvas = new AWTGLCanvas();
        canvas.setSize(width, height);
        
        engine = new ClientEngine(width, height);
        engine.setContext(canvas);
        
        thread = new Thread(engine);
        
        setBackground(Color.white);
        setLayout(new BorderLayout());
        
        add(canvas, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                engine.resize(canvas.getWidth(), canvas.getHeight());
            }
        });
    }
    
    @Override
    public void init()
    {
        
    }
    
    @Override
    public void start()
    {
        thread.start();
    }
    
    @Override
    public void stop()
    {
        engine.setRunning(false);
    }
    
    public AWTGLCanvas getCanvas()
    {
        return canvas;
    }
    
    public ClientEngine getEngine()
    {
        return engine;
    }
    
    public Thread getThread()
    {
        return thread;
    }
    
}
