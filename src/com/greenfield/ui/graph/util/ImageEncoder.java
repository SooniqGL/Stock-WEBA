// Source File Name:   ImageEncoder.java

package com.greenfield.ui.graph.util;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public abstract class ImageEncoder
    implements ImageConsumer
{

    public ImageEncoder(Image image, OutputStream outputstream)
        throws IOException
    {
        this(image.getSource(), outputstream);
    }

    public ImageEncoder(ImageProducer imageproducer, OutputStream outputstream)
        throws IOException
    {
        _$3271 = -1;
        _$3276 = -1;
        _$5697 = 0;
        _$5706 = false;
        _$5751 = null;
        _$5809 = false;
        _$2810 = imageproducer;
        out = outputstream;
    }

    abstract void encodeStart(int i, int j)
        throws IOException;

    abstract void encodePixels(int i, int j, int k, int l, int ai[], int i1, int j1)
        throws IOException;

    abstract void encodeDone()
        throws IOException;

    public synchronized void encode()
        throws IOException
    {
        _$5713 = true;
        _$5721 = null;
        _$2810.startProduction(this);
        while(_$5713) 
            try
            {
                wait();
            }
            catch(InterruptedException interruptedexception) { }
        if(_$5721 != null)
            throw _$5721;
        else
            return;
    }

    private void _$5830(int i, int j, int k, int l, int ai[], int i1, int j1)
        throws IOException
    {
        if(!_$5706)
        {
            _$5706 = true;
            encodeStart(_$3271, _$3276);
            if((_$5697 & 0x2) == 0)
            {
                _$5809 = true;
                _$5819 = new int[_$3271 * _$3276];
            }
        }
        if(_$5809)
        {
            for(int k1 = 0; k1 < l; k1++)
                System.arraycopy(ai, k1 * j1 + i1, _$5819, (j + k1) * _$3271 + i, k);

        } else
        {
            encodePixels(i, j, k, l, ai, i1, j1);
        }
    }

    private void _$5852()
        throws IOException
    {
        if(_$5809)
        {
            encodePixels(0, 0, _$3271, _$3276, _$5819, 0, _$3271);
            _$5819 = null;
            _$5809 = false;
        }
    }

    private synchronized void _$5864()
    {
        _$5713 = false;
        notifyAll();
    }

    public void setDimensions(int i, int j)
    {
        _$3271 = i;
        _$3276 = j;
    }

    public void setProperties(Hashtable hashtable)
    {
        _$5751 = hashtable;
    }

    public void setColorModel(ColorModel colormodel)
    {
    }

    public void setHints(int i)
    {
        _$5697 = i;
    }

    public void setPixels(int i, int j, int k, int l, ColorModel colormodel, byte abyte0[], int i1, 
            int j1)
    {
        int ai[] = new int[k];
        for(int k1 = 0; k1 < l; k1++)
        {
            int l1 = i1 + k1 * j1;
            for(int i2 = 0; i2 < k; i2++)
                ai[i2] = colormodel.getRGB(abyte0[l1 + i2] & 0xff);

            try
            {
                _$5830(i, j + k1, k, 1, ai, 0, k);
            }
            catch(IOException ioexception)
            {
                _$5721 = ioexception;
                _$5864();
                return;
            }
        }

    }

    public void setPixels(int i, int j, int k, int l, ColorModel colormodel, int ai[], int i1, 
            int j1)
    {
        if(colormodel == _$5734)
        {
            try
            {
                _$5830(i, j, k, l, ai, i1, j1);
            }
            catch(IOException ioexception)
            {
                _$5721 = ioexception;
                _$5864();
                return;
            }
        } else
        {
            int ai1[] = new int[k];
            for(int k1 = 0; k1 < l; k1++)
            {
                int l1 = i1 + k1 * j1;
                for(int i2 = 0; i2 < k; i2++)
                    ai1[i2] = colormodel.getRGB(ai[l1 + i2]);

                try
                {
                    _$5830(i, j + k1, k, 1, ai1, 0, k);
                }
                catch(IOException ioexception1)
                {
                    _$5721 = ioexception1;
                    _$5864();
                    return;
                }
            }

        }
    }

    public void imageComplete(int i)
    {
        _$2810.removeConsumer(this);
        if(i == 4)
            _$5721 = new IOException("image aborted");
        else
            try
            {
                _$5852();
                encodeDone();
            }
            catch(IOException ioexception)
            {
                _$5721 = ioexception;
            }
        _$5864();
    }

    protected OutputStream out;
    private ImageProducer _$2810;
    private int _$3271;
    private int _$3276;
    private int _$5697;
    private boolean _$5706;
    private boolean _$5713;
    private IOException _$5721;
    private static final ColorModel _$5734 = ColorModel.getRGBdefault();
    private Hashtable _$5751;
    private boolean _$5809;
    private int _$5819[];

}
