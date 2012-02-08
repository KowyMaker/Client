package com.kowymaker.client;

import java.io.File;

import org.lwjgl.input.Keyboard;

import com.kowymaker.client.core.world.World;
import com.kowymaker.client.demo.WorldDemo;
import com.kowymaker.client.graphics.ClientWindow;
import com.kowymaker.spec.console.ConsoleOutputManager;
import com.kowymaker.spec.utils.Configuration;
import com.kowymaker.spec.utils.SystemUtils;

public class KowyMakerClient
{
    private final Configuration config = new Configuration();
    private final ClientWindow  window;
    
    public KowyMakerClient() throws Exception
    {
        ConsoleOutputManager.register("client");
        
        // Init LWJGL
        File nativesDir = new File("natives" + File.separator
                + SystemUtils.getSystemOS().name());
        System.setProperty("org.lwjgl.librarypath",
                nativesDir.getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath",
                nativesDir.getAbsolutePath());
        
        // Load config
        try
        {
            config.load(KowyMakerClient.class
                    .getResourceAsStream("/config/config.yml"), "yaml");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // Load Window
        window = new ClientWindow(this);
        
        World world = new World(this);
        WorldDemo demo = new WorldDemo(this, world);
        window.getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_UP, demo);
        window.getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_LEFT, demo);
        window.getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_DOWN, demo);
        window.getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_RIGHT, demo);
        
        window.getApplet().getEngine().addChild(world);
    }
    
    public void start()
    {
        // Start Window
        window.start();
    }
    
    public void stop()
    {
        // Stop Window
        window.stop();
        
        System.exit(0);
    }
    
    public Configuration getConfig()
    {
        return config;
    }
    
    public ClientWindow getWindow()
    {
        return window;
    }
    
    public static void main(String[] args)
    {
        try
        {
            KowyMakerClient main = new KowyMakerClient();
            main.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
