/**
 *  This file is part of Kowy Maker - Client.
 *
 *  Kowy Maker - Client is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Kowy Maker - Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Kowy Maker - Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kowymaker.client.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Koka El Kiwi
 * 
 */
public class GameFrame extends Frame
{
    private static final long serialVersionUID = 553848661133182552L;
    private final GameWindow  window;
    private final Canvas      canvas           = new Canvas();
    
    public GameFrame(final GameWindow window)
    {
        this.window = window;
        
        canvas.setSize(new Dimension(window.getMain()
                .getConfiguration().getInteger("graphics.width"), window
                .getMain().getConfiguration().getInteger("graphics.height")));
        canvas.setLocation(8, 30);
        
        setLayout(null);
        add(canvas);
        
        setTitle(window.getMain().getConfiguration().getString("game.name"));
        
        setPreferredSize(new Dimension(window.getMain().getConfiguration()
                .getInteger("graphics.width"), window.getMain()
                .getConfiguration().getInteger("graphics.height")));
        pack();
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e)
            {
                window.getMain().stop();
            }
            
        });
        
        addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentResized(ComponentEvent e)
            {
                window.getRenderer().setDimension(GameFrame.this.getSize());
                canvas.setSize(GameFrame.this.getSize());
            }
            
        });
    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }
    
    public GameWindow getWindow()
    {
        return window;
    }
}
