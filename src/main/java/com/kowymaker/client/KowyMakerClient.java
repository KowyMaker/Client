/**
 *  This file is part of Kowy Maker - Client.
 *
 *  Kowy Maker - Client is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Kowy Maker - Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Kowy Maker - Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kowymaker.client;

import com.kowymaker.client.graphics.GameWindow;
import com.kowymaker.spec.console.ConsoleOutputManager;
import com.kowymaker.spec.utils.Configuration;

public class KowyMakerClient
{
    private final Configuration configuration = new Configuration();
    private final GameWindow    window;
    
    /**
     * Initialize variables and resources.
     */
    public KowyMakerClient()
    {
        ConsoleOutputManager.register("client");
        
        // Initialize configuration
        try
        {
            configuration.load(KowyMakerClient.class
                    .getResourceAsStream("/config/config.yml"), "yaml");
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        
        // Load resources
        
        // Initialize window
        window = new GameWindow(this);
        window.init();
        
        // Initialize interface
        
    }
    
    /**
     * Start Kowy Maker.
     */
    public void start()
    {
        //Show window
        window.open();
    }
    
    /**
     * Stop Kowy Maker.
     */
    public void stop()
    {
        window.close();
        System.exit(0);
    }
    
    /**
     * Get Client configuration.
     * 
     * @return Client configuration
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }
    
    public static void main(String[] args)
    {
        final KowyMakerClient client = new KowyMakerClient();
        client.start();
    }
    
}
