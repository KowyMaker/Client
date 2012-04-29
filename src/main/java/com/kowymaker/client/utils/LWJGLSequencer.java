package com.kowymaker.client.utils;

import java.io.IOException;
import java.util.Arrays;

import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.lwjgl.LWJGLTexture;

import com.kowymaker.spec.utils.res.Sequencer;

public class LWJGLSequencer
{
    private final Sequencer.Sequence       sequence;
    private final Sequencer.SequenceRunner runner;
    private final ITexture[]               textures;
    
    public LWJGLSequencer(Sequencer.Sequence sequence)
    {
        this.sequence = sequence;
        runner = new Sequencer.SequenceRunner(sequence);
        textures = new ITexture[sequence.getLength()];
        Arrays.fill(textures, null);
    }
    
    public ITexture getTexture() throws IOException
    {
        return getTexture(System.currentTimeMillis());
    }
    
    public ITexture getTexture(long time) throws IOException
    {
        int index = runner.getIndex(time);
        ITexture tex = textures[index];
        
        if (tex == null)
        {
            tex = LWJGLTexture.uploadTextureToVideoRAM(sequence.getFrame(index)
                    .getData());
            textures[index] = tex;
        }
        
        return tex;
    }
}
