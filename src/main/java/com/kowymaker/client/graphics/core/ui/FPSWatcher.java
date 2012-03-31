package com.kowymaker.client.graphics.core.ui;

import org.fenggui.binding.render.Graphics;
import org.fenggui.binding.render.ImageFont;
import org.fenggui.util.Color;

import com.kowymaker.client.graphics.core.ClientEngine;
import com.kowymaker.client.graphics.core.IChild;

public class FPSWatcher implements IChild
{
    private ImageFont font = null;
    
    public FPSWatcher()
    {
        this(ImageFont.getDefaultFont());
    }
    
    public FPSWatcher(ImageFont font)
    {
        this.font = font;
    }
    
    public void update(ClientEngine engine)
    {
        
    }
    
    public void render(ClientEngine engine)
    {
        Graphics g = engine.getBinding().getGraphics();
        
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("FPS: " + engine.getFps().getFps(), 0, 0);
        
        g.setColor(engine.getConfig().getBackground());
    }
    
    public boolean contains(double x, double y)
    {
        return false;
    }
    
    public ImageFont getFont()
    {
        return font;
    }
    
    public void setFont(ImageFont font)
    {
        this.font = font;
    }
    
}
