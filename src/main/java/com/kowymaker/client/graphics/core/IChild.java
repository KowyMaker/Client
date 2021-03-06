package com.kowymaker.client.graphics.core;

public interface IChild
{
    public void update(ClientEngine engine);
    
    public void render(ClientEngine engine);
    
    public boolean contains(double x, double y);
}
