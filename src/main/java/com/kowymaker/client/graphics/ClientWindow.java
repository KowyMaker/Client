package com.kowymaker.client.graphics;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;

import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientApplet;

public class ClientWindow extends Frame
{
    private static final long     serialVersionUID = 1111824300511792902L;
    
    private final KowyMakerClient main;
    private final ClientApplet    applet;
    
    public ClientWindow(final KowyMakerClient main) throws HeadlessException,
            LWJGLException
    {
        super(main.getConfig().getString("game.name"));
        this.main = main;
        applet = new ClientApplet(
                main.getConfig().getInteger("graphics.width"), main.getConfig()
                        .getInteger("graphics.height"));
        
        try
        {
            setIconImage(ImageIO.read(ClientWindow.class
                    .getResourceAsStream("/res/icon.png")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                main.stop();
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e)
            {
                applet.requestFocusInWindow();
            }
        });
        
        add(applet, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public void start()
    {
        applet.start();
        setVisible(true);
    }
    
    public void stop()
    {
        setVisible(false);
        applet.stop();
    }
    
    public KowyMakerClient getMain()
    {
        return main;
    }
    
    public ClientApplet getApplet()
    {
        return applet;
    }
}
