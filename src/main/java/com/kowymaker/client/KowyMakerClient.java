package com.kowymaker.client;

import java.io.File;


import com.kowymaker.client.graphics.ClientWindow;
import com.kowymaker.spec.console.ConsoleOutputManager;
import com.kowymaker.spec.utils.Configuration;
import com.kowymaker.spec.utils.SystemUtils;

public class KowyMakerClient
{
    private final Configuration configuration = new Configuration();
    
    private final ClientWindow  window;
    
    public KowyMakerClient() throws Exception
    {
        ConsoleOutputManager.register("client");
        
        try
        {
            configuration.load(KowyMakerClient.class
                    .getResourceAsStream("/config/config.yml"), "yaml");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        File nativesDir = new File("natives" + File.separator
                + SystemUtils.getSystemOS().name());
        System.setProperty("org.lwjgl.librarypath",
                nativesDir.getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath",
                nativesDir.getAbsolutePath());
        
        window = new ClientWindow(this);
        window.getEngine().getConfiguration().setCenterOrigin(true);
        
        Test test = new Test();
        window.getEngine().addChild(test);
    }
    
    public Configuration getConfiguration()
    {
        return configuration;
    }
    
    public void start()
    {
        window.setVisible(true);
    }
    
    public void stop()
    {
        window.setVisible(false);
        System.exit(0);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            KowyMakerClient client = new KowyMakerClient();
            client.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
