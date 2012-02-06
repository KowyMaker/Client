package com.kowymaker.client.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.fenggui.event.key.KeyAdapter;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.util.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kokakiwi.maths.generator.sample.biomes.DesertBiome;
import com.kokakiwi.maths.generator.sample.biomes.ForestBiome;
import com.kokakiwi.maths.generator.sample.biomes.LakeBiome;
import com.kokakiwi.maths.generator.sample.biomes.MountainBiome;
import com.kokakiwi.maths.generator.sample.biomes.OceanBiome;
import com.kokakiwi.maths.generator.sample.biomes.PlainBiome;
import com.kokakiwi.maths.generator.sample.biomes.RiverBiome;
import com.kokakiwi.maths.generator.sample.biomes.SnowBiome;
import com.kokakiwi.maths.generator.sample.biomes.VolcanoBiome;
import com.kokakiwi.maths.generator.sample.params.HeightMap;
import com.kokakiwi.maths.generator.sample.params.Oasis;
import com.kokakiwi.maths.generator.sample.params.Rivers;
import com.kokakiwi.maths.generator.sample.params.Snow;
import com.kokakiwi.maths.generator.sample.params.Temperature;
import com.kokakiwi.maths.generator.sample.params.Volcano;
import com.kokakiwi.maths.generator.world.WorldGenerator;
import com.kokakiwi.maths.generator.world.gen.Biome;
import com.kokakiwi.maths.generator.world.utils.MathsHelper;
import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Demo implements IChild
{
    private final List<List<Tile>> tiles   = new LinkedList<List<Tile>>();
    private final WorldGenerator   generator;
    private final HeightMap        heightmap;
    
    private int                    baseX   = 0;
    private int                    baseY   = 0;
    
    private final static int       TIMEOUT = 500;
    
    public Demo(final KowyMakerClient main)
    {
        generator = new WorldGenerator("hoy");
        // On inscrit les biomes
        generator.registerBiome(OceanBiome.class);
        generator.registerBiome(SnowBiome.class);
        generator.registerBiome(RiverBiome.class);
        generator.registerBiome(LakeBiome.class);
        generator.registerBiome(VolcanoBiome.class);
        generator.registerBiome(DesertBiome.class);
        generator.registerBiome(ForestBiome.class);
        generator.registerBiome(PlainBiome.class);
        generator.registerBiome(MountainBiome.class);
        
        // On inscrit les variables d'environnement
        generator.getEnvironment().registerParameter(HeightMap.class);
        generator.getEnvironment().registerParameter(Rivers.class);
        generator.getEnvironment().registerParameter(Temperature.class);
        generator.getEnvironment().registerParameter(Oasis.class);
        generator.getEnvironment().registerParameter(Volcano.class);
        generator.getEnvironment().registerParameter(Snow.class);
        
        heightmap = generator.getEnvironment().getParameter(HeightMap.class);
        
        create();
        
        main.getWindow().getApplet().getEngine().getEventManager()
                .registerKeyListener(Keyboard.KEY_F2, new KeyAdapter() {
                    
                    @Override
                    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                    {
                        System.out.println("Screenshot.");
                        
                        BufferedImage image = main.getWindow().getApplet()
                                .takeScreenShot();
                        String msg = "FPS: "
                                + main.getWindow().getApplet().getEngine()
                                        .getFps().getFps();
                        System.out.println(msg);
                        
                        try
                        {
                            ImageIO.write(image, "png", new File(
                                    "screenshot.png"));
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    
                });
        
        main.getWindow().getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_UP, new KeyAdapter() {
                    private boolean pressed = false;
                    private long    last;
                    
                    @Override
                    public void keyPressed(KeyPressedEvent keyPressedEvent)
                    {
                        if (!pressed)
                        {
                            last = System.currentTimeMillis();
                            pressed = true;
                        }
                        
                        long current = System.currentTimeMillis();
                        if (current - last > TIMEOUT)
                        {
                            modify();
                        }
                    }
                    
                    @Override
                    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                    {
                        pressed = false;
                        modify();
                    }
                    
                    private void modify()
                    {
                        baseY += Tile.height;
                        create();
                    }
                    
                });
        main.getWindow().getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_LEFT, new KeyAdapter() {
                    
                    private boolean pressed = false;
                    private long    last;
                    
                    @Override
                    public void keyPressed(KeyPressedEvent keyPressedEvent)
                    {
                        if (!pressed)
                        {
                            last = System.currentTimeMillis();
                            pressed = true;
                        }
                        
                        long current = System.currentTimeMillis();
                        if (current - last > TIMEOUT)
                        {
                            modify();
                        }
                    }
                    
                    @Override
                    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                    {
                        pressed = false;
                        modify();
                    }
                    
                    private void modify()
                    {
                        baseX -= Tile.width;
                        create();
                    }
                    
                });
        main.getWindow().getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_RIGHT, new KeyAdapter() {
                    
                    private boolean pressed = false;
                    private long    last;
                    
                    @Override
                    public void keyPressed(KeyPressedEvent keyPressedEvent)
                    {
                        if (!pressed)
                        {
                            last = System.currentTimeMillis();
                            pressed = true;
                        }
                        
                        long current = System.currentTimeMillis();
                        if (current - last > TIMEOUT)
                        {
                            modify();
                        }
                    }
                    
                    @Override
                    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                    {
                        pressed = false;
                        modify();
                    }
                    
                    private void modify()
                    {
                        baseX += Tile.width;
                        create();
                    }
                    
                });
        main.getWindow().getApplet().getEventManager()
                .registerKeyListener(Keyboard.KEY_DOWN, new KeyAdapter() {
                    
                    private boolean pressed = false;
                    private long    last;
                    
                    @Override
                    public void keyPressed(KeyPressedEvent keyPressedEvent)
                    {
                        if (!pressed)
                        {
                            last = System.currentTimeMillis();
                            pressed = true;
                        }
                        
                        long current = System.currentTimeMillis();
                        if (current - last > TIMEOUT)
                        {
                            modify();
                        }
                    }
                    
                    @Override
                    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
                    {
                        pressed = false;
                        modify();
                    }
                    
                    private void modify()
                    {
                        baseY -= Tile.height;
                        create();
                    }
                    
                });
    }
    
    private void create()
    {
        tiles.clear();
        
        for (int y = 57; y >= 0; y--)
        {
            int startX = (y % 2 == 0) ? 0 : 1;
            List<Tile> range = new LinkedList<Tile>();
            for (int x = startX; x < 47; x += 2)
            {
                
                heightmap.reset();
                
                int tileX = x * (Tile.width / 2);
                int tileY = y * (Tile.height / 2);
                
                double worldX = (tileX + baseX) * 0.5;
                double worldY = (tileY + baseY) * 0.5;
                
                Biome biome = generator.getBiome(worldX, worldY);
                java.awt.Color biomeColor = biome.getColor(worldX, worldY);
                Color color = new Color(biomeColor.getRed(),
                        biomeColor.getGreen(), biomeColor.getBlue());
                double height = heightmap.getValue(worldX, worldY) * 10;
                
                int numZ = (int) MathsHelper.supervise(Math.round(height) + 1,
                        1, 20);
                
                for (int z = 0; z < numZ; z++)
                {
                    int tileZ = z * Tile.depth;
                    
                    range.add(new Tile(tileX, tileY, tileZ, color));
                }
                
            }
            tiles.add(range);
        }
    }
    
    public void update(ClientEngine engine)
    {
        for (List<Tile> range : tiles)
        {
            for (Tile tile : range)
            {
                tile.update(engine);
            }
        }
    }
    
    public void render(ClientEngine engine)
    {
        for (List<Tile> range : tiles)
        {
            for (Tile tile : range)
            {
                tile.render(engine);
            }
        }
    }
    
    public boolean contains(double x, double y)
    {
        boolean contains = false;
        
        for (List<Tile> range : tiles)
        {
            for (Tile tile : range)
            {
                if (!contains)
                {
                    contains = tile.contains(x, y);
                }
            }
        }
        
        return contains;
    }
    
    public static class Tile implements IChild
    {
        public final static int width  = 60;
        public final static int height = 30;
        public final static int depth  = 10;
        
        private final int       x;
        private final int       y;
        private final Color     baseColor;
        private Color           color;
        
        private boolean         hover  = false;
        
        public Tile(int x, int y, int z)
        {
            this(x, y, z, Color.random());
        }
        
        public Tile(int x, int y, int z, Color color)
        {
            this.x = x;
            this.y = y + z;
            
            this.baseColor = color;
            this.color = color;
        }
        
        public void update(ClientEngine engine)
        {
            int mouseX = Mouse.getX();
            int mouseY = Mouse.getY();
            
            if (contains(mouseX, mouseY))
            {
                if (!hover)
                {
                    color = color.darker();
                    hover = true;
                }
            }
            else
            {
                if (hover)
                {
                    color = baseColor.clone();
                    hover = false;
                }
            }
        }
        
        public void render(ClientEngine engine)
        {
            LWJGLOpenGL gl = engine.getGl();
            
            gl.startQuads();
            
            gl.color(color);
            gl.vertex(x + (width / 2), y + height);
            gl.vertex(x + width, y + (height / 2));
            gl.vertex(x + (width / 2), y);
            gl.vertex(x, y + (height / 2));
            
            gl.end();
            
            gl.startQuads();
            
            gl.color(color.darker(0.35f));
            gl.vertex(x, y + (height / 2));
            gl.vertex(x + (width / 2), y);
            gl.vertex(x + (width / 2), y - depth);
            gl.vertex(x, y + (height / 2) - depth);
            
            gl.end();
            
            gl.startQuads();
            
            gl.color(color.darker(0.35f));
            gl.vertex(x + (width / 2), y);
            gl.vertex(x + width, y + (height / 2));
            gl.vertex(x + width, y + (height / 2) - depth);
            gl.vertex(x + (width / 2), y - depth);
            
            gl.end();
        }
        
        public boolean contains(double x, double y)
        {
            double localX = x - this.x;
            double localY = y - this.y;
            
            if (localX > 0 && localX <= width && localY > 0 && localY <= height)
            {
                return true;
            }
            
            return false;
        }
    }
}
