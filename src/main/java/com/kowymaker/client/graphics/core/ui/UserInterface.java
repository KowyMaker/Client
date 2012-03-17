package com.kowymaker.client.graphics.core.ui;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.theme.DefaultTheme;

import com.kowymaker.client.graphics.core.ClientApplet;
import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class UserInterface implements IChild
{
    private final ClientApplet applet;
    
    private Display            display = null;
    
    public UserInterface(ClientApplet applet)
    {
        this.applet = applet;
    }
    
    public ClientApplet getApplet()
    {
        return applet;
    }
    
    public Display getDisplay()
    {
        return display;
    }
    
    public void update(ClientEngine engine)
    {
        if (display == null)
        {
            init(engine);
        }
    }
    
    public void render(ClientEngine engine)
    {
        display.display();
    }
    
    public boolean contains(double x, double y)
    {
        return true;
    }
    
    private void init(ClientEngine engine)
    {
        display = new Display(engine.getBinding());
        FengGUI.setTheme(new DefaultTheme());
        
        engine.getEventManager().setDisplay(display);
    }
    
    public void resize(int width, int height)
    {
        if (display != null)
        {
            display.setSize(width, height);
        }
    }
}
