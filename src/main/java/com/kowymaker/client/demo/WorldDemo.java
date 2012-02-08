package com.kowymaker.client.demo;

import java.util.Map;

import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;

import com.google.common.collect.Maps;
import com.kowymaker.client.KowyMakerClient;
import com.kowymaker.client.core.world.World;

public class WorldDemo implements IKeyListener
{
    private final KowyMakerClient main;
    private final World           world;
    
    private final Map<Key, Long>  pressed = Maps.newEnumMap(Key.class);
    
    public WorldDemo(KowyMakerClient main, World world)
    {
        this.main = main;
        this.world = world;
    }
    
    public void keyPressed(KeyPressedEvent keyPressedEvent)
    {
        if (!pressed.containsKey(keyPressedEvent.getKeyClass()))
        {
            pressed.put(keyPressedEvent.getKeyClass(),
                    System.currentTimeMillis());
        }
        
        long latest = pressed.get(keyPressedEvent.getKeyClass());
        long diff = System.currentTimeMillis() - latest;
        
        if (diff > 300)
        {
            change(keyPressedEvent.getKeyClass());
        }
    }
    
    public void keyReleased(KeyReleasedEvent keyReleasedEvent)
    {
        if (pressed.containsKey(keyReleasedEvent.getKeyClass()))
        {
            pressed.remove(keyReleasedEvent.getKeyClass());
        }
    }
    
    public void keyTyped(KeyTypedEvent keyTypedEvent)
    {
        change(keyTypedEvent.getKeyClass());
    }
    
    public void change(Key key)
    {
        int dx = 0;
        int dy = 0;
        
        switch (key)
        {
            case LEFT:
                dx--;
                dy++;
                break;
            
            case RIGHT:
                dx++;
                dy--;
                break;
            
            case UP:
                dx++;
                dy++;
                break;
            
            case DOWN:
                dx--;
                dy--;
                break;
        }
        
        world.setOffsetX(world.getOffsetX() + dx);
        world.setOffsetY(world.getOffsetY() + dy);
    }
}
