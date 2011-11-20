package com.kowymaker.client.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientEngine;

public class ClientWindow extends Frame
{
    private static final long     serialVersionUID = 1111824300511792902L;
    
    private final KowyMakerClient client;
    
    private final Canvas          canvas           = new Canvas();
    private final ClientEngine    engine           = new ClientEngine();
    private final Thread          thread;
    
    public ClientWindow(final KowyMakerClient client) throws HeadlessException
    {
        // Init window & variables
        super();
        this.client = client;
        engine.setContext(canvas);
        thread = new Thread(engine);
        
        // Init display
        canvas.setSize(client.getConfiguration().getInteger("graphics.width"),
                client.getConfiguration().getInteger("graphics.height"));
        
        setLayout(null);
        add(canvas);
        
        setTitle(client.getConfiguration().getString("game.name"));
        
        setPreferredSize(new Dimension(client.getConfiguration().getInteger(
                "graphics.width"), client.getConfiguration().getInteger(
                "graphics.height")));
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
        
        addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentResized(ComponentEvent e)
            {
                engine.resize(getWidth(), getHeight());
                canvas.setSize(getSize());
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
