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
package com.kowymaker.client.graphics.engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.spec.utils.SystemUtils;
import com.kowymaker.spec.utils.SystemUtils.OS;

/**
 * @author Koka El Kiwi
 * 
 */
public class Renderer implements Runnable
{
    private final KowyMakerClient main;
    private boolean               running   = false;
    private Canvas                canvas    = null;
    private Dimension             dimension = null;
    private final File            nativesDir;
    private LWJGLOpenGL           gl;
    
    public Renderer(KowyMakerClient main)
    {
        this.main = main;
        OS os = SystemUtils.getSystemOS();
        if (os == OS.unknown)
        {
            throw new Error("Unknow Operating System. Can't setup Renderer.");
        }
        
        nativesDir = new File("natives" + File.separator + os.name());
        System.setProperty("org.lwjgl.librarypath",
                nativesDir.getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath",
                nativesDir.getAbsolutePath());
    }
    
    /**
     * Run the renderer.
     */
    @Override
    public void run()
    {
        running = true;
        try
        {
            initDisplay();
            initGL();
            
            while (running)
            {
                
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                        | GL11.GL_DEPTH_BUFFER_BIT);
                
                update();
                render();
                
                Display.update();
                
                RenderConfig.clear();
                
                Display.sync(RenderConfig.fps);
            }
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize the Display window.
     * 
     * @throws LWJGLException
     */
    private void initDisplay() throws LWJGLException
    {
        //Apply config
        RenderConfig.width = main.getConfiguration().getInteger(
                "graphics.width");
        RenderConfig.height = main.getConfiguration().getInteger(
                "graphics.height");
        
        //Create window
        Display.setParent(canvas);
        Display.setDisplayMode(new DisplayMode(RenderConfig.width,
                RenderConfig.height));
        Display.setVSyncEnabled(true);
        Display.create();
    }
    
    /**
     * Setup OpenGL
     */
    private void initGL()
    {
        gl = new LWJGLOpenGL();
        RenderConfig.apply();
    }
    
    /**
     * Update all elements
     * 
     * @throws LWJGLException
     */
    private void update() throws LWJGLException
    {
        //Update elements
        
        //Update window
        if (dimension != null)
        {
            gl.setViewPort(0, 0, dimension.width, dimension.height);
            dimension = null;
        }
    }
    
    /**
     * Render all elements.
     */
    private void render()
    {
        gl.startQuads();
        
        gl.color(0.5f, 0.5f, 0.3f, 1.0f);
        gl.vertex(30.0f, 30.0f);
        gl.color(0.2f, 0.5f, 0.3f, 0.8f);
        gl.vertex(600.0f, 30.0f);
        gl.color(0.5f, 0.2f, 0.3f, 0.6f);
        gl.vertex(600.0f, 600.0f);
        gl.color(0.5f, 0.5f, 0.1f, 0.8f);
        gl.vertex(30.0f, 600.0f);
        
        gl.end();
    }
    
    public Dimension getDimension()
    {
        return dimension;
    }
    
    public void setDimension(Dimension dimension)
    {
        this.dimension = dimension;
    }
    
    public boolean isRunning()
    {
        return running;
    }
    
    public void setRunning(boolean running)
    {
        this.running = running;
    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }
    
    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }
    
    public File getNativesDir()
    {
        return nativesDir;
    }
    
    public KowyMakerClient getMain()
    {
        return main;
    }
    
}
