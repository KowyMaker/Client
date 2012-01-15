package com.kowymaker.client.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientEngine;

public class ClientWindow extends JFrame
{
    private static final long     serialVersionUID = 1111824300511792902L;
    
    private final KowyMakerClient client;
    
    private final AWTGLCanvas     canvas;
    private final ClientEngine    engine           = new ClientEngine();
    private final Thread          thread;
    
    public ClientWindow(final KowyMakerClient client) throws HeadlessException,
            LWJGLException
    {
        // Init window & variables
        super();
        
        canvas = new AWTGLCanvas();
        this.client = client;
        engine.setContext(canvas);
        thread = new Thread(engine);
        
        // Init display
        canvas.setSize(client.getConfiguration().getInteger("graphics.width"),
                client.getConfiguration().getInteger("graphics.height"));
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(canvas, BorderLayout.CENTER);
        
        setTitle(client.getConfiguration().getString("game.name"));
        
        getContentPane().setPreferredSize(
                new Dimension(client.getConfiguration().getInteger(
                        "graphics.width"), client.getConfiguration()
                        .getInteger("graphics.height")));
        pack();
        setLocationRelativeTo(null);
        
        // Init listeners
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e)
            {
                client.stop();
            }
            
        });
        
        getContentPane().addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentResized(ComponentEvent e)
            {
                engine.resize(canvas.getWidth(), canvas.getHeight());
            }
            
        });
    }
    
    public KowyMakerClient getClient()
    {
        return client;
    }
    
    public Canvas getCanvas()
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
    
    @Override
    public void setVisible(boolean b)
    {
        if (b)
        {
            thread.start();
        }
        else
        {
            engine.setRunning(false);
        }
        super.setVisible(b);
    }
}
