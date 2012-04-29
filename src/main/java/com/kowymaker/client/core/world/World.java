package com.kowymaker.client.core.world;

import java.io.File;
import java.util.Map;

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
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;
import com.kowymaker.client.utils.LWJGLSequencer;
import com.kowymaker.spec.res.ResourcesManager;
import com.kowymaker.spec.res.impl.TileFile;
import com.kowymaker.spec.res.impl.TileFormat;

public class World implements IChild
{
    private int                               offsetX = 0;
    private int                               offsetY = 0;
    
    private Map<Integer, Map<Integer, Chunk>> chunks  = Maps.newLinkedHashMap();
    
    private LWJGLSequencer                    sequencer;
    
    private final WorldGenerator              generator;
    
    public static int                         tile    = 0;
    
    public World()
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
        
        // Sequencer
        try
        {
            TileFile tile = ResourcesManager.get(TileFormat.class).load(
                    new File("res/tiles/grass.kmr"));
            sequencer = new LWJGLSequencer(tile.getSequencer().getSequence(
                    "normal"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Tile getTileByWorldCoord(int x, int y)
    {
        int localX = x % Chunk.width;
        int localY = y % Chunk.height;
        
        if (localX < 0)
        {
            localX += Chunk.width;
        }
        
        if (localY < 0)
        {
            localY += Chunk.height;
        }
        
        return getChunkByWorldCoord(x, y).getTile(localX, localY);
    }
    
    public Chunk getChunkByWorldCoord(int x, int y)
    {
        int cx = x;
        int cy = y;
        
        if (x < 0)
        {
            cx++;
        }
        
        if (y < 0)
        {
            cy++;
        }
        
        int chunkX = (cx - (cx % Chunk.width)) / Chunk.width;
        int chunkY = (cy - (cy % Chunk.height)) / Chunk.height;
        
        if (x < 0)
        {
            chunkX--;
        }
        
        if (y < 0)
        {
            chunkY--;
        }
        
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
    
    public WorldGenerator getGenerator()
    {
        return generator;
    }
    
    public LWJGLSequencer getSequencer()
    {
        return sequencer;
    }
    
    public HeightMap getHeightmap()
    {
        return generator.getEnvironment().getParameter(HeightMap.class);
    }
    
    int n = 2;
    
    public void update(ClientEngine engine)
    {
        tile = 0;
        
        Chunk center = getChunkByWorldCoord(offsetX, offsetY);
        
        int startX = center.getX() - n;
        int endX = center.getX() + n;
        int startY = center.getY() - n;
        int endY = center.getY() + n;
        
        for (int x = startX; x <= endX; x++)
        {
            for (int y = startY; y <= endY; y++)
            {
                getChunk(x, y).update(engine);
            }
        }
        
        ClientEngine.text = "Tiles : " + tile;
    }
    
    public void render(ClientEngine engine)
    {
        Chunk center = getChunkByWorldCoord(offsetX, offsetY);
        
        int startX = center.getX() - n;
        int endX = center.getX() + n;
        int startY = center.getY() - n;
        int endY = center.getY() + n;
        
        for (int x = endX; x >= startX; x--)
        {
            for (int y = endY; y >= startY; y--)
            {
                Chunk chunk = getChunk(x, y);
                
                chunk.render(engine);
            }
        }
        
    }
    
    public boolean contains(double x, double y)
    {
        int wx = (int) Math.round(x);
        int wy = (int) Math.round(y);
        
        return getChunkByWorldCoord(wx, wy).contains(x, y);
    }
}
