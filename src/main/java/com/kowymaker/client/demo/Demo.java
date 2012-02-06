package com.kowymaker.client.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.binding.render.lwjgl.LWJGLOpenGL;
import org.fenggui.binding.render.lwjgl.LWJGLTexture;
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
import com.kokakiwi.maths.generator.world.env.Parameter;
import com.kokakiwi.maths.generator.world.gen.Biome;
import com.kokakiwi.maths.generator.world.utils.MathsHelper;
import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class Demo implements IChild
{
    private final List<List<List<Tile>>> tiles   = new LinkedList<List<List<Tile>>>();
    private final WorldGenerator         generator;
    private final HeightMap              heightmap;
    private final Temperature            temperature;
    
    private int                          baseX   = 0;
    private int                          baseY   = 0;
    
    public static LWJGLTexture           texture = null;
    
    public static String                 status  = "";
    
    private final static int             TIMEOUT = 350;
    
    public Demo(final KowyMakerClient main)
    {
        generator = new WorldGenerator("kq452f25fqfs*/q-s*/-sq*fs/q*+/qf");
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
        temperature = generator.getEnvironment()
                .getParameter(Temperature.class);
        
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
        
        for (int y = 42; y >= 0; y--)
        {
            int startX = (y % 2 == 0) ? 0 : 1;
            List<List<Tile>> range = new LinkedList<List<Tile>>();
            for (int x = startX; x < 41; x += 2)
            {
                
                int tileX = x * (Tile.width / 2);
                int tileY = y * (Tile.height / 2);
                
                double worldX = (tileX + baseX) * 0.15;
                double worldY = (tileY + baseY) * 0.15;
                
                Biome biome = generator.getBiome(worldX, worldY);
                java.awt.Color biomeColor = biome.getColor(worldX, worldY);
                Color color = new Color(biomeColor.getRed(),
                        biomeColor.getGreen(), biomeColor.getBlue());
                
                if (biome instanceof SnowBiome)
                {
                    double temp = temperature.getValue(worldX, worldY) * 0.01;
                    color = color.brighter(MathsHelper.supervise(
                            Float.parseFloat(Double.toString(temp)), 0, 0.2f));
                }
                else
                {
                    double temp = temperature.getValue(worldX, worldY) * 0.01;
                    color = color.darker(MathsHelper.supervise(
                            1 - Float.parseFloat(Double.toString(temp)), 0,
                            0.8f));
                }
                
                double height = heightmap.getValue(worldX, worldY) * 10;
                
                int numZ = (int) MathsHelper.supervise(Math.round(height) + 1,
                        1, 20);
                
                List<Tile> range2 = new LinkedList<Tile>();
                
                for (int z = 0; z < numZ; z++)
                {
                    int tileZ = z * Tile.depth;
                    
                    range2.add(new Tile(tileX, tileY, tileZ, color, biome
                            .getClass().getSimpleName()));
                }
                
                range.add(range2);
                
                for (Parameter param : generator.getEnvironment()
                        .getParameters().values())
                {
                    param.reset();
                }
            }
            tiles.add(range);
        }
    }
    
    public void update(ClientEngine engine)
    {
        boolean checked = false;
        
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();
        
        for (int i = tiles.size() - 1; i >= 0; i--)
        {
            List<List<Tile>> range = tiles.get(i);
            
            for (List<Tile> range2 : range)
            {
                for (int j = range2.size() - 1; j >= 0; j--)
                {
                    Tile tile = range2.get(j);
                    
                    if (tile.contains(mouseX, mouseY))
                    {
                        if (!checked)
                        {
                            tile.hover = true;
                            checked = true;
                        }
                        else
                        {
                            tile.hover = false;
                        }
                    }
                    else
                    {
                        tile.hover = false;
                    }
                    
                    tile.update(engine);
                }
            }
        }
        
        if (!checked)
        {
            status = "";
        }
    }
    
    public void render(ClientEngine engine)
    {
        if (texture == null)
        {
            try
            {
                texture = LWJGLTexture.uploadTextureToVideoRAM(ImageIO
                        .read(new File("res/tiles/grass.png")));
                
                System.out.println("Texture loaded: " + texture.getImageWidth()
                        + "*" + texture.getImageHeight() + "px");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        for (List<List<Tile>> range : tiles)
        {
            for (List<Tile> range2 : range)
            {
                for (Tile tile : range2)
                {
                    tile.render(engine);
                }
            }
        }
        
        ImageFont font = ImageFont.getDefaultFont();
        Graphics g = engine.getBinding().getGraphics();
        
        engine.getGl().color(Color.WHITE);
        g.setFont(font);
        g.drawString(status, 0,
                engine.getConfig().getHeight() - font.getHeight());
    }
    
    public boolean contains(double x, double y)
    {
        boolean contains = false;
        
        return contains;
    }
    
    public static class Tile implements IChild
    {
        public final static int width  = 60;
        public final static int height = 30;
        public final static int depth  = 10;
        
        private final int       x;
        private final int       y;
        private final Color     color;
        private final String    biomeName;
        
        private boolean         hover  = false;
        
        public Tile(int x, int y, int z, Color color, String biomeName)
        {
            this.x = x;
            this.y = y + z;
            
            this.color = color;
            this.biomeName = biomeName;
        }
        
        public void update(ClientEngine engine)
        {
            if (hover)
            {
                status = "Tile@[" + x + ";" + y + "] - Biome : '" + biomeName
                        + "'";
            }
        }
        
        public void render(ClientEngine engine)
        {
            LWJGLOpenGL gl = engine.getGl();
            
            Graphics g = engine.getBinding().getGraphics();
            if (hover)
            {
                gl.color(color.darker());
            }
            else
            {
                gl.color(color);
            }
            g.drawImage(texture, x, y - 10);
            
            // gl.startQuads();
            //
            // gl.color(color);
            // gl.vertex(x + (width / 2), y + height);
            // gl.vertex(x + width, y + (height / 2));
            // gl.vertex(x + (width / 2), y);
            // gl.vertex(x, y + (height / 2));
            //
            // gl.end();
            //
            // gl.startQuads();
            //
            // gl.color(color.darker(0.15f));
            // gl.vertex(x, y + (height / 2));
            // gl.vertex(x + (width / 2), y);
            // gl.vertex(x + (width / 2), y - depth);
            // gl.vertex(x, y + (height / 2) - depth);
            //
            // gl.end();
            //
            // gl.startQuads();
            //
            // gl.color(color.darker(0.15f));
            // gl.vertex(x + (width / 2), y);
            // gl.vertex(x + width, y + (height / 2));
            // gl.vertex(x + width, y + (height / 2) - depth);
            // gl.vertex(x + (width / 2), y - depth);
            //
            // gl.end();
        }
        
        public boolean contains(double x, double y)
        {
            double localX = x - this.x;
            double localY = y - this.y;
            
            if (localX >= 0 && localX <= width && localY >= -10
                    && localY <= height)
            {
                double limitUpY;
                
                if (localX < width / 2)
                {
                    limitUpY = (height / 2) + ((height * localX) / width);
                }
                else
                {
                    limitUpY = (height / 2)
                            + ((height / 2) + (-height * (localX - (width / 2)) / width));
                }
                
                if (localY < limitUpY)
                {
                    double limitDownY;
                    
                    if (localX < width / 2)
                    {
                        limitDownY = (-height * localX / width) + (height / 2)
                                - depth;
                    }
                    else
                    {
                        limitDownY = (height * (localX - (width / 2)) / width)
                                - depth;
                    }
                    
                    if (localY > limitDownY)
                    {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        public boolean isHover()
        {
            return hover;
        }
    }
}
