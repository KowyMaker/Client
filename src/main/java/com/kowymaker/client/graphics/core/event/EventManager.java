package com.kowymaker.client.graphics.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fenggui.binding.render.lwjgl.EventHelper;
import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.mouse.IMouseListener;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Maps;
import com.kowymaker.client.graphics.core.ClientApplet;

public class EventManager
{
    private final ClientApplet                           applet;
    
    private final Map<Integer, List<IKeyListener>>       keyListeners   = Maps.newLinkedHashMap();
    private final Map<MouseButton, List<IMouseListener>> mouseListeners = Maps.newEnumMap(MouseButton.class);
    
    public EventManager(ClientApplet applet)
    {
        this.applet = applet;
    }
    
    public void registerKeyListener(int key, IKeyListener keyListener)
    {
        if (!keyListeners.containsKey(key))
        {
            List<IKeyListener> listeners = new ArrayList<IKeyListener>();
            keyListeners.put(key, listeners);
        }
        
        keyListeners.get(key).add(keyListener);
    }
    
    public void registerMouseListener(MouseButton button,
            IMouseListener mouseListener)
    {
        if (!mouseListeners.containsKey(button))
        {
            List<IMouseListener> listeners = new ArrayList<IMouseListener>();
            mouseListeners.put(button, listeners);
        }
        
        mouseListeners.get(button).add(mouseListener);
    }
    
    public void update()
    {
        // Listen Mouse
        for (MouseButton button : mouseListeners.keySet())
        {
            if (Mouse.isButtonDown(button.getCode()))
            {
                MouseClickedEvent event = new MouseClickedEvent(null,
                        Mouse.getX(), Mouse.getY(), button, 0, null);
                
                for (IMouseListener mouseListener : mouseListeners.get(button))
                {
                    mouseListener.mouseClicked(event);
                }
            }
        }
        
        // Listen Keyboard
        while (Keyboard.next())
        {
            int code = Keyboard.getEventKey();
            Key key = EventHelper.mapEventKey();
            char c = EventHelper.mapKeyChar();
            
            if (keyListeners.containsKey(code))
            {
                for (IKeyListener keyListener : keyListeners.get(code))
                {
                    if (Keyboard.getEventKeyState())
                    {
                        KeyPressedEvent event = new KeyPressedEvent(null, c,
                                key, null);
                        
                        keyListener.keyPressed(event);
                    }
                    else
                    {
                        KeyReleasedEvent event = new KeyReleasedEvent(null, c,
                                key, null);
                        
                        keyListener.keyReleased(event);
                    }
                }
            }
        }
        
        for (int code : keyListeners.keySet())
        {
            for (IKeyListener keyListener : keyListeners.get(code))
            {
                if (Keyboard.isKeyDown(code))
                {
                    KeyPressedEvent event = new KeyPressedEvent(null, '0',
                            Key.UNDEFINED, null);
                    
                    keyListener.keyPressed(event);
                }
            }
        }
    }
    
    public ClientApplet getApplet()
    {
        return applet;
    }
    
    public Map<Integer, List<IKeyListener>> getKeyListeners()
    {
        return keyListeners;
    }
    
    public Map<MouseButton, List<IMouseListener>> getMouseListeners()
    {
        return mouseListeners;
    }
    
}
