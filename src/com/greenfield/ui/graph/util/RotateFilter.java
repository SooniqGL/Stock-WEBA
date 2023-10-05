// Source File Name:   RotateFilter.java

package com.greenfield.ui.graph.util;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;
import java.util.Hashtable;

public class RotateFilter extends ImageFilter
{

    public RotateFilter(double d)
    {
        _$6940 = new double[2];
        _$2752 = d;
        _$2649 = Math.sin(d);
        _$2646 = Math.cos(d);
    }

    public void transform(double d, double d1, double ad[])
    {
        ad[0] = _$2646 * d + _$2649 * d1;
        ad[1] = _$2646 * d1 - _$2649 * d;
    }

    public void itransform(double d, double d1, double ad[])
    {
        ad[0] = _$2646 * d - _$2649 * d1;
        ad[1] = _$2646 * d1 + _$2649 * d;
    }

    public void transformBBox(Rectangle rectangle)
    {
        double d = (1.0D / 0.0D);
        double d1 = (1.0D / 0.0D);
        double d2 = (-1.0D / 0.0D);
        double d3 = (-1.0D / 0.0D);
        for(int i = 0; i <= 1; i++)
        {
            for(int j = 0; j <= 1; j++)
            {
                transform(rectangle.x + j * rectangle.width, rectangle.y + i * rectangle.height, _$6940);
                d = Math.min(d, _$6940[0]);
                d1 = Math.min(d1, _$6940[1]);
                d2 = Math.max(d2, _$6940[0]);
                d3 = Math.max(d3, _$6940[1]);
            }

        }

        rectangle.x = (int)Math.floor(d);
        rectangle.y = (int)Math.floor(d1);
        rectangle.width = ((int)Math.ceil(d2) - rectangle.x) + 1;
        rectangle.height = ((int)Math.ceil(d3) - rectangle.y) + 1;
    }

    public void setDimensions(int i, int j)
    {
        Rectangle rectangle = new Rectangle(0, 0, i, j);
        transformBBox(rectangle);
        _$6951 = -rectangle.x;
        _$6958 = -rectangle.y;
        _$6965 = i;
        _$6969 = j;
        _$6973 = rectangle.width;
        _$6977 = rectangle.height;
        _$6945 = new int[_$6965 * _$6969];
        consumer.setDimensions(_$6973, _$6977);
    }

    public void setProperties(Hashtable hashtable)
    {
        hashtable = (Hashtable)hashtable.clone();
        Object obj = hashtable.get("filters");
        if(obj == null)
            hashtable.put("filters", toString());
        else
        if(obj instanceof String)
            hashtable.put("filters", String.valueOf((String)obj) + String.valueOf(toString()));
        consumer.setProperties(hashtable);
    }

    public void setColorModel(ColorModel colormodel)
    {
        consumer.setColorModel(_$6930);
    }

    public void setHints(int i)
    {
        consumer.setHints(0xe | i & 0x10);
    }

    public void setPixels(int i, int j, int k, int l, ColorModel colormodel, byte abyte0[], int i1, 
            int j1)
    {
        int k1 = i1;
        int l1 = j * _$6965 + i;
        for(int i2 = 0; i2 < l; i2++)
        {
            for(int j2 = 0; j2 < k; j2++)
                _$6945[l1++] = colormodel.getRGB(abyte0[k1++] & 0xff);

            k1 += j1 - k;
            l1 += _$6965 - k;
        }

    }

    public void setPixels(int i, int j, int k, int l, ColorModel colormodel, int ai[], int i1, 
            int j1)
    {
        int k1 = i1;
        int l1 = j * _$6965 + i;
        if(colormodel == _$6930)
        {
            for(int i2 = 0; i2 < l; i2++)
            {
                System.arraycopy(ai, k1, _$6945, l1, k);
                k1 += j1;
                l1 += _$6965;
            }

        } else
        {
            for(int j2 = 0; j2 < l; j2++)
            {
                for(int k2 = 0; k2 < k; k2++)
                    _$6945[l1++] = colormodel.getRGB(ai[k1++]);

                k1 += j1 - k;
                l1 += _$6965 - k;
            }

        }
    }

    public void imageComplete(int i)
    {
        if(i == 1 || i == 4)
        {
            consumer.imageComplete(i);
            return;
        }
        int ai[] = new int[_$6973];
        for(int j = 0; j < _$6977; j++)
        {
            itransform(0 - _$6951, j - _$6958, _$6940);
            double d = _$6940[0];
            double d1 = _$6940[1];
            itransform(_$6973 - _$6951, j - _$6958, _$6940);
            double d2 = _$6940[0];
            double d3 = _$6940[1];
            double d4 = (d2 - d) / (double)_$6973;
            double d5 = (d3 - d1) / (double)_$6973;
            for(int k = 0; k < _$6973; k++)
            {
                int l = (int)Math.round(d);
                int i1 = (int)Math.round(d1);
                if(l < 0 || i1 < 0 || l >= _$6965 || i1 >= _$6969)
                    ai[k] = 0;
                else
                    ai[k] = _$6945[i1 * _$6965 + l];
                d += d4;
                d1 += d5;
            }

            consumer.setPixels(0, j, _$6973, 1, _$6930, ai, 0, _$6973);
        }

        consumer.imageComplete(i);
    }

    private static ColorModel _$6930 = ColorModel.getRGBdefault();
    private double _$2752;
    private double _$2649;
    private double _$2646;
    private double _$6940[];
    private int _$6945[];
    private int _$6951;
    private int _$6958;
    private int _$6965;
    private int _$6969;
    private int _$6973;
    private int _$6977;

}
