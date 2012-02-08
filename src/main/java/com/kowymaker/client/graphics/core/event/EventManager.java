package com.kowymaker.client.graphics.core.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenggui.Display;
import org.fenggui.binding.render.lwjgl.EventHelper;
import org.fenggui.binding.render.lwjgl.LWJGLEventBinder;
import org.fenggui.event.key.IKeyListener;
import org.fenggui.event.key.Key;
import org.fenggui.event.key.KeyPressedEvent;
import org.fenggui.event.key.KeyReleasedEvent;
import org.fenggui.event.key.KeyTypedEvent;
import org.fenggui.event.mouse.IMouseListener;
import org.fenggui.event.mouse.MouseButton;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseDoubleClickedEvent;
import org.fenggui.event.mouse.MouseDraggedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;
import org.fenggui.event.mouse.MouseMovedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.event.mouse.MouseWheelEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kowymaker.client.graphics.core.ClientApplet;

public class EventManager
{
    private final ClientApplet                             applet;
    private Display                                        display        = null;
    
    private final Map<Integer, List<IKeyListener>>         keyListeners   = Maps.newLinkedHashMap();
    private final Map<Integer, LWJGLEventBinder.State>     keyStates      = Maps.newLinkedHashMap();
    private final Map<Integer, Character>                  keyCharValues  = Maps.newLinkedHashMap();
    private final Set<Key>                                 modifiers      = Sets.newTreeSet();
    
    private final Map<MouseButton, List<IMouseListener>>   mouseListeners = Maps.newEnumMap(MouseButton.class);
    private final Map<MouseButton, LWJGLEventBinder.State> mouseStates    = Maps.newEnumMap(MouseButton.class);
    
    public EventManager(ClientApplet applet)
    {
        this.applet = applet;
    }
    
    public void registerKeyListener(int key, IKeyListener keyListener)
    {
        List<IKeyListener> listeners = keyListeners.get(key);
        
        if (listeners == null)
        {
            listeners = new LinkedList<IKeyListener>();
            keyListeners.put(key, listeners);
        }
        
        listeners.add(keyListener);
    }
    
    public void registerMouseListener(MouseButton button,
            IMouseListener mouseListener)
    {
        List<IMouseListener> listeners = mouseListeners.get(button);
        
        if (listeners == null)
        {
            listeners = new LinkedList<IMouseListener>();
            mouseListeners.put(button, listeners);
        }
        
        listeners.add(mouseListener);
    }
    
    public void update()
    {
        // Listen mouse
        while (Mouse.next())
        {
            int code = Mouse.getEventButton();
            MouseButton button = MouseButton.valueOf(code);
            LWJGLEventBinder.State state = mouseStates.get(button);
            
            IMouseListener listener = getMouseListeners(button);
            
            if (state == null)
            {
                state = new LWJGLEventBinder.State(Mouse.isButtonDown(code));
                mouseStates.put(button, state);
            }
            
            final long current = System.currentTimeMillis();
            final long diff = current - state.getLastPressed();
            
            if (Mouse.isButtonDown(button.getCode()))
            {
                if (!state.isPressed())
                {
                    if (display != null)
                    {
                        if (!display.fireMousePressedEvent(Mouse.getX(),
                                Mouse.getY(), button, 1))
                        {
                            listener.mousePressed(new MousePressedEvent(null,
                                    Mouse.getX(), Mouse.getY(), button, 0,
                                    modifiers));
                        }
                    }
                    else
                    {
                        listener.mousePressed(new MousePressedEvent(null, Mouse
                                .getX(), Mouse.getY(), button, 0, modifiers));
                    }
                    state.setPressed(true);
                    state.setLastPressed(current);
                }
                
                if (button == MouseButton.LEFT)
                {
                    if (display != null)
                    {
                        if (!display.fireMouseDraggedEvent(Mouse.getX(),
                                Mouse.getY(), button, 1))
                        {
                            listener.mouseDragged(new MouseDraggedEvent(null,
                                    Mouse.getX(), Mouse.getY(), button,
                                    modifiers));
                        }
                    }
                    else
                    {
                        listener.mouseDragged(new MouseDraggedEvent(null, Mouse
                                .getX(), Mouse.getY(), button, modifiers));
                    }
                }
            }
            else
            {
                if (state.isPressed())
                {
                    if (display != null)
                    {
                        if (!display.fireMouseReleasedEvent(Mouse.getX(),
                                Mouse.getY(), button, 1))
                        {
                            listener.mouseReleased(new MouseReleasedEvent(null,
                                    Mouse.getX(), Mouse.getY(), button, 0,
                                    modifiers));
                        }
                    }
                    else
                    {
                        listener.mouseReleased(new MouseReleasedEvent(null,
                                Mouse.getX(), Mouse.getY(), button, 0,
                                modifiers));
                    }
                    
                    if (diff < 300)
                    {
                        if (display != null)
                        {
                            if (!display.fireMouseClickEvent(Mouse.getX(),
                                    Mouse.getY(), button, 1))
                            {
                                listener.mouseClicked(new MouseClickedEvent(
                                        null, Mouse.getX(), Mouse.getY(),
                                        button, 1, modifiers));
                            }
                        }
                        else
                        {
                            listener.mouseClicked(new MouseClickedEvent(null,
                                    Mouse.getX(), Mouse.getY(), button, 1,
                                    modifiers));
                        }
                    }
                    
                    state.setPressed(false);
                }
            }
        }
        
        if (Mouse.isInsideWindow())
        {
            if (display != null)
            {
                display.fireMouseMovedEvent(Mouse.getX(), Mouse.getY(),
                        MouseButton.LEFT, 0);
            }
        }
        
        // Listen Keyboard
        while (Keyboard.next())
        {
            int code = Keyboard.getEventKey();
            char c = EventHelper.mapKeyChar();
            Key key = EventHelper.mapEventKey(code);
            
            LWJGLEventBinder.State state = keyStates.get(code);
            if (state == null)
            {
                state = new LWJGLEventBinder.State(Keyboard.isKeyDown(code),
                        System.currentTimeMillis());
                keyStates.put(code, state);
            }
            
            final long current = System.currentTimeMillis();
            final long diff = current - state.getLastPressed();
            IKeyListener listener = getKeyListeners(code);
            
            if (Keyboard.getEventKeyState())
            {
                setChar(code, c);
                
                if (!state.isPressed())
                {
                    if (display != null)
                    {
                        if (!display.fireKeyPressedEvent(c, key))
                        {
                            listener.keyPressed(new KeyPressedEvent(null, c,
                                    key, modifiers));
                        }
                        else
                        {
                            listener.keyPressed(new KeyPressedEvent(null, c,
                                    key, modifiers));
                        }
                    }
                    
                    state.setPressed(true);
                    state.setLastPressed(current);
                    
                    modifiers.add(key);
                }
            }
            else
            {
                if (keyCharValues.containsKey(code))
                {
                    c = getChar(code);
                }
                
                if (state.isPressed())
                {
                    if (display != null)
                    {
                        if (!display.fireKeyReleasedEvent(c, key))
                        {
                            listener.keyReleased(new KeyReleasedEvent(null, c,
                                    key, modifiers));
                        }
                    }
                    else
                    {
                        listener.keyReleased(new KeyReleasedEvent(null, c, key,
                                modifiers));
                    }
                    
                    if (diff < 300)
                    {
                        if (display != null)
                        {
                            if (!display.fireKeyTypedEvent(c))
                            {
                                listener.keyTyped(new KeyTypedEvent(null, c,
                                        key, modifiers));
                            }
                        }
                        else
                        {
                            listener.keyTyped(new KeyTypedEvent(null, c, key,
                                    modifiers));
                        }
                    }
                    
                    state.setPressed(false);
                    
                    modifiers.remove(key);
                }
            }
        }
        
        for (int code : keyStates.keySet())
        {
            Key key = EventHelper.mapEventKey(code);
            IKeyListener listener = getKeyListeners(code);
            
            if (Keyboard.isKeyDown(code) && keyCharValues.containsKey(code))
            {
                char c = getChar(code);
                
                if (display != null)
                {
                    if (!display.fireKeyPressedEvent(c, key))
                    {
                        listener.keyPressed(new KeyPressedEvent(null, c, key,
                                modifiers));
                    }
                }
                else
                {
                    listener.keyPressed(new KeyPressedEvent(null, c, key,
                            modifiers));
                }
            }
        }
    }
    
    public ListKeyListener getKeyListeners(int code)
    {
        List<IKeyListener> listeners = keyListeners.get(code);
        if (listeners == null)
        {
            listeners = new LinkedList<IKeyListener>();
        }
        
        return new ListKeyListener(listeners);
    }
    
    public ListMouseListener getMouseListeners(MouseButton button)
    {
        List<IMouseListener> listeners = mouseListeners.get(button);
        if (listeners == null)
        {
            listeners = new LinkedList<IMouseListener>();
        }
        
        return new ListMouseListener(listeners);
    }
    
    public void setChar(int code, char c)
    {
        keyCharValues.put(code, c);
    }
    
    public char getChar(int code)
    {
        return keyCharValues.get(code);
    }
    
    public ClientApplet getApplet()
    {
        return applet;
    }
    
    public Display getDisplay()
    {
        return display;
    }
    
    public void setDisplay(Display display)
    {
        this.display = display;
    }
    
    private class ListKeyListener implements IKeyListener
    {
        private final List<IKeyListener> keyListeners;
        
        public ListKeyListener(List<IKeyListener> keyListeners)
        {
            this.keyListeners = keyListeners;
        }
        
        public void keyPressed(KeyPressedEvent event)
        {
            for (IKeyListener keyListener : keyListeners)
            {
                keyListener.keyPressed(event);
            }
        }
        
        public void keyReleased(KeyReleasedEvent event)
        {
            for (IKeyListener keyListener : keyListeners)
            {
                keyListener.keyReleased(event);
            }
        }
        
        public void keyTyped(KeyTypedEvent event)
        {
            for (IKeyListener keyListener : keyListeners)
            {
                keyListener.keyTyped(event);
            }
        }
        
    }
    
    private class ListMouseListener implements IMouseListener
    {
        private final List<IMouseListener> mouseListeners;
        
        public ListMouseListener(List<IMouseListener> mouseListeners)
        {
            this.mouseListeners = mouseListeners;
        }
        
        public void mouseDragged(MouseDraggedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseDragged(event);
            }
        }
        
        public void mouseEntered(MouseEnteredEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseEntered(event);
            }
        }
        
        public void mouseExited(MouseExitedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseExited(event);
            }
        }
        
        public void mouseMoved(MouseMovedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseMoved(event);
            }
        }
        
        public void mousePressed(MousePressedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mousePressed(event);
            }
        }
        
        public void mouseClicked(MouseClickedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseClicked(event);
            }
        }
        
        public void mouseDoubleClicked(MouseDoubleClickedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseDoubleClicked(event);
            }
        }
        
        public void mouseReleased(MouseReleasedEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseReleased(event);
            }
        }
        
        public void mouseWheel(MouseWheelEvent event)
        {
            for (IMouseListener mouseListener : mouseListeners)
            {
                mouseListener.mouseWheel(event);
            }
        }
    }
}
