package com.kowymaker.client.graphics.core.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.TextEditor;
import org.fenggui.composite.Window;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.GridLayout;
import org.fenggui.theme.ITheme;
import org.fenggui.theme.css.CSSTheme;
import org.fenggui.util.Point;

import aurelienribon.ui.css.Style;

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
        // FengGUI.setTheme(new DefaultTheme());
        ITheme theme = null;
        theme = new CSSTheme(UserInterface.class.getResource("/style.css"));
        FengGUI.setTheme(theme);
        
        // Save manuals
        File functionsManualFile = new File("functions-manual.txt");
        File propertiesManualFile = new File("properties-manual.txt");
        try
        {
            IOUtils.write(Style.getFunctionsManual(), new FileOutputStream(
                    functionsManualFile));
            IOUtils.write(Style.getPropertiesManual(), new FileOutputStream(
                    propertiesManualFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        Window window = new Window();
        window.setTitle("Window #1");
        window.setId("window-1");
        window.setStyleClass("colored");
        window.setPosition(new Point(20, 20));
        window.getContentContainer().setLayoutManager(new GridLayout(3, 2));
        
        {
            Label label = new Label("Label :");
            
            TextEditor text = new TextEditor();
            text.setMultiline(false);
            
            window.getContentContainer().addWidget(label, text);
        }
        
        Window window2 = new Window();
        window2.setTitle("Window #2");
        window2.setId("window-2");
        window2.setPosition(new Point(100, 100));
        window2.getContentContainer().setLayoutManager(new BorderLayout());
        
        {
            
        }
        
        display.addWidget(window, window2);
        FengGUI.getTheme().setUp(display);
        
        window.setResizable(true);
        
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
