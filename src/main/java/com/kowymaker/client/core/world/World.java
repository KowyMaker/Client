package com.kowymaker.client.core.world;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.fenggui.binding.render.lwjgl.LWJGLTexture;

import com.google.common.collect.Maps;
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
import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;
import com.kowymaker.spec.utils.debug.Debug;

public class World implements IChild
{
    private final KowyMakerClient             main;
    
    private int                               offsetX = 0;
    private int                               offsetY = 0;
    
    private Map<Integer, Map<Integer, Chunk>> chunks  = Maps.newLinkedHashMap();
    
    private LWJGLTexture                      texture = null;
    
    private final WorldGenerator              generator;
    
    public World(KowyMakerClient main)
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
        
        this.main = main;
    }
    
    public Chunk getChunkByWorldCoord(int x, int y)
    {
        int chunkX = (x - (x % Chunk.width)) / Chunk.width;
        int chunkY = (y - (y % Chunk.height)) / Chunk.height;
        
        return getChunk(chunkX, chunkY);
    }
    
    public Chunk getChunk(int x, int y)
    {
        Map<Integer, Chunk> mapChunks = chunks.get(x);
        if (mapChunks == null)
        {
            mapChunks = Maps.newLinkedHashMap();
            chunks.put(x, mapChunks);
        }
        
        Chunk chunk = mapChunks.get(y);
        if (chunk == null)
        {
            chunk = new Chunk(this, x, y);
            chunk.generate();
            mapChunks.put(y, chunk);
        }
        
        return chunk;
    }
    
    public int getOffsetX()
    {
        return offsetX;
    }
    
    public void setOffsetX(int offsetX)
    {
        this.offsetX = offsetX;
    }
    
    public int getOffsetY()
    {
        return offsetY;
    }
    
    public void setOffsetY(int offsetY)
    {
        this.offsetY = offsetY;
    }
    
    public KowyMakerClient getMain()
    {
        return main;
    }
    
    public LWJGLTexture getTexture()
    {
        return texture;
    }
    
    public WorldGenerator getGenerator()
    {
        return generator;
    }
    
    public HeightMap getHeightmap()
    {
        return generator.getEnvironment().getParameter(HeightMap.class);
    }
    
    int n = 1;
    
    public void update(ClientEngine engine)
    {
        if (texture == null)
        {
            try
            {
                texture = LWJGLTexture.uploadTextureToVideoRAM(ImageIO
                        .read(new File("res/tiles/grass2.png")));
                
                System.out.println("Texture loaded: " + texture.getImageWidth()
                        + "*" + texture.getImageHeight() + "px");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        Chunk center = getChunkByWorldCoord(offsetX, offsetY);
        
        int startX = center.getX() - n;
        int endX = center.getX() + n;
        int startY = center.getY() - n;
        int endY = center.getY() + n;
        
        for (int x = startX; x <= endX; x++)
        {
            for (int y = startY; y <= endY; y++)
            {
                getChunk(x, y).render(engine);
            }
        }
    }
    
    boolean a = false;
    
    public void render(ClientEngine engine)
    {
        Chunk center = getChunkByWorldCoord(offsetX, offsetY);
        
        int startX = center.getX() - n;
        int endX = center.getX() + n;
        int startY = center.getY() - n;
        int endY = center.getY() + n;
        
        int num = 0;
        for (int x = endX; x >= startX; x--)
        {
            for (int y = endY; y >= startY; y--)
            {
                Chunk chunk = getChunk(x, y);
                
                chunk.render(engine);
                num++;
            }
        }
        if(!a)
        {
            System.out.println("Rendered " + num + " chunks.");
            System.out.println("Rendered " + (num * Chunk.width * Chunk.height) + " tiles.");
            a = true;
        }
    }
    
    public boolean contains(double x, double y)
    {
        int wx = (int) Math.round(x);
        int wy = (int) Math.round(y);
        
        return getChunkByWorldCoord(wx, wy).contains(x, y);
    }
}
