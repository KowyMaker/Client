package com.kowymaker.client.graphics.core.ui;

import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.composite.Window;
import org.fenggui.layout.BorderLayout;
import org.fenggui.theme.css.CSSTheme;
import org.fenggui.util.Point;

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
        display.setLayoutManager(new BorderLayout());
        
        CSSTheme theme = new CSSTheme(
                UserInterface.class.getResource("/style.css"));
        FengGUI.setTheme(theme);
        
        Window window = FengGUI.createWindow(true, true, true, true);
        window.setTitle("Hello world!");
        window.setPosition(new Point(10, 10));
        window.setSize(300, 100);
        
        display.addWidget(window);
        
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
