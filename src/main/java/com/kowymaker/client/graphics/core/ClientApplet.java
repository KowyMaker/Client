package com.kowymaker.client.graphics.core;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.GL11;

import com.kowymaker.client.graphics.core.event.EventManager;
import com.kowymaker.client.graphics.core.ui.UserInterface;

public class ClientApplet extends Applet
{
    private static final long   serialVersionUID = 2556163398694720499L;
    
    private final AWTGLCanvas   canvas;
    
    private final ClientEngine  engine;
    private final Thread        thread;
    
    private final UserInterface ui;
    
    private final EventManager  eventManager;
    
    public ClientApplet(int width, int height) throws HeadlessException,
            LWJGLException
    {
        super();
        canvas = new AWTGLCanvas();
        canvas.setSize(width, height);
        eventManager = new EventManager();
        
        engine = new ClientEngine(
                new ClientEngine.Configuration(width, height), eventManager);
        engine.setContext(canvas);
        
        thread = new Thread(engine);
        
        ui = new UserInterface(this);
        engine.addChild(ui, 0);
        
        setBackground(Color.black);
        setLayout(new BorderLayout());
        
        add(canvas, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                engine.resize(canvas.getWidth(), canvas.getHeight());
                ui.resize(canvas.getWidth(), canvas.getHeight());
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                canvas.requestFocusInWindow();
            }
        });
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
    
    public BufferedImage takeScreenShot()
    {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, buffer);
        
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16)
                        | (g << 8) | b);
            }
        
        return image;
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
    
    public UserInterface getUi()
    {
        return ui;
    }
    
    public EventManager getEventManager()
    {
        return eventManager;
    }
    
}
