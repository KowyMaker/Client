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
package com.kowymaker.client.graphics;

import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.engine.Renderer;

/**
 * @author Koka El Kiwi
 * 
 */
public class GameWindow
{
    private final KowyMakerClient main;
    private final Renderer        renderer;
    private final Thread          renderThread;
    
    private final GameFrame       frame;
    
    public GameWindow(KowyMakerClient main)
    {
        this.main = main;
        renderer = new Renderer(main);
        renderThread = new Thread(renderer);
        frame = new GameFrame(this);
    }
    
    public void init()
    {
        renderer.setCanvas(frame.getCanvas());
    }
    
    public void open()
    {
        frame.setVisible(true);
        renderThread.start();
    }
    
    public void close()
    {
        renderer.setRunning(false);
        frame.setVisible(false);
    }
    
    public Thread getRenderThread()
    {
        return renderThread;
    }
    
    public Renderer getRenderer()
    {
        return renderer;
    }
    
    public KowyMakerClient getMain()
    {
        return main;
    }
}
